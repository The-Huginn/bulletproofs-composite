package com.thehuginn.bulletproofs.composite

import com.thehuginn.bulletproofs.composite.api.DefaultCompositeApi
import com.thehuginn.bulletproofs.composite.api.gadget.DefaultProvingGadget
import com.thehuginn.bulletproofs.composite.api.gadget.DefaultVerifyingGadget
import com.weavechain.zk.bulletproofs.gadgets.MiMC
import com.weavechain.zk.bulletproofs.gadgets.MiMCStringHashPreImageParams
import com.weavechain.zk.bulletproofs.gadgets.NumberInListParams
import com.weavechain.zk.bulletproofs.gadgets.NumberInRangeParams
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

}