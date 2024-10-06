package com.thehuginn.bulletproofs.composite.api.gadget

import com.weavechain.zk.bulletproofs.BulletProofGenerators
import com.weavechain.zk.bulletproofs.BulletProofs
import com.weavechain.zk.bulletproofs.PedersenCommitment
import com.weavechain.zk.bulletproofs.Proof

interface VerifyingGadget : SuggestingGadget {

    fun verify(
        proof: Proof,
        pedersenCommitment: PedersenCommitment,
        bulletProofGenerators: BulletProofGenerators,
        bulletProofs: BulletProofs
    )

}