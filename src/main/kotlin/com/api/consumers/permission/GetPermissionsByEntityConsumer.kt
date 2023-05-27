package com.permission.api.api.consumers

import org.springframework.stereotype.Service
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.core.Message as RabbitMQMessage;
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.connection.CorrelationData
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import com.permission.api.application.services.crud.IPermissionCRUDService
import com.permission.api.common.models.api.request.messaging.base.Message
import com.permission.api.common.models.api.request.messaging.permission.PermissionMessagingReadRequest
import com.permission.api.common.enums.ActorType
import com.permission.api.common.models.application.permission.Scope

const val QUEUE = "queue:get-permissions-by-entity"

@Service
class GetPermissionsByEntityConsumer (
        private val rabbitTemplate: RabbitTemplate,
        private val crudService: IPermissionCRUDService,
        ) {

        @RabbitListener(queues = [QUEUE])
        fun receive(message: RabbitMQMessage): Unit {
                val messageBytes = message.body
                val messageProperties = message.messageProperties

                val responseBytes = getResponse(messageBytes)
                val responseMessage = RabbitMQMessage(responseBytes, messageProperties)

                val correlationData = getCorrelationData(messageProperties)

                val replyTo = messageProperties.replyTo

                rabbitTemplate.send(replyTo, replyTo, responseMessage, correlationData)
        }

        private fun getResponse(messageBytes: ByteArray): ByteArray {
                val messageJSON = String(messageBytes, charset("UTF-8"))
                val deserializedMessage = Json.decodeFromString<Message<PermissionMessagingReadRequest>>(messageJSON)
                val request = deserializedMessage.data
                val actorType = ActorType.valueOf(request.actorType) ?: ActorType.USER
                val entityId = request.entityId

                val permissions: List<Scope> = crudService.getEntityPermissions(actorType, entityId)

                val responseMessage = Message(permissions)
                val responseJSON = Json.encodeToString(responseMessage)

                return responseJSON.toByteArray()
        }

        private fun getCorrelationData(messageProperties: MessageProperties): CorrelationData {
                val correlationId = messageProperties.correlationId
                return CorrelationData(correlationId)
        }
}
