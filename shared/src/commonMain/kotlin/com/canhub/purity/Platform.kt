package com.canhub.purity

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
