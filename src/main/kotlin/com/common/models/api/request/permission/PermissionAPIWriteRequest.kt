package com.permissionapi.common.models.api

import com.permissionapi.common.models.application.permission
import com.permissionapi.common.enums

class PermissionAPIWriteRequest (
    val entityId: String,
    val actorType: ActorType,
    val scopes: List<Scope> = mutableListOf()
)