package com.backpouch

import net.neoforged.fml.ModList
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.common.ModConfigSpec

object BackpouchConfig {
    val useRelicsCharm: ModConfigSpec.BooleanValue
    val SPEC: ModConfigSpec

    init {
        val builder = ModConfigSpec.Builder()
        useRelicsCharm = builder
            .comment(
                "Use Relics' charm slot instead of the built-in backpouch_charm slot.",
                "Requires Relics mod to be installed. Default: false"
            )
            .define("useRelicsCharm", false)
        SPEC = builder.build()
    }

    fun register() {
        val container = ModList.get().getModContainerById(BackpouchMod.MOD_ID)
        container.ifPresent { it.registerConfig(ModConfig.Type.COMMON, SPEC) }
    }
}
