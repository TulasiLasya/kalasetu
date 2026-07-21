package com.example.kalasetu

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform