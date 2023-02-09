package com.permissionapi.common.models.dataaccess.permission.dynamo

import com.permissionapi.common.enums.ActorType

data class PermissionPartitionKeyBuilder (private val actorType: ActorType, private val entityId: String) {
    val partitionKey: String = "$actorType::$entityId"
}