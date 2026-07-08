package com.backpouch.item

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import top.theillusivec4.curios.api.SlotAttribute
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurio

class BackpouchItem(
    properties: Properties,
    private val baseSlots: Int,
    private val isUpgradable: Boolean = false
) : Item(properties), ICurio {

    companion object {
        val CHARM_MODIFIER_ID = ResourceLocation.parse("backpouch:charm_bonus")
    }

    override fun getStack(): ItemStack = ItemStack.EMPTY

    override fun getAttributeModifiers(
        slotContext: SlotContext, uuid: ResourceLocation
    ): Multimap<Holder<Attribute>, AttributeModifier> {
        val modifiers = HashMultimap.create<Holder<Attribute>, AttributeModifier>()

        val charmHolder = SlotAttribute.getOrCreate("charm")

        modifiers.put(
            charmHolder,
            AttributeModifier(
                CHARM_MODIFIER_ID,
                baseSlots.toDouble(),
                AttributeModifier.Operation.ADD_VALUE
            )
        )

        return modifiers
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        if (isUpgradable) {
            tooltipComponents.add(
                Component.translatable("tooltip.backpouch.sockets", 0, 2)
            )
        }
        tooltipComponents.add(
            Component.translatable("tooltip.backpouch.charm_slots", baseSlots)
        )
    }
}
