package com.thehuginn.bulletproofs.composite.api

import com.thehuginn.bulletproofs.composite.api.gadget.ProvingGadget
import com.thehuginn.bulletproofs.composite.api.gadget.VerifyingGadget
import com.weavechain.zk.bulletproofs.Proof

interface CompositeApi {

    fun createProof(vararg provingGadgets: ProvingGadget): Proof

    fun verifyProof(proof: Proof, vararg verifyingGadgets: VerifyingGadget): Boolean

}