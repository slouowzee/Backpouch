package com.backpouch.item

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import top.theillusivec4.curios.api.SlotAttribute
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurioItem

open class BackpouchItem(
    properties: Properties,
    private val baseSlots: Int,
    private val isUpgradable: Boolean = false
) : Item(properties), ICurioItem {

    companion object {
        val CHARM_MODIFIER_ID = ResourceLocation.parse("backpouch:charm_bonus")
    }

    override fun hasCurioCapability(stack: ItemStack): Boolean = true

    open fun getUpgradeBonus(stack: ItemStack, provider: HolderLookup.Provider): Int = 0

    override fun getAttributeModifiers(
        slotContext: SlotContext, id: ResourceLocation, stack: ItemStack
    ): Multimap<Holder<Attribute>, AttributeModifier> {
        val modifiers = HashMultimap.create<Holder<Attribute>, AttributeModifier>()
        val charmHolder = SlotAttribute.getOrCreate("charm")
        val provider = slotContext.entity().level().registryAccess()
        val totalSlots = baseSlots + getUpgradeBonus(stack, provider)

        modifiers.put(
            charmHolder,
            AttributeModifier(
                CHARM_MODIFIER_ID,
                totalSlots.toDouble(),
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
