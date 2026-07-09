package com.backpouch.item

import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.neoforged.neoforge.items.ItemStackHandler

class UpgradeInventory : ItemStackHandler {

    private var stackRef: ItemStack? = null
    private var provider: HolderLookup.Provider = RegistryAccess.EMPTY

    constructor() : super(2)

    constructor(stack: ItemStack, provider: HolderLookup.Provider = RegistryAccess.EMPTY) : super(2) {
        this.stackRef = stack
        this.provider = provider
        val data = stack.get(DataComponents.CUSTOM_DATA)
        if (data != null) {
            val tag = data.copyTag()
            if (tag.contains("upgrades", 10)) {
                deserializeNBT(provider, tag.getCompound("upgrades"))
            }
        }
    }

    override fun onContentsChanged(slot: Int) {
        stackRef?.let { s ->
            val current = s.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
            val updated = current.update { tag -> tag.put("upgrades", serializeNBT(provider)) }
            s.set(DataComponents.CUSTOM_DATA, updated)
        }
    }

    override fun getSlotLimit(slot: Int): Int = 1

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
        return stack.item is SlotUpgradeItem && getStackInSlot(slot).isEmpty
    }
}
