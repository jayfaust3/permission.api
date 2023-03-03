package com.permission.api.application.services.crud

import com.permission.api.dataaccess.repositories.IPermissionRepository
import com.permission.api.common.enums.ActorType
import com.permission.api.common.models.application.permission.Scope

class PermissionCRUDService(private val dynamoPermissionRepository: IPermissionRepository) : IPermissionCRUDService {
    override fun getEntityPermissions(actorType: ActorType, entityId: String): List<Scope> {
        return dynamoPermissionRepository.getEntityPermissions(actorType, entityId)
    }

    override fun createEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope> {
        return dynamoPermissionRepository.createEntityPermissions(actorType, entityId, scopes)
    }

    override fun updateEntityPermissions(actorType: ActorType, entityId: String, scopes: List<Scope>): List<Scope> {
        return dynamoPermissionRepository.updateEntityPermissions(actorType, entityId, scopes)
    }
}
