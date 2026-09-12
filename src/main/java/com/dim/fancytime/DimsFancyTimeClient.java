package com.dim.fancytime;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;

    public class DimsFancyTimeClient implements ClientModInitializer {

        @Override
        public void onInitializeClient() {
            FancyTimeConfig.load(); //Reads the config file which contains the position of the HUD element. It reads it before the element even loads!
            HudElementRegistry.attachElementBefore(
                    VanillaHudElements.HOTBAR, //Tells the time HUD element to launch before the hotbar.
                    Identifier.fromNamespaceAndPath(DimSFancyTime.MOD_ID, "fancy_time_hud"),
                    DimsFancyTimeClient::renderHud
            );
        }

        private static void renderHud(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
        long timeOfDay = Minecraft.getInstance().level.getOverworldClockTime();

        long shiftedTime = (timeOfDay + 6000) % 24000; //Makes sure the time cannot go over 24000 ticks.
        double totalMinutes = shiftedTime / 16.67;
        int hours = (int)(totalMinutes / 60); //Rounds up the decimals.
        int minutes = (int)(totalMinutes % 60);

        String amPM = (hours < 12) ? "AM" : "PM"; //If hours < 12 is true the expression becomes AM, otherwise it becomes PM.
            String formattedTime = String.format("%02d:%02d", hours, minutes); //Keeps the original formattedLine as the default (the 24-hour one), so if the player's format is HOUR_24 nothing below the if block runs

            if (FancyTimeConfig.currentTimeFormat == TimeFormat.HOUR_12) {
                int displayHours = hours % 12;
                if (displayHours == 0) {
                    displayHours = 12;
                }
                formattedTime = String.format("%02d:%02d %s", displayHours, minutes, amPM);
            }


            int textWidth = Minecraft.getInstance().font.width(formattedTime);
            int x;
            int y = 10;

            switch (FancyTimeConfig.currentPosition) { //Checks the current enum value
                case TOP_RIGHT -> x = graphics.guiWidth() - textWidth - 10;
                case CENTER -> x = (graphics.guiWidth() - textWidth) / 2;
                default -> x = 10;
            }

            graphics.text(Minecraft.getInstance().font, formattedTime, x, y, 0xFFFFFFFF);
        }
    }