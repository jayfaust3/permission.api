package com.permission.api.configuration

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfiguration
//    (
//    @Value(value = "\${spring.rabbitmq.host}")
//    val host: String,
//    @Value(value = "\${spring.rabbitmq.virtualhost}")
//    val virtualHost: String,
//    @Value(value = "\${spring.rabbitmq.port}")
//    val port: Int,
//    @Value(value = "\${spring.rabbitmq.username}")
//    val username: String,
//    @Value(value = "\${spring.rabbitmq.password}")
//    val password: String,
//    @Value(value = "\${spring.rabbitmq.publisher-confirm-type}")
//    val publisherConfirmType: String,
//    @Value(value = "\${spring.rabbitmq.publisher-returns}")
//    val publisherReturns: Boolean
//        )
    (val cachingConnectionFactory: CachingConnectionFactory)
{
    final val RPC_EXCHANGE = "exchange:rpc"

    final val GET_PERMISSIONS_BY_ENTITY_ROUTING_KEY = "get-permissions-by-entity"

    final val GET_PERMISSIONS_BY_ENTITY_QUEUE = "queue:get-permissions-by-entity"

//    @Bean
//    fun rpcExchange(): DirectExchange {
//        return DirectExchange(RPC_EXCHANGE)
//    }
//
//    @Bean
//    fun getPermissionsByEntityQueue(): Queue {
//        return Queue(GET_PERMISSIONS_BY_ENTITY_QUEUE)
//    }
//
//    @Bean
//    fun getPermissionsByEntityBinding(): Binding {
//        return BindingBuilder.bind(getPermissionsByEntityQueue()).to(rpcExchange()).with(GET_PERMISSIONS_BY_ENTITY_ROUTING_KEY)
//    }
    @Bean
    fun createGetPermissionsByEntitySchema(): Declarables {
        return Declarables(
            FanoutExchange(RPC_EXCHANGE),
            Queue(GET_PERMISSIONS_BY_ENTITY_QUEUE),
            Binding(
                GET_PERMISSIONS_BY_ENTITY_QUEUE,
                Binding.DestinationType.QUEUE,
                RPC_EXCHANGE,
                GET_PERMISSIONS_BY_ENTITY_ROUTING_KEY,
                null
            )
        )
    }

//    @Bean
//    fun cachingConnectionFactory(): CachingConnectionFactory {
//        return CachingConnectionFactory()
//    }

    @Bean
    fun converter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun rabbitTemplate(cachingConnectionFactory: CachingConnectionFactory, converter: Jackson2JsonMessageConverter): RabbitTemplate {
        val template = RabbitTemplate(cachingConnectionFactory)
        template.messageConverter = converter
        return template
    }
}
