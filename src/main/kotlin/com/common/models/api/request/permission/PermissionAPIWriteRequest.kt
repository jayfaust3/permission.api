package com.permission.api.common.models.api.request

import com.permission.api.common.models.application.permission.Scope

class PermissionAPIWriteRequest (
    val entityId: String,
    val scopes: List<Scope> = mutableListOf()
)