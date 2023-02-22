package com.permission.api.common.models.dataaccess.permission.dynamo

import com.permission.api.common.enums.ActorType
import com.permission.api.common.models.application.permission.Scope

data class PermissionDynamoWriteRequest (
        private val actorType: ActorType,
        private val entityId: String,
        private val scope: Scope
) {
        val partitionKey: String = PermissionPartitionKeyBuilder(actorType, entityId).partitionKey
        val sortKey: String = PermissionSortKeyBuilder(scope).sortKey
}