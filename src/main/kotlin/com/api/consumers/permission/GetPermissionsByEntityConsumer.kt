package com.permission.api.api.consumers

import org.springframework.context.annotation.Bean
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.core.Message as RabbitMQMessage;
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.CorrelationData
import com.permission.api.application.services.crud.IPermissionCRUDService
import com.common.models.api.request.messaging.base.Message
import com.common.models.api.request.messaging.permission.PermissionMessagingReadRequest
import com.permission.api.common.models.application.permission.Scope
import com.permission.api.configuration.RabbitMQConfiguration
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter

const val QUEUE = "queue:get-permissions-by-entity"

@RabbitListener(queues = [QUEUE])
class GetPermissionsByEntityConsumer {

        private val config: RabbitMQConfiguration
        private val rabbitTemplate: RabbitTemplate
        private val crudService: IPermissionCRUDService

        constructor (
                config: RabbitMQConfiguration,
                rabbitTemplate: RabbitTemplate,
                crudService: IPermissionCRUDService
        ) {
                this.config = config
                this.rabbitTemplate = rabbitTemplate
                this.crudService = crudService
//                val connectionFactory = CachingConnectionFactory(config.host)
//                connectionFactory.virtualHost = config.virtualHost
//                connectionFactory.port = config.port
//                connectionFactory.username = config.username
//                connectionFactory.setPassword(config.password)
//                connectionFactory.setPublisherConfirmType(
//                        if (config.publisherConfirmType == "correlated")
//                                CachingConnectionFactory.ConfirmType.CORRELATED
//                        else
//                                CachingConnectionFactory.ConfirmType.NONE
//                )
//                connectionFactory.isPublisherReturns = config.publisherReturns
//                this.rabbitTemplate = RabbitTemplate(connectionFactory)
//                Jackson2JsonMessageConverter().also { this.rabbitTemplate.messageConverter = it }
        }

        @RabbitHandler
        fun receive(message: RabbitMQMessage): Unit {
                val messageBytes = message.body
                val messageProperties = message.messageProperties

                val responseBytes = getResponse(messageBytes)

                val responseMessage = RabbitMQMessage(responseBytes, messageProperties)

                val correlationData = getCorrelationData(messageProperties)

                this.rabbitTemplate.sendAndReceive(
                        config.RPC_EXCHANGE,
                        config.GET_PERMISSIONS_BY_ENTITY_ROUTING_KEY,
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
