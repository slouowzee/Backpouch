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

    override fun renderBg(graphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        val x = (width - imageWidth) / 2
        val y = (height - imageHeight) / 2
        graphics.blit(CONTAINER_TEXTURE, x, y, 0, 0, imageWidth, 3 * 18 + 17)
        graphics.blit(CONTAINER_TEXTURE, x, y + 3 * 18 + 17, 0, 3 * 18 + 17, imageWidth, imageHeight)
    }

    override fun renderLabels(graphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040)
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040)
    }
}
