package com.permissionapi.dataaccess.repositories.permission

import com.permissionapi.common.enums.ActorType
import com.permissionapi.common.models.application.permission.Scope

abstract class BasePermissionRepository : IPermissionRepository {
    abstract override fun getEntityPermissions(actorType: ActorType, entityId: String): List<Scope>
    abstract override fun createEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope>
    abstract override fun updateEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope>
}