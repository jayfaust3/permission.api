package com.permissionapi.common.models.api

import com.permissionapi.common.models.data.permission.Permission

class UserPermissionWriteModel (
    val userId: String,
    val permissions: List<Permission> = mutableListOf()
        )