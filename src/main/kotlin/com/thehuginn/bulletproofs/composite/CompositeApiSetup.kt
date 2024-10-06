package com.thehuginn.bulletproofs.composite

import com.thehuginn.bulletproofs.composite.bytecode.PedersenCommitmentInterceptor
import com.thehuginn.bulletproofs.composite.bytecode.ProverFieldInterceptor
import com.thehuginn.bulletproofs.composite.bytecode.ProverInterceptor
import com.thehuginn.bulletproofs.composite.bytecode.VerifierFieldInterceptor
import com.thehuginn.bulletproofs.composite.bytecode.VerifierInterceptor
import java.lang.instrument.Instrumentation
import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.implementation.SuperMethodCall
import net.bytebuddy.matcher.ElementMatchers.any
import net.bytebuddy.matcher.ElementMatchers.named

class CompositeApiSetup {

    companion object {

        @JvmStatic
        fun premain(agentArgs: String?, inst: Instrumentation) {
            AgentBuilder.Default()
                .type(named("com.weavechain.zk.bulletproofs.Prover"))
                .transform { builder, _, _, _, _ ->
                    builder.method(any())
                        .intercept(MethodDelegation.to(ProverInterceptor::class.java))
                        .constructor(any())
                        .intercept(
                            SuperMethodCall.INSTANCE
                                .andThen(MethodDelegation.to(ProverFieldInterceptor::class.java))
                        )
                }
                .type(named("com.weavechain.zk.bulletproofs.Verifier"))
                .transform { builder, _, _, _, _ ->
                    builder
                        .method(any())
                        .intercept(MethodDelegation.to(VerifierInterceptor::class.java))
                        .constructor(any())
                        .intercept(
                            SuperMethodCall.INSTANCE
                                .andThen(MethodDelegation.to(VerifierFieldInterceptor::class.java))
                        )
                }
                .type(named("com.weavechain.zk.bulletproofs.PedersenCommitment"))
                .transform { builder, _, _, _, _ ->
                    builder.method(named("getDefault"))
                        .intercept(MethodDelegation.to(PedersenCommitmentInterceptor::class.java))
                }
                .installOn(inst)
        }
    }

}