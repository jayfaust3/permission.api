package com.permission.api.api.consumers

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.core.Message as RabbitMQMessage;
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import com.permission.api.application.services.crud.IPermissionCRUDService
import com.common.models.api.request.messaging.base.Message
import com.common.models.api.request.messaging.permission.PermissionMessagingReadRequest
import com.permission.api.common.models.application.permission.Scope
import com.permission.api.configuration.RabbitMQConfiguration
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.connection.CorrelationData

const val QUEUE = "get-permissions-by-entity"

@RabbitListener(queues = [QUEUE])
class GetPermissionsByEntityConsumer (
        private val config: RabbitMQConfiguration,
        private val rabbitTemplate: RabbitTemplate,
        private val crudService: IPermissionCRUDService
        ){

        @RabbitHandler
        fun receive(message: RabbitMQMessage): Unit {
                val messageBytes = message.body
                val messageProperties = message.messageProperties

                val responseBytes = getResponse(messageBytes)

                val responseMessage = RabbitMQMessage(responseBytes, messageProperties)

                val correlationData = getCorrelationData(messageProperties)

                rabbitTemplate.sendAndReceive(
                        config.RPC_EXCHANGE,
                        QUEUE,
                        responseMessage,
                        correlationData
                )
        }

        private fun getResponse(messageBytes: ByteArray): ByteArray {
                val messageJSON = String(messageBytes, charset("UTF-8"))
                val deserializedMessage = Json.decodeFromString<Message<PermissionMessagingReadRequest>>(messageJSON)
                val request = deserializedMessage.data
                val permissions: List<Scope> = crudService.getEntityPermissions(
                        request.actorType,
                        request.entityId
                )
                val responseJSON = Json.encodeToString(
                        Message(
                                permissions
                        )
                )
                return responseJSON.toByteArray()
        }

        private fun getCorrelationData(messageProperties: MessageProperties): CorrelationData {
                val correlationId = messageProperties.correlationId
                return CorrelationData(correlationId)
        }
}