package com.permission.api.dataaccess.repositories.permission

import com.permission.api.common.enums.ActorType
import com.permission.api.common.models.application.permission.Scope

abstract class BasePermissionRepository : IPermissionRepository {
    abstract override fun getEntityPermissions(actorType: ActorType, entityId: String): List<Scope>
    abstract override fun createEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope>
    abstract override fun updateEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope>
}