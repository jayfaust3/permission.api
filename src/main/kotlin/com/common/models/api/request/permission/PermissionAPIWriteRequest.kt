package com.permissionapi.common.models.api.request

import com.permissionapi.common.models.application.permission.Scope
import com.permissionapi.common.enums.ActorType

class PermissionAPIWriteRequest (
    val entityId: String,
    val actorType: ActorType,
    val scopes: List<Scope> = mutableListOf()
)