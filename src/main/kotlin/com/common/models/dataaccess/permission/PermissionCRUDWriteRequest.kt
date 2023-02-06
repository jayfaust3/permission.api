package com.permissionapi.common.models.dataaccess.permission

import com.permissionapi.common.enums

class PermissionCRUDWriteRequest (
        val entityId: String,
        val actorType: ActorType,
        val permission: String
)