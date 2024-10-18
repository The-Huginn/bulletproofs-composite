package com.thehuginn.bulletproofs.composite.api.gadget

import com.weavechain.zk.bulletproofs.GadgetParams
import com.weavechain.zk.bulletproofs.GadgetType
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.mimc_hash_preimage
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.mimc_string_hash_preimage
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.number_in_list
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.number_in_range
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.number_is_equal
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.number_is_greater_or_equal
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.number_is_less_or_equal
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.number_is_non_zero
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.number_is_not_equal
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.number_is_positive
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.number_is_zero
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.number_not_in_list
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.numbers_are_non_zero
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.numbers_are_positive
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.numbers_in_range
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.numbers_sum_to
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.records_add_update_proof
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.records_with_hash_preimage_sum_to
import com.weavechain.zk.bulletproofs.gadgets.Gadgets.records_with_hashes_sum_to
import com.weavechain.zk.bulletproofs.gadgets.MiMCHashPreImageParams
import com.weavechain.zk.bulletproofs.gadgets.MiMCStringHashPreImageParams
import com.weavechain.zk.bulletproofs.gadgets.NumberInListParams
import com.weavechain.zk.bulletproofs.gadgets.NumberInRangeParams
import com.weavechain.zk.bulletproofs.gadgets.NumberIsEqualParams
import com.weavechain.zk.bulletproofs.gadgets.NumberIsGreaterOrEqualParams
import com.weavechain.zk.bulletproofs.gadgets.NumberIsLessOrEqualParams
import com.weavechain.zk.bulletproofs.gadgets.NumberIsNonZeroParams
import com.weavechain.zk.bulletproofs.gadgets.NumberIsNotEqualParams
import com.weavechain.zk.bulletproofs.gadgets.NumberIsPositiveParams
import com.weavechain.zk.bulletproofs.gadgets.NumberIsZeroParams
import com.weavechain.zk.bulletproofs.gadgets.NumberNotInListParams
import com.weavechain.zk.bulletproofs.gadgets.NumbersAreNonZeroParams
import com.weavechain.zk.bulletproofs.gadgets.NumbersArePositiveParams
import com.weavechain.zk.bulletproofs.gadgets.NumbersInRangeParams
import com.weavechain.zk.bulletproofs.gadgets.NumbersSumToParams
import com.weavechain.zk.bulletproofs.gadgets.RecordsAddPreImageHashParams
import com.weavechain.zk.bulletproofs.gadgets.RecordsWithHashPreImageSumToParams
import com.weavechain.zk.bulletproofs.gadgets.RecordsWithHashesSumToParams

object GadgetRegistry {
    private val registry = mutableMapOf<Class<out GadgetParams>, GadgetType>()
    private val suggestedGenerators = mutableMapOf<Class<out GadgetParams>, Int>()

    init {
        registry[NumberInRangeParams::class.java] = number_in_range
        registry[NumbersInRangeParams::class.java] = numbers_in_range
        registry[NumberInListParams::class.java] = number_in_list
        registry[NumberIsPositiveParams::class.java] = number_is_positive
        registry[NumbersArePositiveParams::class.java] = numbers_are_positive
        registry[NumbersSumToParams::class.java] = numbers_sum_to
        registry[RecordsWithHashesSumToParams::class.java] = records_with_hashes_sum_to
        registry[RecordsWithHashPreImageSumToParams::class.java] = records_with_hash_preimage_sum_to
        registry[RecordsAddPreImageHashParams::class.java] = records_add_update_proof
        registry[NumberIsGreaterOrEqualParams::class.java] = number_is_greater_or_equal
        registry[NumberIsLessOrEqualParams::class.java] = number_is_less_or_equal
        registry[NumberIsNotEqualParams::class.java] = number_is_not_equal
        registry[NumberIsNonZeroParams::class.java] = number_is_non_zero
        registry[NumbersAreNonZeroParams::class.java] = numbers_are_non_zero
        registry[NumberIsZeroParams::class.java] = number_is_zero
        registry[NumberIsEqualParams::class.java] = number_is_equal
        registry[NumberNotInListParams::class.java] = number_not_in_list
        registry[MiMCHashPreImageParams::class.java] = mimc_hash_preimage
        registry[MiMCStringHashPreImageParams::class.java] = mimc_string_hash_preimage

        suggestedGenerators[NumbersInRangeParams::class.java] = 512
        suggestedGenerators[MiMCHashPreImageParams::class.java] = 1024
        suggestedGenerators[MiMCStringHashPreImageParams::class.java] = 8192
    }

    fun <T: GadgetParams> getGadgetType(params: T) = registry[params::class.java]

    fun <T: GadgetParams> getSuggestedGenerators(params: T) = suggestedGenerators.getOrDefault(params::class.java, 128)
}