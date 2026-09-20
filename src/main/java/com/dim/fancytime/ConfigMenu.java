package com.dim.fancytime;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import net.minecraft.network.chat.Component;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;

import java.awt.*;

public class ConfigMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Dim's Fancy Time Settings"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("General"))
                        .option(Option.<HudPosition>createBuilder()
                                .name(Component.literal("Position"))
                                .binding(
                                        HudPosition.TOP_LEFT, //The default value if nothing has been set yet
                                        () -> FancyTimeConfig.currentPosition, //Getter lambda; gets the current value
                                        (value) -> FancyTimeConfig.currentPosition = value //Setter lambda; writes a new value
                                )
                                .controller(opt -> EnumControllerBuilder.create(opt)
                                        .enumClass(HudPosition.class)
                                        .formatValue(v -> Component.literal(v.getDisplayName()))) //Implements the gentle looking button text preview thing! Sorry guys I suck at describing what things do like I mean I kind of start the description well but it doesn't end nicely :( excuse me pls
                                .build())
                        .option(Option.<TimeFormat>createBuilder()
                                .name(Component.literal("12h/24h Time Format"))
                                .binding(
                                        TimeFormat.HOUR_12,
                                        () -> FancyTimeConfig.currentTimeFormat,
                                        (value) -> FancyTimeConfig.currentTimeFormat = value
                                )
                                .controller(opt -> EnumControllerBuilder.create(opt)
                                        .enumClass(TimeFormat.class)
                                        .formatValue(v -> Component.literal(v.getDisplayName())))
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Text shadow"))
                                .binding(
                                        true,
                                        () -> FancyTimeConfig.shadowEnabled,
                                        (value) -> FancyTimeConfig.shadowEnabled = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Component.literal("Clock text color"))
                                .binding(
                                        new Color(0xFFFFFFFF, true),
                                        () -> new Color (FancyTimeConfig.currentColor, true), //Wraps the stored int into a Color object when YACL asks for the current value
                                        (value) -> FancyTimeConfig.currentColor = value.getRGB()
                                )
                                .controller(opt -> ColorControllerBuilder.create(opt)
                                        )
                                .build())
                        .build())
                .save(FancyTimeConfig::save)
                .build()
                .generateScreen(parent);
    }
}

//I just wanted to add a smiley face here :)
//Fun fact: this smiley face was in the old FancyTimeConfigScreen class that housed the old menu, which was removed in version 1.1.