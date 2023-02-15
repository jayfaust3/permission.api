package com.permission.api.dataaccess.repositories.permission

import com.permission.api.common.enums.ActorType
import com.permission.api.common.models.application.permission.Scope

interface IPermissionRepository {
    fun getEntityPermissions(actorType: ActorType, entityId: String): List<Scope>
    fun createEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope>
    fun updateEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope>
}