package com.backpouch

import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import net.minecraft.core.registries.Registries

object BackpouchItems {
    val ITEMS = DeferredRegister.create(Registries.ITEM, BackpouchMod.MOD_ID)

    val LEATHER_BACKPOUCH: DeferredItem<Item> = ITEMS.register("leather_backpouch") {
        Item(Item.Properties().stacksTo(1))
    }

    val IRON_BACKPOUCH: DeferredItem<Item> = ITEMS.register("iron_backpouch") {
        Item(Item.Properties().stacksTo(1))
    }

    val GOLD_BACKPOUCH: DeferredItem<Item> = ITEMS.register("gold_backpouch") {
        Item(Item.Properties().stacksTo(1))
    }

    val DIAMOND_BACKPOUCH: DeferredItem<Item> = ITEMS.register("diamond_backpouch") {
        Item(Item.Properties().stacksTo(1))
    }

    val NETHERITE_BACKPOUCH: DeferredItem<Item> = ITEMS.register("netherite_backpouch") {
        Item(Item.Properties().stacksTo(1).fireResistant())
    }

    val SLOT_UPGRADE_BASIC: DeferredItem<Item> = ITEMS.register("slot_upgrade_basic") {
        Item(Item.Properties().stacksTo(16))
    }

    val SLOT_UPGRADE_ADVANCED: DeferredItem<Item> = ITEMS.register("slot_upgrade_advanced") {
        Item(Item.Properties().stacksTo(16))
    }
}
