package com.permission.api.api.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.permission.api.application.services.crud.IPermissionCRUDService
import com.common.models.api.request.api.permission.PermissionAPIWriteRequest
import com.permission.api.common.models.api.response.ResponseModel
import com.permission.api.common.enums.ActorType
import com.permission.api.common.models.application.permission.Scope

@RestController
@RequestMapping("/api/permissions/user")
class UserPermissionController(
    private val crudService: IPermissionCRUDService
    ) {

    private val actorType = ActorType.USER
    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: String): ResponseEntity<ResponseModel<List<Scope>>> {
        val scopes = crudService.getEntityPermissions(actorType, id)

        return ResponseEntity.ok(ResponseModel<List<Scope>>(scopes))
    }

    @PostMapping
    fun create(@RequestBody request: PermissionAPIWriteRequest): ResponseEntity<ResponseModel<List<Scope>>> {
        val scopes = crudService.createEntityPermissions(actorType, request.entityId, request.scopes)

        return ResponseEntity(ResponseModel<List<Scope>>(scopes), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun update(@RequestBody request: PermissionAPIWriteRequest, @PathVariable("id") id: String): ResponseEntity<ResponseModel<List<Scope>>> {
        val scopes = crudService.updateEntityPermissions(actorType, request.entityId, request.scopes)

        return ResponseEntity.ok(ResponseModel<List<Scope>>(scopes))
    }
}
