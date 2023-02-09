package com.permissionapi.application.services.crud

import com.permissionapi.common.enums.ActorType
import com.permissionapi.common.models.application.permission.Scope

interface IPermissionCRUDService {
    fun getEntityPermissions(actorType: ActorType, entityId: String): List<Scope>
    fun createEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope>
    fun updateEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope>
}
