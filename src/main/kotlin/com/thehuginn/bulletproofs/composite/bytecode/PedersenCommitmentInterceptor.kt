package com.thehuginn.bulletproofs.composite.bytecode

import java.util.concurrent.Callable
import lombok.Getter
import net.bytebuddy.implementation.bind.annotation.RuntimeType
import net.bytebuddy.implementation.bind.annotation.SuperCall
import org.apache.logging.log4j.kotlin.logger

@Getter
object PedersenCommitmentInterceptor : ManageableInterceptor() {
    private var cachedPedersenCommitment: Any? = null
    private val logger = logger()

    @RuntimeType
    @JvmStatic
    fun intercept(@SuperCall zuper: Callable<*>): Any? {
        if (!isIntercepting) {
            return zuper.call()
        }

        logger.info("Intercepting PedersenCommitment#getDefault")
        synchronized(this) {
            if (cachedPedersenCommitment == null) {
                cachedPedersenCommitment = zuper.call()
            }
        }
        return cachedPedersenCommitment
    }

    override fun stop() {
        cachedPedersenCommitment = null
    }

    override fun start() {
    }
}
