package com.permission.api.dataaccess.repositories

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec
import org.springframework.beans.factory.annotation.Value
import com.permission.api.common.enums.ActorType
import com.permission.api.common.models.application.permission.Scope
import com.permission.api.common.models.dataaccess.permission.dynamo.PermissionDynamoDeleteRequest
import com.permission.api.common.models.dataaccess.permission.dynamo.PermissionDynamoWriteRequest
import com.permission.api.common.models.dataaccess.permission.dynamo.PermissionDynamoReadRequest

class DynamoPermissionRepository(
    @Value(value = "\${app.aws.region}")
    private val awsRegion: String,
    @Value(value = "\${app.aws.clientId}")
    private val awsClientId: String,
    @Value(value = "\${app.aws.clientSecret}")
    private val awsClientSecret: String,
    @Value(value = "\${app.aws.dynamo.endpoint}")
    private val dynamoEndpoint: String,
    @Value(value = "\${app.aws.dynamo.permissionTableName}")
    private val permissionTableName: String
) : BasePermissionRepository() {

    private val dynamoClient = DynamoDB(
        AmazonDynamoDBClientBuilder
            .standard()
            .withCredentials(
                AWSStaticCredentialsProvider(
                    BasicAWSCredentials(awsClientId, awsClientSecret)
                )
            )
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    dynamoEndpoint,
                    awsRegion
                )
            )
            .build()
    ).getTable(permissionTableName)

    override fun getEntityPermissions(actorType: ActorType, entityId: String): List<Scope> {
        val readRequest = PermissionDynamoReadRequest(actorType, entityId)

        val spec = QuerySpec().withHashKey(
            "entity_id",
            readRequest.partitionKey
        )

        val queryResult = dynamoClient.query(spec)

        return queryResult
            .map{ item -> item.get("scope")}
            .map{ scopeString ->
                val scopeParts = (scopeString as String).split(":")

                val resource = scopeParts[0]
                val action = scopeParts[1]

                Scope(resource, action)
            }
    }

    override fun createEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope> {
        val writeRequests = scopes.map{ scope -> PermissionDynamoWriteRequest(actorType, entityId, scope) }

        for (request in writeRequests) {
            val item = Item()
                .withPrimaryKey(
                    "entity_id",
                    request.partitionKey,
                    "scope",
                    request.sortKey
                )

            dynamoClient.putItem(item)
        }

        return scopes
    }

    override fun updateEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope> {
        val incomingScopes = scopes.map { scope -> "${scope.resource}:${scope.action}" }
        val existingScopes = getEntityPermissions(actorType, entityId).map { scope -> "${scope.resource}:${scope.action}" }

        val scopesToAdd = mutableListOf<Scope>()
        val scopesToDelete = mutableListOf<Scope>()

        for (scope in incomingScopes) {
            if (!existingScopes.contains(scope)) {
                val scopeParts = scope.split(":")

                val resource = scopeParts[0]
                val action = scopeParts[1]

                scopesToAdd.add(
                    Scope(resource, action)
                )
            }
        }

        for (scope in existingScopes) {
            if (!incomingScopes.contains(scope)) {
                val scopeParts = scope.split(":")

                val resource = scopeParts[0]
                val action = scopeParts[1]

                scopesToDelete.add(
                    Scope(resource, action)
                )
            }
        }

        deleteEntityPermissions(actorType, entityId, scopesToDelete)
        createEntityPermissions(actorType, entityId, scopesToAdd)

        return scopes
    }

    private fun deleteEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): Unit {
        val deleteRequests = scopes.map{ scope -> PermissionDynamoDeleteRequest(actorType, entityId, scope) }

        for (request in deleteRequests) {
            val spec = DeleteItemSpec()
                .withPrimaryKey(
                    "entity_id",
                    request.partitionKey,
                    "scope",
                    request.sortKey
                )

            dynamoClient.deleteItem(spec)
        }
    }
}