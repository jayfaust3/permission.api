package com.common.models.dataaccess

import com.permissionapi.common.enums.ActorType

data class PermissionPrimaryKeyBuilder (private val actorType: ActorType, private val entityId: String) {
    val primaryKey: String = "$actorType::$entityId"
}