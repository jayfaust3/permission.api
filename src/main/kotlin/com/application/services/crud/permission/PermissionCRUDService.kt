package com.permissionapi.application.services.crud

import com.permissionapi.dataaccess.repositories.permission.IPermissionRepository
import com.permissionapi.common.enums.ActorType
import com.permissionapi.common.models.application.permission.Scope

class PermissionCRUDService(private val repository: IPermissionRepository) : IPermissionCRUDService {
    override fun getEntityPermissions(actorType: ActorType, entityId: String): List<Scope> {
        return repository.getEntityPermissions(actorType, entityId)
    }

    override fun createEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope> {
        return repository.createEntityPermissions(actorType, entityId, scopes)
    }

    override fun updateEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope> {
        return repository.updateEntityPermissions(actorType, entityId, scopes)
    }
}
