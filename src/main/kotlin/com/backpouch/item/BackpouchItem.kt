package com.backpouch.item

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor
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
import com.backpouch.BackpouchConfig

open class BackpouchItem(
    properties: Properties,
    private val baseSlots: Int
) : Item(properties), ICurioItem {

    companion object {
        val CHARM_MODIFIER_ID = ResourceLocation.parse("backpouch:charm_bonus")

        fun hasTombstoneUpgrade(stack: ItemStack): Boolean {
            val data = stack.get(DataComponents.CUSTOM_DATA) ?: return false
            val tag = data.copyTag()
            if (!tag.contains("upgrades", 9)) return false
            val upgrades = tag.getList("upgrades", 8)
            for (i in 0 until upgrades.size) {
                if (upgrades.getString(i) == "backpouch:tombstone_upgrade") return true
            }
            return false
        }
    }

    override fun hasCurioCapability(stack: ItemStack): Boolean = true

    override fun getName(stack: ItemStack): Component {
        return super.getName(stack).copy().withStyle { it.withColor(TextColor.fromRgb(0x56BFFF)) }
    }

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

    private fun readUpgradeIds(stack: ItemStack): List<String> {
        val data = stack.get(DataComponents.CUSTOM_DATA) ?: return emptyList()
        val tag = data.copyTag()
        if (!tag.contains("upgrades", 9)) return emptyList()
        val upgrades = tag.getList("upgrades", 8)
        val result = mutableListOf<String>()
        for (i in 0 until upgrades.size) {
            result.add(upgrades.getString(i))
        }
        return result
    }

    open fun getUpgradeIds(stack: ItemStack): List<String> = readUpgradeIds(stack)

    open fun getUpgradeCount(stack: ItemStack): Int {
        return readUpgradeIds(stack).size
    }

    open fun getUpgradeBonus(stack: ItemStack, provider: HolderLookup.Provider): Int {
        val upgrades = readUpgradeIds(stack)
        var bonus = 0
        for (upgradeId in upgrades) {
            bonus += when (upgradeId) {
                "backpouch:bi_slot_upgrade" -> 2
                "backpouch:quadru_slot_upgrade" -> 4
                else -> 0
            }
        }
        return bonus
    }

    override fun getDropRule(
        slotContext: SlotContext, source: DamageSource, recentlyHit: Boolean, stack: ItemStack
    ): ICurio.DropRule {
        if (hasTombstoneUpgrade(stack)) {
            return ICurio.DropRule.ALWAYS_KEEP
        }
        return ICurio.DropRule.DEFAULT
    }

    override fun getAttributeModifiers(
        slotContext: SlotContext, id: ResourceLocation, stack: ItemStack
    ): Multimap<Holder<Attribute>, AttributeModifier> {
        val entity = slotContext.entity()
        val level = entity?.level()
        val provider = level?.registryAccess()
        val totalSlots = if (provider != null) {
            baseSlots + getUpgradeBonus(stack, provider)
        } else {
            baseSlots
        }

        val slotName = if (BackpouchConfig.useRelicsCharm.get()) "charm" else "backpouch_charm"
        val modifiers = HashMultimap.create<Holder<Attribute>, AttributeModifier>()
        modifiers.put(
            SlotAttribute.getOrCreate(slotName),
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
        val used = getUpgradeCount(stack)
        val upgrades = getUpgradeIds(stack)
        tooltipComponents.add(
            Component.translatable("tooltip.backpouch.sockets", used, 2)
                .withStyle(ChatFormatting.GRAY)
        )
        if (upgrades.isNotEmpty()) {
            val names = upgrades.joinToString(", ") { id ->
                Component.translatable("item.$id".replace(':', '.')).string
            }
            tooltipComponents.add(
                Component.translatable("tooltip.backpouch.upgrade_list", names)
                    .withStyle(ChatFormatting.GRAY)
            )
        } else {
            tooltipComponents.add(
                Component.translatable("tooltip.backpouch.no_upgrades")
                    .withStyle(ChatFormatting.GRAY)
            )
        }
    }
}
