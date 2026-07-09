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
import top.theillusivec4.curios.api.CuriosApi
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

    override fun canUnequip(slotContext: SlotContext, stack: ItemStack): Boolean {
        val opt = CuriosApi.getCuriosInventory(slotContext.entity())
        if (opt.isEmpty) return true
        val curios = opt.get()
        for (entry in curios.curios.entries) {
            if (entry.key == slotContext.identifier()) {
                val handler = entry.value
                for (i in 0 until handler.slots) {
                    val itemStack = handler.stacks.getStackInSlot(i)
                    if (itemStack.isEmpty) continue
                    if (i == slotContext.index()) continue
                    if (itemStack.item is BackpouchItem) continue
                    return false
                }
            }
        }
        return true
    }

    open fun getUpgradeBonus(stack: ItemStack, provider: HolderLookup.Provider): Int = 0

    open fun getUpgradeCount(stack: ItemStack): Int = 0

    open fun getUpgradeIds(stack: ItemStack): List<String> = emptyList()

    private fun hasTombstoneUpgrade(stack: ItemStack): Boolean {
        return getUpgradeIds(stack).contains("backpouch:tombstone_upgrade")
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
        val totalSlots = context.level()?.let { baseSlots + getUpgradeBonus(stack, it.registryAccess()) } ?: baseSlots
        if (isUpgradable) {
            val used = getUpgradeCount(stack)
            val upgrades = getUpgradeIds(stack)
            tooltipComponents.add(
                Component.translatable("tooltip.backpouch.sockets", used, 2)
            )
            if (upgrades.isNotEmpty()) {
                val names = upgrades.joinToString(", ") { id ->
                    Component.translatable("item.$id".replace(':', '.')).string
                }
                tooltipComponents.add(
                    Component.translatable("tooltip.backpouch.upgrade_list", names)
                )
            } else {
                tooltipComponents.add(
                    Component.translatable("tooltip.backpouch.no_upgrades")
                )
            }
        }
        tooltipComponents.add(
            Component.translatable("tooltip.backpouch.charm_slots", totalSlots)
        )
    }
}
