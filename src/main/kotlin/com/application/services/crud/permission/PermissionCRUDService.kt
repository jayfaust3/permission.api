package com.permission.api.application.services.crud

import org.springframework.stereotype.Service
import com.permission.api.dataaccess.repositories.IPermissionRepository
import com.permission.api.common.enums.ActorType
import com.permission.api.common.models.application.permission.Scope

@Service
class PermissionCRUDService(private val permissionRepository: IPermissionRepository) : IPermissionCRUDService {
    override fun getEntityPermissions(actorType: ActorType, entityId: String): List<Scope> {
        return permissionRepository.getEntityPermissions(actorType, entityId)
    }

    override fun createEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope> {
        return permissionRepository.createEntityPermissions(actorType, entityId, scopes)
    }

    override fun updateEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope> {
        return permissionRepository.updateEntityPermissions(actorType, entityId, scopes)
    }
}
