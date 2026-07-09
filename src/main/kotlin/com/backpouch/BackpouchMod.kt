package com.backpouch

import com.backpouch.item.BackpouchItem
import com.backpouch.recipe.BackpouchRecipeSerializers
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModList
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.common.NeoForge
import top.theillusivec4.curios.api.CuriosApi
import top.theillusivec4.curios.api.event.DropRulesEvent
import top.theillusivec4.curios.api.type.capability.ICurio

@Mod(BackpouchMod.MOD_ID)
class BackpouchMod(bus: IEventBus) {
    companion object {
        const val MOD_ID = "backpouch"
    }

    init {
        BackpouchConfig.register()
        ModList.get().getModContainerById(MOD_ID).ifPresent { container ->
            container.registerExtensionPoint(
                IConfigScreenFactory::class.java,
                IConfigScreenFactory { modContainer, screen -> ConfigurationScreen(modContainer, screen) }
            )
        }
        BackpouchItems.ITEMS.register(bus)
        BackpouchCreativeTab.TABS.register(bus)
        BackpouchRecipeSerializers.SERIALIZERS.register(bus)

        CuriosApi.registerCurioPredicate(
            ResourceLocation.parse("$MOD_ID:backpouch")
        ) { slotResult ->
            slotResult.stack().item is BackpouchItem &&
                slotResult.slotContext().identifier() == "backpouch"
        }

        NeoForge.EVENT_BUS.addListener<DropRulesEvent> { event ->
            event.addOverride(
                { stack -> stack.item is BackpouchItem && BackpouchItem.hasTombstoneUpgrade(stack) },
                ICurio.DropRule.ALWAYS_KEEP
            )
        }
    }
}
