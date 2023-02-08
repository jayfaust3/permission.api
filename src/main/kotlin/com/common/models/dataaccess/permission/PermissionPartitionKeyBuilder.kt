package com.permissionapi.common.models.dataaccess

import com.permissionapi.common.enums.ActorType

data class PermissionPartitionKeyBuilder (private val actorType: ActorType, private val entityId: String) {
    val primaryKey: String = "$actorType::$entityId"
}