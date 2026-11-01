package com.dim.fancytime;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class SleepReminderToast implements Toast {
    private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/advancement");
    private static final int MAX_LINES = 2;
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
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, width(), height()); //Draws the toast bg

        int maxTextWidth = width() -20; //Figures out how many pixels wide text is allowed to exist before it has to wrap
        String text = FancyTimeConfig.customSleepChime; //Grabs YOUR custom text! Why am I so excited about StringController :(

        List<FormattedCharSequence> lines = font.split(FormattedText.of(text), maxTextWidth); //This one here makes the plain simple String into the good stuff that Minecraft wants. font.split() then measures the real pixel-width of the text

        if (lines.size() > MAX_LINES) { //After wrapping, it checks if it needed more than 2 lines.
            //I hate adding comments and seperating lines if you get what I mean like I hate doing what I did now anyways this is a truncation branch it starts with the full text, then chops off one character from the end each loop. substring(0, trimmed.length() - 1) means “give me everything except the last character.” The while(!trimmed.isEmpty()) just guards against chopping forever
            String trimmed = text;
            while(!trimmed.isEmpty()) {
                trimmed = trimmed.substring(0, trimmed.length() - 1);
                //After each chop, re-wrap with "..." glued at the end. The moment it does save it into lines overwriting the long version and break out of the loop so it doesn't chop forever.
                List<FormattedCharSequence> testLines = font.split(FormattedText.of(trimmed + "..."), maxTextWidth);
                if (testLines.size() <= MAX_LINES) {
                    lines = testLines;
                    break;
                }
            }
        }
        //Draws stuff
        int y = 12;
        for (FormattedCharSequence line : lines) {
            int lineWidth = font.width(line);
            int x = (width() - lineWidth) / 2; //Kind of like that fix with the text sleep reminder when the bug essentially was that it tucked itself behind the armor and food bars etc, but now to make sure that the text is centered in toast when it has to be wrapped and another line to be added.
            graphics.text(font, line, x, y, FancyTimeConfig.currentColor, true);
            y += font.lineHeight;
        }
    }
}
