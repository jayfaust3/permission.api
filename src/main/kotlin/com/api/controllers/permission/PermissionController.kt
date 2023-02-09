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
@RequestMapping("/api/permissions")
class PermissionController(
    private val crudService: IPermissionCRUDService
    ) {
    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: String): ResponseEntity<ResponseModel<List<Scope>>> {
        val scopes = crudService.getEntityPermissions(ActorType.USER, id)

        return ResponseEntity.ok(ResponseModel<List<Scope>>(scopes))
    }

    @PostMapping
    fun create(@RequestBody request: PermissionAPIWriteRequest): ResponseEntity<ResponseModel<List<Scope>>> {
        val scopes = crudService.createEntityPermissions(request.actorType, request.entityId, request.scopes)

        return ResponseEntity(ResponseModel<List<Scope>>(scopes), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun update(@RequestBody request: PermissionAPIWriteRequest, @PathVariable("id") id: String): ResponseEntity<ResponseModel<List<Scope>>> {
        val scopes = crudService.updateEntityPermissions(request.actorType, request.entityId, request.scopes)

        return ResponseEntity.ok(ResponseModel<List<Scope>>(scopes))
    }
}
