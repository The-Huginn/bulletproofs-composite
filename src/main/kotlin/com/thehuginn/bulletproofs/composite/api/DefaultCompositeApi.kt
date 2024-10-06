package com.thehuginn.bulletproofs.composite.api

import com.thehuginn.bulletproofs.composite.api.gadget.ProvingGadget
import com.thehuginn.bulletproofs.composite.api.gadget.SuggestingGadget
import com.thehuginn.bulletproofs.composite.api.gadget.VerifyingGadget
import com.thehuginn.bulletproofs.composite.bytecode.InterceptorController
import com.thehuginn.bulletproofs.composite.bytecode.ProverInterceptor
import com.thehuginn.bulletproofs.composite.bytecode.VerifierInterceptor
import com.weavechain.zk.bulletproofs.BulletProofGenerators
import com.weavechain.zk.bulletproofs.BulletProofs
import com.weavechain.zk.bulletproofs.PedersenCommitment
import com.weavechain.zk.bulletproofs.Proof
import com.weavechain.zk.bulletproofs.Prover
import com.weavechain.zk.bulletproofs.Transcript
import com.weavechain.zk.bulletproofs.Verifier
import com.weavechain.zk.bulletproofs.gadgets.Gadgets

class DefaultCompositeApi(
) : CompositeApi {

    companion object {
        private val bulletProofs: BulletProofs = BulletProofs()

        init {
            Gadgets.registerGadgets(bulletProofs)
        }
    }

    override fun createProof(vararg provingGadgets: ProvingGadget): Proof {
        InterceptorController.start()
        
        val pedersenCommitment = PedersenCommitment.getDefault()
        val bulletProofGenerators = BulletProofGenerators(suggestedGenerators(provingGadgets), 1)

        val proofs = provingGadgets.map {
            it.prove(
                pedersenCommitment,
                bulletProofGenerators,
                bulletProofs,
            )
        }

        ProverInterceptor.shouldNotIntercept()
        val transcript = Transcript()
        val prover = Prover(transcript, PedersenCommitment.getDefault())


        val proof = Proof(
            prover.prove(bulletProofGenerators),
            proofs.flatMap { it.commitments }
        )

        InterceptorController.stop()

        return proof
    }

    override fun verifyProof(proof: Proof, vararg verifyingGadgets: VerifyingGadget): Boolean {
        InterceptorController.start()

        val pedersenCommitment = PedersenCommitment.getDefault()
        val bulletProofGenerators = BulletProofGenerators(suggestedGenerators(verifyingGadgets), 1)
        val compositeProof = CompositeProof(Proof.deserialize(proof.serialize()))

        verifyingGadgets.forEach {
            it.verify(
                compositeProof,
                pedersenCommitment,
                bulletProofGenerators,
                bulletProofs,
            )
            compositeProof.removeRetrievedCommitments()
        }

        VerifierInterceptor.shouldNotIntercept()
        val verifier = Verifier(Transcript())
        val match = verifier.verify(
            compositeProof,
            pedersenCommitment,
            bulletProofGenerators
        )

        InterceptorController.stop()

        return match
    }

    private fun suggestedGenerators(suggestingGadgets: Array<out SuggestingGadget>) : Int {
        val suggested = suggestingGadgets.sumOf { it.suggestedGenerators() }
        return Integer.highestOneBit(suggested - 1) shl 1
    }

}