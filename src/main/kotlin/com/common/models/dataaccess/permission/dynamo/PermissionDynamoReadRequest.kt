package com.permission.api.common.models.dataaccess.permission.dynamo

import com.permission.api.common.enums.ActorType

data class PermissionDynamoReadRequest (
    private val actorType: ActorType,
    private val entityId: String
) {
    val partitionKey: String = PermissionPartitionKeyBuilder(actorType, entityId).partitionKey
}