package com.thehuginn.bulletproofs.composite.bytecode

import com.weavechain.zk.bulletproofs.BulletProofs
import java.util.concurrent.Callable
import net.bytebuddy.implementation.bind.annotation.RuntimeType
import net.bytebuddy.implementation.bind.annotation.SuperCall
import org.apache.logging.log4j.kotlin.logger

/**
 * Used for testing & debugging, to keep security measures
 * at place we have to keep randomization at place
 */
object UtilsInterceptor : ManageableInterceptor() {
    private val logger = logger()

    @RuntimeType
    @JvmStatic
    fun intercept(@SuperCall zuper: Callable<*>): Any? {
        if (!isIntercepting) {
            return zuper.call()
        }

        logger.info("Intercepting Utils#randomScalar")
        val random = ByteArray(32) { 0xAB.toByte() }
        return BulletProofs.getFactory().fromBits(random)
    }

    override fun stop() {
    }

    override fun start() {
    }

}