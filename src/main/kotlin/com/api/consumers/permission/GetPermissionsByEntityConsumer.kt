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
import org.springframework.amqp.rabbit.connection.CorrelationData

const val QUEUE = "get-permissions-by-entity"

@RabbitListener(queues = [QUEUE])
class GetPermissionsByEntityConsumer (
//        @Value(value = "\${app.rabbit-mq.host}")
//        private val rabbitMQHost: String,
//        @Value(value = "\${app.rabbit-mq.virtualHost}")
//        private val rabbitMQVirtualHost: String,
//        @Value(value = "\${app.rabbit-mq.port}")
//        private val rabbitMQPort: String,
//        @Value(value = "\${app.rabbit-mq.username}")
//        private val rabbitMQUserName: String,
//        @Value(value = "\${app.rabbit-mq.password}")
//        private val rabbitMQPassword: String,
        private val config: RabbitMQConfiguration,
        private val rabbitTemplate: RabbitTemplate,
        private val crudService: IPermissionCRUDService
        ){

        // private val getPermissionsByEntityQueueName = "rpc/permissions"

//        init {
//                val factory = ConnectionFactory()
//                factory.host = rabbitMQHost
//                factory.virtualHost = rabbitMQVirtualHost
//                factory.port = Integer.parseInt(rabbitMQPort)
//                factory.username = rabbitMQUserName
//                factory.password = rabbitMQPassword
//
//                val connection = factory.newConnection()
//                val channel = connection.createChannel()
//                channel.queueDeclare(getPermissionsByEntityQueueName, false, false, false, null)
//                channel.basicQos(1)
//
//                val consumer = object : DefaultConsumer(channel) {
//                        override fun handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: ByteArray) {
//                                val replyProps = AMQP.BasicProperties.Builder()
//                                        .correlationId(properties.correlationId)
//                                        .build()
//                                val messageJSON = String(body, charset("UTF-8"))
//                                val message = Json.decodeFromString<Message<PermissionMessagingReadRequest>>(messageJSON)
//                                val request = message.data
//                                val permissions: List<Scope> = crudService.getEntityPermissions(
//                                        request.actorType,
//                                        request.entityId
//                                )
//                                val responseJSON = Json.encodeToString(
//                                        Message(
//                                                permissions
//                                        )
//                                )
//                                channel.basicPublish(getPermissionsByEntityQueueName, properties.replyTo, replyProps, responseJSON.toByteArray())
//                                channel.basicAck(envelope.deliveryTag, false)
//                        }
//                }
//
//                channel.basicConsume(getPermissionsByEntityQueueName, false, consumer)
//                // Wait and be prepared to consume the message from RPC client.
//                while (true) {
//                        synchronized(consumer) {
//                                (consumer as Object).wait()
//                        }
//                }
//        }

        @RabbitHandler
        fun receive(message: RabbitMQMessage): Unit {
                message.messageProperties.headers
                val messageBytes = message.body
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
                val responseBytes = responseJSON.toByteArray()
                val messageProperties = message.messageProperties
                val responseMessage = RabbitMQMessage(responseBytes, messageProperties)

                val correlationId = messageProperties.correlationId
                val correlationData = CorrelationData(correlationId);
                rabbitTemplate.sendAndReceive(
                        config.RPC_EXCHANGE,
                        QUEUE,
                        responseMessage,
                        correlationData
                )
        }
}