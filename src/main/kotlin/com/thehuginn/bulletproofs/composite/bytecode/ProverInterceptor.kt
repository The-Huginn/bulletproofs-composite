package com.thehuginn.bulletproofs.composite.bytecode

import java.lang.reflect.Method
import java.util.concurrent.Callable
import java.util.concurrent.atomic.AtomicBoolean
import net.bytebuddy.implementation.bind.annotation.Origin
import net.bytebuddy.implementation.bind.annotation.RuntimeType
import net.bytebuddy.implementation.bind.annotation.SuperCall
import net.bytebuddy.implementation.bind.annotation.This
import org.apache.logging.log4j.kotlin.logger

object ProverInterceptor : ManageableInterceptor() {
    private val shouldIntercept: ThreadLocal<AtomicBoolean> = ThreadLocal.withInitial { AtomicBoolean(true) }
    private val transcriptionWriterInterceptor = TranscriptionWriterInterceptor()
    private val logger = logger()

    @RuntimeType
    @JvmStatic
    fun intercept(
        @SuperCall zuper: Callable<*>,
        @Origin method: Method,
        @This instance: Any
    ): Any? {
        if (!isIntercepting) {
            return zuper.call()
        }

        logger.info("Intercepting ${instance.javaClass.name}#${method.name}")

        return when (method.name) {
            "prove" -> {
                if (shouldIntercept.get().get()) {
                    null
                } else {
                    transcriptionWriterInterceptor.mergeTranscriptsAndSet(instance)
                    zuper.call()
                }
            }

            "commit" -> {
                if (shouldIntercept.get().get()) {
                    transcriptionWriterInterceptor.switchToCommitments(instance)
                }
                zuper.call()
            }

            else -> {
                if (shouldIntercept.get().get()) {
                    transcriptionWriterInterceptor.switchToProofs(instance)
                }
                zuper.call()
            }
        }
    }

    fun shouldNotIntercept() {
        shouldIntercept.get().set(false)
    }

    override fun stop() {
        // clearance of transcript is done byt it's interceptor
    }

    override fun start() {
        shouldIntercept.get().set(true)
    }

}