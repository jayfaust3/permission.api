package com.permissionapi.common.models.dataaccess.permission.dynamo

import com.permissionapi.common.enums.ActorType
import com.permissionapi.common.models.application.permission.Scope

class PermissionDynamoWriteRequest (
        private val actorType: ActorType,
        private val entityId: String,
        private val scope: Scope
) {
        val partitionKey: String = PermissionPartitionKeyBuilder(actorType, entityId).partitionKey
        val sortKey: String = PermissionSortKeyBuilder(scope).sortKey
}