package com.permissionapi.common.models.dataaccess.permission.dynamo

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import org.springframework.data.annotation.Id

@DynamoDBTable(tableName = "Permissions")
data class PermissionDynamoRecord (
    @get:DynamoDBHashKey(attributeName = "entity_id")
    val partitionKey: String,
    @get:DynamoDBRangeKey(attributeName = "scope")
    val sortKey: String
    ) {
    @Id
    private var id: UniqueId? = null
        get() {
            return UniqueId(partitionKey, sortKey)
        }
}

data class UniqueId(
    @DynamoDBHashKey
    val partitionKey: String,
    @DynamoDBRangeKey
    val sortKey: String
)
