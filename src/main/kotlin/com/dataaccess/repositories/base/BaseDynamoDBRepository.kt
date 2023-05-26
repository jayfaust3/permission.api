package com.permission.api.dataaccess.repositories

import org.springframework.stereotype.Repository
import org.springframework.context.annotation.Bean
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.permission.api.configuration.AWSConfiguration

@Repository
open class BaseDynamoDBRepository  (
    protected val awsConfig: AWSConfiguration
) {
    @Bean
    protected fun client(): DynamoDB {
        return DynamoDB(AmazonDynamoDBClientBuilder
            .standard()
            .withCredentials(
                AWSStaticCredentialsProvider(
                    BasicAWSCredentials(
                        awsConfig.awsClientId,
                        awsConfig.awsClientSecret
                    )
                )
            )
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    awsConfig.dynamoEndpoint,
                    awsConfig.awsRegion
                )
            )
            .build()
        )
    }
}