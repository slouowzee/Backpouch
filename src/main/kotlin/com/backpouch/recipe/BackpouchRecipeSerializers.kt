package com.backpouch.recipe

import com.backpouch.BackpouchMod
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object BackpouchRecipeSerializers {
    val SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, BackpouchMod.MOD_ID)

    val BACKPOUCH_UPGRADE = SERIALIZERS.register("backpouch_upgrade", Supplier { BackpouchUpgradeRecipeSerializer })
}
