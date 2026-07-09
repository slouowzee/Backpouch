package com.backpouch.recipe

import com.backpouch.item.BackpouchItem
import com.backpouch.item.SlotUpgradeItem
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SmithingRecipe
import net.minecraft.world.item.crafting.SmithingRecipeInput
import net.minecraft.world.level.Level

class BackpouchUpgradeRecipe : SmithingRecipe {

    override fun matches(input: SmithingRecipeInput, level: Level): Boolean {
        val base = input.base()
        val addition = input.addition()
        return base.item is BackpouchItem && addition.item is SlotUpgradeItem
    }

    override fun assemble(input: SmithingRecipeInput, provider: HolderLookup.Provider): ItemStack {
        val base = input.base()
        val addition = input.addition()
        val result = base.copy()
        if (addition.item is SlotUpgradeItem) {
            addUpgrade(result, addition)
        }
        return result
    }

    override fun getResultItem(provider: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    override fun isTemplateIngredient(stack: ItemStack): Boolean = false

    override fun isBaseIngredient(stack: ItemStack): Boolean =
        stack.item is BackpouchItem

    override fun isAdditionIngredient(stack: ItemStack): Boolean =
        stack.item is SlotUpgradeItem
    override fun getSerializer(): RecipeSerializer<*> = BackpouchUpgradeRecipeSerializer

    override fun isSpecial(): Boolean = true

    private fun addUpgrade(stack: ItemStack, upgradeStack: ItemStack) {
        val upgradeId = BuiltInRegistries.ITEM.getKey(upgradeStack.item).toString()
        val current = stack.getOrDefault(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY)
        val updated = current.update { tag ->
            val upgrades = if (tag.contains("upgrades", 9)) {
                tag.getList("upgrades", 8)
            } else {
                ListTag()
            }

            for (i in 0 until upgrades.size) {
                if (upgrades.getString(i) == upgradeId) return@update
            }

            upgrades.add(StringTag.valueOf(upgradeId))
            tag.put("upgrades", upgrades)
        }
        stack.set(DataComponents.CUSTOM_DATA, updated)
    }
}
