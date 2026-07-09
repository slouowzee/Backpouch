package com.backpouch

import com.backpouch.item.UpgradeScreen
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent

@Mod(BackpouchMod.MOD_ID)
class BackpouchMod(bus: IEventBus) {
    companion object {
        const val MOD_ID = "backpouch"
    }

    init {
        BackpouchItems.ITEMS.register(bus)
        BackpouchCreativeTab.TABS.register(bus)
        BackpouchMenus.MENUS.register(bus)

        bus.addListener { event: RegisterMenuScreensEvent ->
            event.register(BackpouchMenus.UPGRADE_MENU.get(), ::UpgradeScreen)
        }
    }
}
