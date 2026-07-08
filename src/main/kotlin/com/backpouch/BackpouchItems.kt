package com.backpouch

import com.backpouch.item.BackpouchItem
import com.backpouch.item.NetheriteBackpouchItem
import com.backpouch.item.SlotUpgradeItem
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object BackpouchItems {
    val ITEMS = DeferredRegister.create(Registries.ITEM, BackpouchMod.MOD_ID)

    val LEATHER_BACKPOUCH: DeferredHolder<Item, BackpouchItem> = ITEMS.register("leather_backpouch",
        Supplier { BackpouchItem(Item.Properties().stacksTo(1), baseSlots = 4) })

    val IRON_BACKPOUCH: DeferredHolder<Item, BackpouchItem> = ITEMS.register("iron_backpouch",
        Supplier { BackpouchItem(Item.Properties().stacksTo(1), baseSlots = 6) })

    val GOLD_BACKPOUCH: DeferredHolder<Item, BackpouchItem> = ITEMS.register("gold_backpouch",
        Supplier { BackpouchItem(Item.Properties().stacksTo(1), baseSlots = 8) })

    val DIAMOND_BACKPOUCH: DeferredHolder<Item, BackpouchItem> = ITEMS.register("diamond_backpouch",
        Supplier { BackpouchItem(Item.Properties().stacksTo(1), baseSlots = 10) })

    val NETHERITE_BACKPOUCH: DeferredHolder<Item, NetheriteBackpouchItem> = ITEMS.register("netherite_backpouch",
        Supplier { NetheriteBackpouchItem(Item.Properties().stacksTo(1).fireResistant(), baseSlots = 12) })

    val BI_SLOT_UPGRADE: DeferredHolder<Item, SlotUpgradeItem> = ITEMS.register("bi_slot_upgrade",
        Supplier { SlotUpgradeItem(Item.Properties().stacksTo(16), slotBonus = 2) })

    val QUADRU_SLOT_UPGRADE: DeferredHolder<Item, SlotUpgradeItem> = ITEMS.register("quadru_slot_upgrade",
        Supplier { SlotUpgradeItem(Item.Properties().stacksTo(16), slotBonus = 4) })

    val TOMBSTONE_UPGRADE: DeferredHolder<Item, SlotUpgradeItem> = ITEMS.register("tombstone_upgrade",
        Supplier { SlotUpgradeItem(Item.Properties().stacksTo(16).fireResistant(), slotBonus = 0) })
}
