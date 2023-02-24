package com.common.models.api.request.messaging.permission

import kotlinx.serialization.Serializable
import com.permission.api.common.enums.ActorType

@Serializable
data class PermissionMessagingReadRequest (
    val actorType: ActorType,
    val entityId: String
)
