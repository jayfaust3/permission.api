package com.permissionapi.dataaccess.repositories.permission

import com.permissionapi.common.enums.ActorType
import com.permissionapi.common.models.application.permission.Scope
import com.permissionapi.common.models.dataaccess.permission.dynamo.PermissionDynamoWriteRequest
import com.permissionapi.common.models.dataaccess.permission.dynamo.PermissionDynamoReadRequest


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