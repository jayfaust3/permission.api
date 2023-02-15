package com.permission.api.dataaccess.repositories.permission

import com.permission.api.common.enums.ActorType
import com.permission.api.common.models.application.permission.Scope
import com.permission.api.common.models.dataaccess.permission.dynamo.PermissionDynamoWriteRequest
import com.permission.api.common.models.dataaccess.permission.dynamo.PermissionDynamoReadRequest


class DynamoPermissionRepository() : BasePermissionRepository() {
    override fun getEntityPermissions(actorType: ActorType, entityId: String): List<Scope> {
        val readRequest = PermissionDynamoReadRequest(actorType, entityId)
        return listOf<Scope>()
    }

    override fun createEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope> {
        for (scope in scopes) {
            val writeRequest = PermissionDynamoWriteRequest(actorType, entityId, scope)
        }

        return scopes
    }

    override fun updateEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope> {
        for (scope in scopes) {
            val writeRequest = PermissionDynamoWriteRequest(actorType, entityId, scope)
        }

        return scopes
    }
}