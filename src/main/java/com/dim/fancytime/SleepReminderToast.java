package com.dim.fancytime;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class SleepReminderToast implements Toast {
    private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/advancement");
    private long startTime = -1;
    //This code block answers if the toast should be on screen: if it hasn't started yet, or less than three seconds have passed since it started the toast shows, otherwise it hides.
    @Override
    public Toast.Visibility getWantedVisibility() {
        return (startTime == -1 || System.currentTimeMillis() - startTime < 3000)
                ? Toast.Visibility.SHOW
                : Toast.Visibility.HIDE;
    }

    @Override
    public void update(ToastManager manager, long fullyVisibleForMs) {
        if  (startTime == -1) {
            startTime = System.currentTimeMillis();
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, Font font, long fullyVisibleForMs){
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0,0, width(), height());
        graphics.text(font, FancyTimeConfig.customSleepChime, 10, 12, FancyTimeConfig.currentColor, true);
    }
}
