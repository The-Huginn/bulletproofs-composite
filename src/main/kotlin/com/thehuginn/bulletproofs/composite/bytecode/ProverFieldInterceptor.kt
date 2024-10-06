package com.thehuginn.bulletproofs.composite.bytecode

import java.lang.reflect.Modifier.isFinal
import java.lang.reflect.Modifier.isStatic
import java.util.concurrent.ConcurrentHashMap
import net.bytebuddy.implementation.bind.annotation.RuntimeType
import net.bytebuddy.implementation.bind.annotation.This
import org.apache.logging.log4j.kotlin.logger

object ProverFieldInterceptor : ManageableInterceptor() {
    private val staticFields = ConcurrentHashMap<String, Any?>()
    private val logger = logger()

    @RuntimeType
    @JvmStatic
    fun intercept(
        @This self: Any?
    ) {
        if (!isIntercepting) {
            return
        }

        logger.info("Intercepting constructor of Prover()")
        self?.javaClass?.declaredFields?.forEach {
            it?.apply {
                if (isStatic(modifiers) && isFinal(modifiers)) {
                    return@forEach
                }

                isAccessible = true
                val fieldName = it.name

                staticFields.computeIfAbsent(fieldName) {
                    get(self)
                }

                set(self, staticFields[fieldName])
            }
        }
    }

    override fun stop() {
        staticFields.clear()
    }

    override fun start() {
    }
}