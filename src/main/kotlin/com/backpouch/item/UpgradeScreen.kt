package com.backpouch.item

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class UpgradeScreen(
    menu: UpgradeMenu,
    inv: Inventory,
    title: Component
) : AbstractContainerScreen<UpgradeMenu>(menu, inv, title) {

    companion object {
        val CONTAINER_TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png")
    }

    override fun init() {
        imageHeight = 141
        super.init()
        inventoryLabelY = 36
        titleLabelX = -10000
        titleLabelY = -10000
    }

    override fun renderBg(graphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        val x = leftPos
        val y = topPos

        for (slot in menu.slots.take(2)) {
            graphics.blit(CONTAINER_TEXTURE, x + slot.x - 1, y + slot.y - 1, 7, 17, 18, 18)
        }

        graphics.blit(CONTAINER_TEXTURE, x, y + 44, 0, 125, imageWidth, 96)
    }

    override fun renderLabels(graphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040)
    }
}
