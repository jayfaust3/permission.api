package com.permissionapi.api.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.permissionapi.application.services.crud.IPermissionCRUDService
import com.permissionapi.common.models.api.request.PermissionAPIWriteRequest
import com.permissionapi.common.models.api.response.ResponseModel
import com.permissionapi.common.enums.ActorType
import com.permissionapi.common.models.application.permission.Scope

@RestController
@RequestMapping("/api/permissions/system")
class SystemPermissionController(
    private val crudService: IPermissionCRUDService
) {
    private val actorType = ActorType.SYSTEM

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: String): ResponseEntity<ResponseModel<List<Scope>>> {
        val scopes = crudService.getEntityPermissions(actorType, id)

        return ResponseEntity.ok(ResponseModel(scopes))
    }

    @PostMapping
    fun create(@RequestBody request: PermissionAPIWriteRequest): ResponseEntity<ResponseModel<List<Scope>>> {
        val scopes = crudService.createEntityPermissions(actorType, request.entityId, request.scopes)

        return ResponseEntity(ResponseModel(scopes), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun update(@RequestBody request: PermissionAPIWriteRequest, @PathVariable("id") id: String): ResponseEntity<ResponseModel<List<Scope>>> {
        val scopes = crudService.updateEntityPermissions(actorType, request.entityId, request.scopes)

        return ResponseEntity.ok(ResponseModel(scopes))
    }
}
