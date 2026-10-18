package com.dim.fancytime;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.network.chat.Component;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;

import java.awt.*;

public class ConfigMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Dim's Fancy Time Settings"))

                //Clock tab
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Clock"))

                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Cosmetic settings"))
                                .description(OptionDescription.of(Component.literal("Adjust the clock's position and style.")))

                        .option(Option.<HudPosition>createBuilder()
                                .name(Component.literal("Position"))
                                .description(OptionDescription.of(Component.literal("Changes the position of the clock on your screen.")))
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
                                .description(OptionDescription.of(Component.literal("Switches before the 12h and 24h time format.")))
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
                                .description(OptionDescription.of(Component.literal("Toggles the subtle shadow behind the clock.")))
                                .binding(
                                        true,
                                        () -> FancyTimeConfig.shadowEnabled,
                                        (value) -> FancyTimeConfig.shadowEnabled = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Clock background"))
                                .description(OptionDescription.of(Component.literal("Toggles the background behind the clock.")))
                                .binding(
                                        false,
                                        () -> FancyTimeConfig.clockBackground,
                                        (value) -> FancyTimeConfig.clockBackground = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Color>createBuilder()
                                .name(Component.literal("Clock text color"))
                                .description(OptionDescription.of(Component.literal("Lets you change the color of the clock. Also supports hex values!")))
                                .binding(
                                        new Color(0xFFFFFFFF, true),
                                        () -> new Color (FancyTimeConfig.currentColor, true), //Wraps the stored int into a Color object when YACL asks for the current value
                                        (value) -> FancyTimeConfig.currentColor = value.getRGB()
                                )
                                .controller(opt -> ColorControllerBuilder.create(opt)
                                )
                                .build())
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Enable clock"))
                                .description(OptionDescription.of(Component.literal("Enables/disables the clock.")))
                                .binding(
                                        true,
                                        () -> FancyTimeConfig.clockEnabled,
                                        (value) -> FancyTimeConfig.clockEnabled = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())

                //Sleep reminder tab
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Sleep reminder"))

                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Cosmetic settings"))
                                .description(OptionDescription.of(Component.literal("Configure the sleep reminder to your liking.")))

                        .option(Option.<SleepChime>createBuilder()
                                .name(Component.literal("Sleep reminder"))
                                .description(OptionDescription.of(Component.literal("Reminds you that you can sleep with a chime and text popup.")))
                                .binding(
                                        SleepChime.ENABLED,
                                        () -> FancyTimeConfig.sleepChime,
                                        (value) -> FancyTimeConfig.sleepChime = value
                                )
                                .controller(opt -> EnumControllerBuilder.create(opt)
                                        .enumClass(SleepChime.class)
                                        .formatValue(v -> Component.literal(v.getDisplayName())))
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Component.literal("Custom sleep reminder text"))
                                .description(OptionDescription.of(Component.literal("Lets you change the text of the sleep reminder to whatever you like so you can truly fancy up your sleep schedule.")))
                                .binding(
                                        "You can sleep now.",
                                        () -> FancyTimeConfig.customSleepChime,
                                        (value) -> FancyTimeConfig.customSleepChime = value
                                )
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<ReminderStyle>createBuilder()
                                .name(Component.literal("Sleep reminder style"))
                                .description(OptionDescription.of(Component.literal("Changes the way your sleep reminder is shown (text/toast).")))
                                .binding(
                                        ReminderStyle.TEXT,
                                        () -> FancyTimeConfig.reminderStyle,
                                        (value) -> FancyTimeConfig.reminderStyle = value
                                )
                                .controller(opt -> EnumControllerBuilder.create(opt)
                                        .enumClass(ReminderStyle.class)
                                        .formatValue(v -> Component.literal(v.getDisplayName())))
                                .build())
                                .build())
                        .build())
                .save(FancyTimeConfig::save)
                .build()
                .generateScreen(parent);
    }
}

//I just wanted to add a smiley face here :)
//Fun fact: this smiley face was in the old FancyTimeConfigScreen class that housed the old menu, which was removed in version 1.1 (with the class officially getting scrapped in 1.2 because I forgot to remove it in 1.1).
//Also this class was pissing me off because it was a long wall of text prior to 1.5 and the config menu also looked messy in game, so I made it cleaner! I love YACL.