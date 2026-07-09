package com.backpouch.item

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.SlotItemHandler
import top.theillusivec4.curios.api.CuriosApi

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

        addSlot(object : SlotItemHandler(handler, 0, 8, 18) {
            override fun mayPickup(player: Player): Boolean {
                if (!hasItem()) return true
                if (getItem().item is SlotUpgradeItem && hasOccupiedCharmSlots(player)) return false
                return super.mayPickup(player)
            }
        })
        addSlot(object : SlotItemHandler(handler, 1, 26, 18) {
            override fun mayPickup(player: Player): Boolean {
                if (!hasItem()) return true
                if (getItem().item is SlotUpgradeItem && hasOccupiedCharmSlots(player)) return false
                return super.mayPickup(player)
            }
        })

        val yOff = (3 - 4) * 18

        for (row in 0..2) {
            for (col in 0..8) {
                addSlot(Slot(inv, col + row * 9 + 9, 8 + col * 18, 103 + row * 18 + yOff))
            }
        }
        for (col in 0..8) {
            addSlot(Slot(inv, col, 8 + col * 18, 161 + yOff))
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

    private fun hasOccupiedCharmSlots(player: Player): Boolean {
        val opt = CuriosApi.getCuriosInventory(player)
        if (opt.isEmpty) return false
        val curios = opt.get()
        val charmHandler = curios.curios["charm"] ?: return false
        for (i in 0 until charmHandler.slots) {
            val stack = charmHandler.stacks.getStackInSlot(i)
            if (!stack.isEmpty && stack.item !is BackpouchItem) return true
        }
        return false
    }
}
