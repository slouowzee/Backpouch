package com.backpouch.item

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import top.theillusivec4.curios.api.SlotAttribute
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurio
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

    open fun getUpgradeCount(stack: ItemStack): Int = 0

    private fun hasTombstoneUpgrade(stack: ItemStack): Boolean {
        val data = stack.get(DataComponents.CUSTOM_DATA) ?: return false
        val tag = data.copyTag()
        if (!tag.contains("upgrades", 10)) return false
        val upgrades = tag.getCompound("upgrades")
        if (!upgrades.contains("Items", 9)) return false
        val items = upgrades.getList("Items", 10)
        for (i in 0 until items.size) {
            if (items.getCompound(i).getString("id") == "backpouch:tombstone_upgrade") return true
        }
        return false
    }

    override fun getDropRule(
        slotContext: SlotContext, source: DamageSource, recentlyHit: Boolean, stack: ItemStack
    ): ICurio.DropRule {
        if (isUpgradable && hasTombstoneUpgrade(stack)) {
            return ICurio.DropRule.ALWAYS_KEEP
        }
        return ICurio.DropRule.DEFAULT
    }

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
            val used = getUpgradeCount(stack)
            tooltipComponents.add(
                Component.translatable("tooltip.backpouch.sockets", used, 2)
            )
        }
        tooltipComponents.add(
            Component.translatable("tooltip.backpouch.charm_slots", baseSlots)
        )
    }
}
