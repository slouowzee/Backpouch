package com.backpouch

import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object BackpouchCreativeTab {
    val TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BackpouchMod.MOD_ID)

    val TAB = TABS.register("backpouch",
        Supplier {
            CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.backpouch"))
                .icon { ItemStack(BackpouchItems.LEATHER_BACKPOUCH.get()) }
                .displayItems { params, output ->
                    output.accept(BackpouchItems.LEATHER_BACKPOUCH.get())
                    output.accept(BackpouchItems.IRON_BACKPOUCH.get())
                    output.accept(BackpouchItems.GOLD_BACKPOUCH.get())
                    output.accept(BackpouchItems.DIAMOND_BACKPOUCH.get())
                    output.accept(BackpouchItems.NETHERITE_BACKPOUCH.get())
                    output.accept(BackpouchItems.BI_SLOT_UPGRADE.get())
                    output.accept(BackpouchItems.QUADRU_SLOT_UPGRADE.get())
                    output.accept(BackpouchItems.TOMBSTONE_UPGRADE.get())
                }
                .build()
        })
}
