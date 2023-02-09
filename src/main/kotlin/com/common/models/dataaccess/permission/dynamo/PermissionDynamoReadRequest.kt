package com.permissionapi.common.models.dataaccess.permission.dynamo

import com.permissionapi.common.enums.ActorType
import com.permissionapi.common.models.dataaccess.permission.dynamo.PermissionPartitionKeyBuilder

class PermissionDynamoReadRequest (
    private val actorType: ActorType,
    private val entityId: String
) {
    val partitionKey: String = PermissionPartitionKeyBuilder(actorType, entityId).partitionKey
}