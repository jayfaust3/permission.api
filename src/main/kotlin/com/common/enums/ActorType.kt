package com.permission.api.common.enums

enum class ActorType(val value: Int) {
    USER(0),
    SYSTEM(1);

    companion object {
        fun valueOf(value: Int) = ActorType.values().find { it.value == value }
    }
}