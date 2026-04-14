package br.com.plusapps.plusfisio

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform