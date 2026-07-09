package com.backpouch.recipe

import com.mojang.serialization.MapCodec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer

object BackpouchUpgradeRecipeSerializer : RecipeSerializer<BackpouchUpgradeRecipe> {

    override fun codec(): MapCodec<BackpouchUpgradeRecipe> = MapCodec.unit(BackpouchUpgradeRecipe())

    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, BackpouchUpgradeRecipe> =
        StreamCodec.of(
            { _, _ -> },
            { BackpouchUpgradeRecipe() }
        )
}
