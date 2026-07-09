package com.backpouch

import com.backpouch.item.BackpouchItem
import com.backpouch.recipe.BackpouchRecipeSerializers
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import top.theillusivec4.curios.api.CuriosApi

@Mod(BackpouchMod.MOD_ID)
class BackpouchMod(bus: IEventBus) {
    companion object {
        const val MOD_ID = "backpouch"
    }

    init {
        BackpouchItems.ITEMS.register(bus)
        BackpouchCreativeTab.TABS.register(bus)
        BackpouchRecipeSerializers.SERIALIZERS.register(bus)

        CuriosApi.registerCurioPredicate(
            ResourceLocation.parse("$MOD_ID:backpouch")
        ) { slotResult ->
            slotResult.stack().item is BackpouchItem &&
                slotResult.slotContext().identifier() == "backpouch"
        }
    }
}
