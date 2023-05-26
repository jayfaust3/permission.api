package com.permission.api.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value

@Configuration
class AWSConfiguration (
    @Value(value = "\${aws.region}")
    val awsRegion: String,
    @Value(value = "\${aws.clientId}")
    val awsClientId: String,
    @Value(value = "\${aws.clientSecret}")
    val awsClientSecret: String,
    @Value(value = "\${aws.dynamo.endpoint}")
    val dynamoEndpoint: String,
)