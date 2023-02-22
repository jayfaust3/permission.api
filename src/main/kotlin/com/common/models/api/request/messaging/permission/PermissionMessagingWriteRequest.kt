package com.common.models.api.request.messaging.permission

import com.permission.api.common.enums.ActorType
import com.permission.api.common.models.application.permission.Scope

data class PermissionMessagingWriteRequest (
    val actorType: ActorType,
    val entityId: String,
    val scopes: List<Scope>
)
