package com.thehuginn.bulletproofs.composite.bytecode

abstract class ManageableInterceptor {

    protected var isIntercepting = false

    protected abstract fun stop()

    protected abstract fun start()

    fun stopIntercepting() {
        isIntercepting = false
        stop()
    }

    fun startIntercepting() {
        isIntercepting = true
        start()
    }

}