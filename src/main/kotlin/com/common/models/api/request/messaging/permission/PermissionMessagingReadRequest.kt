package com.permission.api.common.models.api.request.messaging.permission

import kotlinx.serialization.Serializable

@Serializable
data class PermissionMessagingReadRequest (
    val actorType: Int,
    val entityId: String
)
