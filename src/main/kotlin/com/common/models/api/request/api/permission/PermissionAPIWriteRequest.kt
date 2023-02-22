package com.common.models.api.request.api.permission

import com.permission.api.common.models.application.permission.Scope

data class PermissionAPIWriteRequest (
    val entityId: String,
    val scopes: List<Scope> = mutableListOf()
)