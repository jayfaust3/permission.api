package com.permissionapi.common.models.dataaccess.permission.dynamo

import com.permissionapi.common.models.application.permission.Scope

data class PermissionSortKeyBuilder (private val scope: Scope) {
    val sortKey: String = "${scope.resource}::${scope.action}";
}