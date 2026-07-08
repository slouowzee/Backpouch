package com.backpouch.item

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.SlotItemHandler

class UpgradeMenu(
    handler: UpgradeInventory,
    windowId: Int,
    inv: Inventory
) : AbstractContainerMenu(menuType, windowId) {

    companion object {
        lateinit var menuType: MenuType<UpgradeMenu>
    }

    constructor(windowId: Int, inv: Inventory) : this(UpgradeInventory(), windowId, inv)

    init {
        check(handler.slots == 2) { "UpgradeInventory must have exactly 2 slots" }

        addSlot(SlotItemHandler(handler, 0, 8, 18))
        addSlot(SlotItemHandler(handler, 1, 26, 18))

        for (row in 0..2) {
            for (col in 0..8) {
                addSlot(Slot(inv, col + row * 9 + 9, 8 + col * 18, 85 + row * 18))
            }
        }
        for (col in 0..8) {
            addSlot(Slot(inv, col, 8 + col * 18, 143))
        }
    }

    override fun stillValid(player: Player): Boolean = true

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        val slot = slots[index]
        if (!slot.hasItem()) return ItemStack.EMPTY

        val stack = slot.item
        val original = stack.copy()

        if (index < 2) {
            if (!moveItemStackTo(stack, 2, slots.size, true)) return ItemStack.EMPTY
        } else if (stack.item is SlotUpgradeItem) {
            if (!moveItemStackTo(stack, 0, 2, false)) return ItemStack.EMPTY
        } else {
            return ItemStack.EMPTY
        }

        if (stack.isEmpty) {
            slot.setByPlayer(ItemStack.EMPTY)
        } else {
            slot.setChanged()
        }

        return original
    }
}
