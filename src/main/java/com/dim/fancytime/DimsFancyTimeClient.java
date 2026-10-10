package com.dim.fancytime;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;

public class DimsFancyTimeClient implements ClientModInitializer {

        @Override
        public void onInitializeClient() {
            FancyTimeConfig.load(); //Reads the config file which contains the position of the HUD element. It reads it before the element even loads!
            HudElementRegistry.attachElementBefore(
                    VanillaHudElements.HOTBAR, //Tells the time HUD element to launch before the hotbar.
                    Identifier.fromNamespaceAndPath(DimSFancyTime.MOD_ID, "fancy_time_hud"),
                    DimsFancyTimeClient::renderHud
            );
            HudElementRegistry.attachElementBefore(
                    VanillaHudElements.HOTBAR,
                    Identifier.fromNamespaceAndPath(DimSFancyTime.MOD_ID, "sleep_chime_hud"),
                    DimsFancyTimeClient::renderSleepChime
            );
        }

        private static boolean wasSleepable = false;
        private static long popupStartTime = -1;

        private static void renderHud(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
            //Preventing a NullPointerException from level (and player, if I decide to add it in the future)
            if (Minecraft.getInstance().level == null || Minecraft.getInstance().player == null) return;
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

            //Fixes the bug when the clock used to get hidden behind active effects when TOP_LEFT before 1.4.
            long visibleEffects = Minecraft.getInstance().player.getActiveEffects().stream()
                    .filter(MobEffectInstance::showIcon)
                    .count();
            int effectShift = (int) visibleEffects * 25;


            switch (FancyTimeConfig.currentPosition) { //Checks the current enum value
                case TOP_RIGHT -> x = graphics.guiWidth() - textWidth - 10 - effectShift;
                case CENTER -> x = (graphics.guiWidth() - textWidth) / 2;
                default -> x = 10;
            }
            if (FancyTimeConfig.clockBackground) {
                drawBackdrop(graphics, x, y, textWidth);
            }
            graphics.text(Minecraft.getInstance().font, formattedTime, x, y, FancyTimeConfig.currentColor, FancyTimeConfig.shadowEnabled);
        }

        private static void drawBackdrop(GuiGraphicsExtractor graphics, int x, int y, int textWidth) {
            int padding = 2;
            graphics.fill(x - padding, y - padding, x + textWidth + padding, y + 8 + padding, 0x88000000);
        }

        private static void renderSleepChime(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {

            //Preventing a NullPointerException from level, player and gameMode
            if (Minecraft.getInstance().level == null|| Minecraft.getInstance().player == null || Minecraft.getInstance().gameMode == null) return;

            if (FancyTimeConfig.sleepChime == SleepChime.DISABLED) return; //Basically tells the whole do a chime when the player can sleep function thing to stop working if the player has it disabled. Why did I use such dramatic wording it's literally the most self-explanatory line ever in this class
            if (FancyTimeConfig.sleepChime == SleepChime.ONLY_IN_SURVIVAL && !(Minecraft.getInstance().gameMode.getPlayerMode() == GameType.SURVIVAL)) return; //Checks the gamemode of the player in order for the ONLY_IN_SURVIVAL option to work. If the gamemode is NOT Survival, it does return; (which means give me nothing).


            long timeOfDay = Minecraft.getInstance().level.getOverworldClockTime() % 24000;
            boolean canSleepNow = timeOfDay >= 12542; //A comparison expression, timeOfDay >= 12542 comes back either true of false, and that value is stored in this boolean variable.

            if (canSleepNow && !wasSleepable) {
                wasSleepable = true;
                popupStartTime = System.currentTimeMillis();
                Minecraft.getInstance().player.playSound(SoundEvents.NOTE_BLOCK_PLING.value(), 1.0f, 1.0f);
            } else if (!canSleepNow) {
                wasSleepable = false;
            }
            if (System.currentTimeMillis() - popupStartTime < 3000) {
                String message = FancyTimeConfig.customSleepChime; //Reads from YOUR custom personalized fancy text!!
                int textWidth = Minecraft.getInstance().font.width(message);
                int x = (graphics.guiWidth() - textWidth) /2;
                int y = graphics.guiHeight() -50;
                int extraOffset = 0; //This thing right here saves my life, it makes sure that when for example both armor bar and bubble bar exist they don't raise the reminder by 20px but still by 10 (since they sit on the same row).

                //Fixes the bug where the "You can sleep now" text hid behind the armor, breathing bubble and rideable mob bars in 1.3.
                if(Minecraft.getInstance().player.getArmorValue() > 0) {
                    extraOffset = Math.max(extraOffset, 10);
                }

                if (Minecraft.getInstance().player.getAirSupply() < Minecraft.getInstance().player.getMaxAirSupply()) {
                    extraOffset = Math.max(extraOffset, 10);
                }

                if (Minecraft.getInstance().player.getVehicle() instanceof LivingEntity mount) {
                    int heartRows = (int) Math.ceil(mount.getMaxHealth()/20.0f); //Each row holds 10 hearts, and one hearth is 2 health ppoints, so one row = 20 health.
                    extraOffset = Math.max(extraOffset, (heartRows -1) * 10);
                }
                y -= extraOffset;
                graphics.text(Minecraft.getInstance().font, message, x, y, FancyTimeConfig.currentColor, FancyTimeConfig.shadowEnabled);
            }
        }
    }