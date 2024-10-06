package com.thehuginn.bulletproofs.composite.api

import com.weavechain.ec.ECPoint
import com.weavechain.zk.bulletproofs.Proof

class CompositeProof(
    proof: Proof
) : Proof(proof.proof, proof.commitments) {
    private val retrievedCommitments: MutableList<Int> = mutableListOf()

    override fun getCommitment(i: Int): ECPoint {
        retrievedCommitments.add(i)
        return super.getCommitment(i)
    }

    fun removeRetrievedCommitments() {
        retrievedCommitments.sortDescending()
        retrievedCommitments.forEach { index ->
            (commitments as MutableList<ECPoint>).removeAt(index)
        }
        retrievedCommitments.clear()
    }
}