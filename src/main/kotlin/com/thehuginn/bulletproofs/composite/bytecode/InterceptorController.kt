package com.thehuginn.bulletproofs.composite.bytecode

object InterceptorController {

    private val interceptors = listOf(
        PedersenCommitmentInterceptor,
        ProverInterceptor,
        ProverFieldInterceptor,
        UtilsInterceptor,
        VerifierInterceptor,
        VerifierFieldInterceptor
    )

    fun start() {
        interceptors.forEach { it.startIntercepting() }
    }

    fun stop() {
        interceptors.forEach { it.stopIntercepting() }
    }
}