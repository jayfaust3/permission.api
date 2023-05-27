package com.permission.api.common.models.application.permission

import kotlinx.serialization.Serializable

@Serializable
data class Scope (
        val resource: String,
        val action: String
)