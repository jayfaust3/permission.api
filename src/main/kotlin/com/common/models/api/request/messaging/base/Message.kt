package com.common.models.api.request.messaging.base

import kotlinx.serialization.Serializable

@Serializable
data class Message<TData>(val data: TData) {

}

