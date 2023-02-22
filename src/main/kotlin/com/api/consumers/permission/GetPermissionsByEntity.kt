package com.api.consumers.permission

import org.springframework.beans.factory.annotation.Value
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import com.permission.api.application.services.crud.IPermissionCRUDService
import com.common.models.api.request.messaging.base.Message
import com.common.models.api.request.messaging.permission.PermissionMessagingReadRequest

class GetPermissionsByEntity (
        @Value(value = "\${app.rabbit-mq.host}")
        private val rabbitMQHost: String,
        @Value(value = "\${app.rabbit-mq.port}")
        private val rabbitMQPort: String,
        @Value(value = "\${app.rabbit-mq.username}")
        private val rabbitMQUserName: String,
        @Value(value = "\${app.rabbit-mq.password}")
        private val rabbitMQPassword: String,
        private val crudService: IPermissionCRUDService
        ){
        val factory = ConnectionFactory()
        factory.host = rabbitMQHost
        val connection = factory.newConnection()
        val channel = connection.createChannel()

        channel.queueDeclare("", false, false, false, null)

        channel.basicQos(1)

        val consumer = object : DefaultConsumer(channel) {
                override fun handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: ByteArray) {
                        val replyProps = AMQP.BasicProperties.Builder()
                                .correlationId(properties.correlationId)
                                .build()
                        val message = String(body, charset("UTF-8"))
                        val n = Integer.parseInt(message)
                        println(" [.] fib($message)")
                        val response = RPCServer.fib(n).toString()
                        channel.basicPublish("", properties.replyTo, replyProps, response.toByteArray())
                        channel.basicAck(envelope.deliveryTag, false)
                }
        }

        channel.basicConsume(RPCServer.RPC_QUEUE_NAME, false, consumer)
        // Wait and be prepared to consume the message from RPC client.
        while (true) {
                synchronized(consumer) {
                        (consumer as Object).wait()
                }
        }

}