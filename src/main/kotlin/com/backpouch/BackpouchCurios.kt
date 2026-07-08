package com.backpouch

import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.InterModComms
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent
import top.theillusivec4.curios.api.SlotTypeMessage

@EventBusSubscriber(modid = BackpouchMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object BackpouchCurios {

    @SubscribeEvent
    fun onInterModComms(event: InterModEnqueueEvent) {
        InterModComms.sendTo("curios", "register_slot") {
            SlotTypeMessage.Builder("backpouch")
                .icon(ResourceLocation.parse("backpouch:textures/item/slot_backpouch"))
                .size(1)
                .build()
        }
    }
}
