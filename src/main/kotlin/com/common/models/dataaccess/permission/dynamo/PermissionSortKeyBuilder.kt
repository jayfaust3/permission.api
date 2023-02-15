package com.permission.api.common.models.dataaccess.permission.dynamo

import com.permission.api.common.models.application.permission.Scope

data class PermissionSortKeyBuilder (private val scope: Scope) {
    val sortKey: String = "${scope.resource}::${scope.action}";
}
