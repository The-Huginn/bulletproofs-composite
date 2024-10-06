package com.thehuginn.bulletproofs.composite.api.gadget

import com.weavechain.zk.bulletproofs.BulletProofGenerators
import com.weavechain.zk.bulletproofs.BulletProofs
import com.weavechain.zk.bulletproofs.GadgetParams
import com.weavechain.zk.bulletproofs.PedersenCommitment
import com.weavechain.zk.bulletproofs.Proof

class DefaultVerifyingGadget<T : GadgetParams>(
    private val params: T
) : VerifyingGadget {

    override fun verify(
        proof: Proof,
        pedersenCommitment: PedersenCommitment,
        bulletProofGenerators: BulletProofGenerators,
        bulletProofs: BulletProofs
    ) {
        GadgetRegistry.getGadgetType(params)?.let {
            bulletProofs.verify(
                it,
                params,
                proof,
                pedersenCommitment,
                bulletProofGenerators
            )
        }
    }

    override fun suggestedGenerators() = GadgetRegistry.getSuggestedGenerators(params)

}