package com.thehuginn.bulletproofs.composite

import com.thehuginn.bulletproofs.composite.api.DefaultCompositeApi
import com.thehuginn.bulletproofs.composite.api.gadget.DefaultProvingGadget
import com.thehuginn.bulletproofs.composite.api.gadget.DefaultVerifyingGadget
import com.weavechain.zk.bulletproofs.gadgets.MiMC
import com.weavechain.zk.bulletproofs.gadgets.MiMCStringHashPreImageParams
import com.weavechain.zk.bulletproofs.gadgets.NumberInListParams
import com.weavechain.zk.bulletproofs.gadgets.NumberInRangeParams
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ByteBuddyExtension::class)
class CompositeApiTest {

    private val compositeApi = DefaultCompositeApi()

    @Test
    fun whenSingleProofGenerated_ShouldCorrectlyVerify() {
        val compositeProof = compositeApi.createProof(
            DefaultProvingGadget(
                NumberInRangeParams(10, 100, 31),
                16
            )
        )

        val match = compositeApi.verifyProof(
            compositeProof,
            DefaultVerifyingGadget(NumberInRangeParams(10, 100, 31))
        )

        assert(match)
    }

    @Test
    fun whenInvalidProofGenerated_ShouldVerifyAsFailed() {
        val compositeProof = compositeApi.createProof(
            DefaultProvingGadget(
                NumberInRangeParams(10, 100, 31),
                1
            )
        )

        val match = compositeApi.verifyProof(
            compositeProof,
            DefaultVerifyingGadget(NumberInRangeParams(10, 100, 31))
        )

        assertFalse(match)
    }

    @Test
    fun whenMultipleProofsGenerated_ShouldCorrectlyVerify() {
        val compositeProof = compositeApi.createProof(
            DefaultProvingGadget(
                NumberInRangeParams(10, 100, 31),
                16
            ),
            DefaultProvingGadget(
                NumberInListParams(listOf(10, 20, 30, 40, 50), 31),
                20
            )
        )

        val match = compositeApi.verifyProof(
            compositeProof,
            DefaultVerifyingGadget(NumberInRangeParams(10, 100, 31)),
            DefaultVerifyingGadget(NumberInListParams(listOf(10, 20, 30, 40, 50), 31))
        )

        assert(match)
    }

    @Test
    fun whenMultipleProofsGeneratedWithString_ShouldCorrectlyVerify() {
        // AI generated :)
        val poem = """
    Whose woods these are I think I know.
    His house is in the village though;
    He will not see me stopping here
    To watch his woods fill up with snow.

    My little horse must think it queer
    To stop without a farmhouse near
    Between the woods and frozen lake
    The darkest evening of the year.

    He gives his harness bells a shake
    To ask if there is some mistake.
    The only other sound's the sweep
    Of easy wind and downy flake.

    The woods are lovely, dark and deep,
    But I have promises to keep,
    And miles to go before I sleep,
    And miles to go before I sleep.
""".trimIndent()

        val compositeProof = compositeApi.createProof(
            DefaultProvingGadget(
                NumberInRangeParams(10, 100, 31),
                16
            ),
            DefaultProvingGadget(
                MiMCStringHashPreImageParams.from(poem, 0, MiMC.DEFAULT_MIMC_ROUNDS),
                poem
            ),
            DefaultProvingGadget(
                NumberInListParams(listOf(10, 20, 30, 40, 50), 31),
                20
            )
        )

        val match = compositeApi.verifyProof(
            compositeProof,
            DefaultVerifyingGadget(NumberInRangeParams(10, 100, 31)),
            DefaultVerifyingGadget(MiMCStringHashPreImageParams.from(poem, 0, MiMC.DEFAULT_MIMC_ROUNDS)),
            DefaultVerifyingGadget(NumberInListParams(listOf(10, 20, 30, 40, 50), 31))
        )

        assert(match)

    }

    @Test
    fun `compare size of composite proof vs individual proofs`() {

        val poem = """
    Hope is the thing with feathers
    That perches in the soul,
    And sings the tune without the words,
    And never stops at all,

    And sweetest in the gale is heard;
    And sore must be the storm
    That could abash the little bird
    That kept so many warm.

    I've heard it in the chillest land,
    And on the strangest sea;
    Yet, never, in extremity,
    It asked a crumb of me.
""".trimIndent()

        val compositeProof = compositeApi.createProof(
            DefaultProvingGadget(
                NumberInRangeParams(10, 100, 31),
                16
            ),
            DefaultProvingGadget(
                MiMCStringHashPreImageParams.from(poem, 0, MiMC.DEFAULT_MIMC_ROUNDS),
                poem
            ),
            DefaultProvingGadget(
                NumberInListParams(listOf(10, 20, 30, 40, 50), 31),
                20
            )
        )

        val compositeProofSize = compositeProof.serialize().size

        // Generate individual proofs
        val proof1 = compositeApi.createProof(
            DefaultProvingGadget(
                NumberInRangeParams(10, 100, 31),
                16
            )
        ).serialize().size

        val proof2 = compositeApi.createProof(
            DefaultProvingGadget(
                MiMCStringHashPreImageParams.from(poem, 0, MiMC.DEFAULT_MIMC_ROUNDS),
                poem
            )
        ).serialize().size

        val proof3 = compositeApi.createProof(
            DefaultProvingGadget(
                NumberInListParams(listOf(10, 20, 30, 40, 50), 31),
                20
            )
        ).serialize().size

        val totalIndividualProofsSize = proof1 + proof2 + proof3

        // Print sizes to the test log
        println("Composite proof size: $compositeProofSize bytes")
        println("Total individual proofs size: $totalIndividualProofsSize bytes")

        assert(compositeProofSize < totalIndividualProofsSize)
        assertEquals(1891, compositeProofSize)
        assertEquals(3689, totalIndividualProofsSize)
    }

}