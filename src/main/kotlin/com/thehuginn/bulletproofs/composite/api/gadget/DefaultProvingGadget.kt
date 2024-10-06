package com.thehuginn.bulletproofs.composite.api.gadget

import com.weavechain.zk.bulletproofs.BulletProofGenerators
import com.weavechain.zk.bulletproofs.BulletProofs
import com.weavechain.zk.bulletproofs.GadgetParams
import com.weavechain.zk.bulletproofs.PedersenCommitment
import com.weavechain.zk.bulletproofs.Proof
import com.weavechain.zk.bulletproofs.Utils

class DefaultProvingGadget<T : GadgetParams>(
    private val params: T,
    private val value: Any,
) : ProvingGadget {

    override fun prove(
        pedersenCommitment: PedersenCommitment,
        bulletProofGenerators: BulletProofGenerators,
        bulletProofs: BulletProofs
    ): Proof {
        return GadgetRegistry.getGadgetType(params)?.let {
            bulletProofs.generate(
                it,
                value,
                params,
                Utils.randomScalar(),
                pedersenCommitment,
                bulletProofGenerators
            )
        } ?: throw IllegalArgumentException("Gadget implementation not found for the given params: ${params.javaClass}")
    }

    override fun suggestedGenerators() = GadgetRegistry.getSuggestedGenerators(params)

}