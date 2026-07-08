package com.backpouch

import com.backpouch.item.UpgradeScreen
import net.minecraft.core.registries.Registries
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.registries.RegisterEvent
import top.theillusivec4.curios.api.CuriosApi

@Mod(BackpouchMod.MOD_ID)
class BackpouchMod(bus: IEventBus) {
    companion object {
        const val MOD_ID = "backpouch"
    }

    init {
        BackpouchItems.ITEMS.register(bus)
        BackpouchCreativeTab.TABS.register(bus)
        BackpouchMenus.MENUS.register(bus)

        bus.addListener { event: RegisterEvent ->
            if (event.getRegistryKey() == Registries.ITEM) {
                listOf(
                    BackpouchItems.LEATHER_BACKPOUCH,
                    BackpouchItems.IRON_BACKPOUCH,
                    BackpouchItems.GOLD_BACKPOUCH,
                    BackpouchItems.DIAMOND_BACKPOUCH,
                    BackpouchItems.NETHERITE_BACKPOUCH,
                ).forEach { CuriosApi.registerCurio(it.get(), it.get()) }
            }
        }

        bus.addListener { event: RegisterMenuScreensEvent ->
            event.register(BackpouchMenus.UPGRADE_MENU.get(), ::UpgradeScreen)
        }
    }
}
