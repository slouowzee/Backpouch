package com.backpouch

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod

@Mod(BackpouchMod.MOD_ID)
class BackpouchMod(bus: IEventBus) {
    companion object {
        const val MOD_ID = "backpouch"
    }

    init {
        BackpouchItems.ITEMS.register(bus)
        BackpouchCreativeTab.TABS.register(bus)
    }
}
