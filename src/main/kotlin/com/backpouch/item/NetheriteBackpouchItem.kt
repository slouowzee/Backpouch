package com.backpouch.item

import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack

class NetheriteBackpouchItem(properties: Properties, baseSlots: Int) :
    BackpouchItem(properties, baseSlots, isUpgradable = true) {

    override fun getUpgradeCount(stack: ItemStack): Int {
        val data = stack.get(DataComponents.CUSTOM_DATA) ?: return 0
        val tag = data.copyTag()
        if (!tag.contains("upgrades", 9)) return 0
        return tag.getList("upgrades", 8).size
    }

    override fun getUpgradeBonus(stack: ItemStack, provider: HolderLookup.Provider): Int {
        val upgrades = readUpgradeIds(stack)
        var bonus = 0
        for (upgradeId in upgrades) {
            bonus += when (upgradeId) {
                "backpouch:bi_slot_upgrade" -> 2
                "backpouch:quadru_slot_upgrade" -> 4
                else -> 0
            }
        }
        return bonus
    }

    override fun getUpgradeIds(stack: ItemStack): List<String> = readUpgradeIds(stack)

    private fun readUpgradeIds(stack: ItemStack): List<String> {
        val data = stack.get(DataComponents.CUSTOM_DATA) ?: return emptyList()
        val tag = data.copyTag()
        if (!tag.contains("upgrades", 9)) return emptyList()
        val upgrades = tag.getList("upgrades", 8)
        val result = mutableListOf<String>()
        for (i in 0 until upgrades.size) {
            result.add(upgrades.getString(i))
        }
        return result
    }
}
