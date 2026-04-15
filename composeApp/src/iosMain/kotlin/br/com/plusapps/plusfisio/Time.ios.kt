package br.com.plusapps.plusfisio

import kotlin.time.Clock

actual fun currentEpochMillis(): Long = Clock.System.now().toEpochMilliseconds()
