package com.api.consumers.permission

import org.springframework.beans.factory.annotation.Value
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import com.permission.api.application.services.crud.IPermissionCRUDService
import com.common.models.api.request.messaging.base.Message
import com.common.models.api.request.messaging.permission.PermissionMessagingReadRequest
import com.permission.api.common.models.application.permission.Scope

class GetPermissionsByEntity (
        @Value(value = "\${app.rabbit-mq.host}")
        private val rabbitMQHost: String,
        @Value(value = "\${app.rabbit-mq.virtualHost}")
        private val rabbitMQVirtualHost: String,
        @Value(value = "\${app.rabbit-mq.port}")
        private val rabbitMQPort: String,
        @Value(value = "\${app.rabbit-mq.username}")
        private val rabbitMQUserName: String,
        @Value(value = "\${app.rabbit-mq.password}")
        private val rabbitMQPassword: String,
        private val crudService: IPermissionCRUDService
        ){

        private val getPermissionsByEntityQueueName = "rpc/permissions"

        init {
                val factory = ConnectionFactory()
                factory.host = rabbitMQHost
                factory.virtualHost = rabbitMQVirtualHost
                factory.port = Integer.parseInt(rabbitMQPort)
                factory.username = rabbitMQUserName
                factory.password = rabbitMQPassword

                val connection = factory.newConnection()
                val channel = connection.createChannel()
                channel.queueDeclare(getPermissionsByEntityQueueName, false, false, false, null)
                channel.basicQos(1)

                val consumer = object : DefaultConsumer(channel) {
                        override fun handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: ByteArray) {
                                val replyProps = AMQP.BasicProperties.Builder()
                                        .correlationId(properties.correlationId)
                                        .build()
                                val messageJSON = String(body, charset("UTF-8"))
                                val message = Json.decodeFromString<Message<PermissionMessagingReadRequest>>(messageJSON)
                                val request = message.data
                                val permissions = crudService.getEntityPermissions(
                                        request.actorType,
                                        request.entityId
                                )
                                val responseJSON = Json.encodeToString(
                                        Message(
                                                permissions
                                        )
                                )
                                channel.basicPublish(getPermissionsByEntityQueueName, properties.replyTo, replyProps, responseJSON.toByteArray())
                                channel.basicAck(envelope.deliveryTag, false)
                        }
                }

                channel.basicConsume(getPermissionsByEntityQueueName, false, consumer)
                // Wait and be prepared to consume the message from RPC client.
                while (true) {
                        synchronized(consumer) {
                                (consumer as Object).wait()
                        }
                }
        }
}