package com.common.models.api.request.messaging.permission

import com.permission.api.common.enums.ActorType

data class PermissionMessagingReadRequest (
    val actorType: ActorType,
    val entityId: String
)
