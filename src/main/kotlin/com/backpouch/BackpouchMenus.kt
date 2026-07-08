package com.backpouch

import com.backpouch.item.UpgradeMenu
import net.minecraft.core.registries.Registries
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object BackpouchMenus {
    val MENUS = DeferredRegister.create(Registries.MENU, BackpouchMod.MOD_ID)

    val UPGRADE_MENU: DeferredHolder<MenuType<*>, MenuType<UpgradeMenu>> = MENUS.register("upgrade_menu",
        Supplier {
            val type = MenuType<UpgradeMenu>({ id, inv -> UpgradeMenu(id, inv) }, FeatureFlags.VANILLA_SET)
            UpgradeMenu.menuType = type
            type
        })
}
