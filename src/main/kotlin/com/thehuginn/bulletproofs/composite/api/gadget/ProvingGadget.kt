package com.thehuginn.bulletproofs.composite.api.gadget

import com.weavechain.zk.bulletproofs.BulletProofGenerators
import com.weavechain.zk.bulletproofs.BulletProofs
import com.weavechain.zk.bulletproofs.PedersenCommitment
import com.weavechain.zk.bulletproofs.Proof

interface ProvingGadget : SuggestingGadget {

    fun prove(
        pedersenCommitment: PedersenCommitment,
        bulletProofGenerators: BulletProofGenerators,
        bulletProofs: BulletProofs
    ): Proof

}