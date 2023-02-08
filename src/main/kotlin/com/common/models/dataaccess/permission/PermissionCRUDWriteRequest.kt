package com.permissionapi.common.models.dataaccess.permission

import com.permissionapi.common.enums.ActorType
import com.permissionapi.common.models.application.permission.Scope
import com.permissionapi.common.models.dataaccess.PermissionPartitionKeyBuilder;
import com.permissionapi.common.models.dataaccess.PermissionSortKeyBuilder;

class PermissionCRUDWriteRequest (
        val entityId: String,
        val actorType: ActorType,
        val scope: Scope
) {
        val partitionKey: String = PermissionPartitionKeyBuilder(actorType, entityId).partitionKey
        val sortKey: String = PermissionSortKeyBuilder(scope).sortKey
}