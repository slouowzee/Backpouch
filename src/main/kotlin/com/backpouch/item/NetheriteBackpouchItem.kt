package com.backpouch.item

import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class NetheriteBackpouchItem(properties: Properties, baseSlots: Int) :
    BackpouchItem(properties, baseSlots, isUpgradable = true) {

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)
        if (!level.isClientSide) {
            val provider = player.level().registryAccess()
            val handler = UpgradeInventory(stack, provider)
            player.openMenu(SimpleMenuProvider(
                { windowId, inv, _ -> UpgradeMenu(handler, windowId, inv) },
                Component.translatable("container.backpouch.upgrades")
            ))
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }

    override fun getUpgradeBonus(stack: ItemStack, provider: HolderLookup.Provider): Int {
        val handler = UpgradeInventory(stack, provider)
        var bonus = 0
        for (i in 0 until handler.slots) {
            val upgradeStack = handler.getStackInSlot(i)
            if (upgradeStack.item is SlotUpgradeItem) {
                bonus += (upgradeStack.item as SlotUpgradeItem).slotBonus
            }
        }
        return bonus
    }
}
