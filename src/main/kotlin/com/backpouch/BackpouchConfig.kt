package com.backpouch

import net.neoforged.fml.ModList
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.common.ModConfigSpec

object BackpouchConfig {
    val useRelicsCharm: ModConfigSpec.BooleanValue
    val COMMON_SPEC: ModConfigSpec

    init {
        val builder = ModConfigSpec.Builder()
        useRelicsCharm = builder
            .comment(
                "Use Relics' charm slot instead of the built-in backpouch_charm slot.",
                "Requires Relics mod to be installed. Default: false"
            )
            .define("useRelicsCharm", false)
        COMMON_SPEC = builder.build()
    }

    fun register() {
        ModList.get().getModContainerById(BackpouchMod.MOD_ID).ifPresent { container ->
            container.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC)
        }
    }
}
