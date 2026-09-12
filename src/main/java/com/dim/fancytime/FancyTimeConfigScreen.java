package com.dim.fancytime;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphicsExtractor; //Fixed like 11 errors, I love imports!!

public class FancyTimeConfigScreen extends Screen {

    private final Screen parent;

    public FancyTimeConfigScreen(Screen parent) {
        super(Component.literal("Dim's Fancy Time Settings"));
        this.parent = parent;

    }

    private static final int PANEL_MARGIN = 30;
    private static final int ROW_SPACING = 50;
    private static final int BUTTON_WIDTH = 140;

    private int panelLeft, panelRight, panelTop, panelBottom;
    private int labelX, buttonX, row1Y, row2Y;

    @Override
    protected void init() {
        panelLeft = PANEL_MARGIN;
        panelRight = this.width - PANEL_MARGIN;
        panelTop = 30;
        panelBottom = this.height - 60;

        int rowWidth = 300;
        labelX = (this.width - rowWidth) / 2;
        buttonX = labelX + 120;
        row1Y = panelTop + 20;
        row2Y = row1Y + ROW_SPACING;

        this.addRenderableWidget(
                Button.builder(Component.literal(FancyTimeConfig.currentPosition.getDisplayName()), (button) -> {
                    int nextIndex = (FancyTimeConfig.currentPosition.ordinal() + 1) % HudPosition.values().length; //In order: grabs the current position, moves forward +1, returns all enum values in the order of how I defined them, grabs the total values, modulo wraps back to 0 once we get to 2, which is the final value
                    FancyTimeConfig.currentPosition = HudPosition.values()[nextIndex]; //Grabs the enum value sitting at that array position
                    button.setMessage(Component.literal(FancyTimeConfig.currentPosition.getDisplayName())); //AActually updates the button's text!
                    FancyTimeConfig.save();
                }).bounds(buttonX, row1Y, BUTTON_WIDTH, 20).build()

        );

        this.addRenderableWidget(
                Button.builder(Component.literal(FancyTimeConfig.currentTimeFormat.getDisplayName()), (button) -> {
                    int nextIndex = (FancyTimeConfig.currentTimeFormat.ordinal() + 1) % TimeFormat.values().length;
                    FancyTimeConfig.currentTimeFormat = TimeFormat.values()[nextIndex];
                    button.setMessage(Component.literal(FancyTimeConfig.currentTimeFormat.getDisplayName()));
                    FancyTimeConfig.save();
                }).bounds(buttonX, row2Y, BUTTON_WIDTH, 20).build()
        );

        int doneButtonX = panelLeft;
        int doneButtonWidth = panelRight - panelLeft;
        int doneButtonY = this.height - 40;
        Button doneButton = Button.builder(Component.literal("Done"), (button) -> {
            this.onClose(); //The actual close function
        }).bounds(doneButtonX, doneButtonY, 200, 20).build();

        this.addRenderableWidget(doneButton);
    }

    //init needs a little sibling...
    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) { //super.extractRenderState(...) is crucial, since it tells Screen to do its normal drawing before my mod does anything on top. Screen might not render anything if this is skipped!
        graphics.fill(panelLeft - 2, panelTop - 2, panelRight + 2, panelBottom + 2, 0xFF555555); //The border for the cool translucent black board thing below!
        graphics.fill(panelLeft, panelTop, panelRight, panelBottom, 0xA0202020); //Draws a cool translucent black board thing, and then the config buttons are drawn on that!

        super.extractRenderState(graphics, mouseX, mouseY, delta);

        int titleX = (this.width - this.font.width(this.title)) / 2; //Variable to find the center of the screen thing
        graphics.text(this.font, this.title, titleX, 5, 0xFFFFFFFF); //Uses the titleX variable which tells it where the center is, then puts the actual text there!
        graphics.text(this.font, "Position", labelX, row1Y + 6, 0xFFFFFFFF); //Since buttons are 20 px tall and text is shorter than that, adding a small offset vertically centers the label text against the button's height! I love when things are symmetrical.
        graphics.text(this.font, "Format", labelX, row2Y + 6, 0xFFFFFFFF);
    }

    //This one redirects to the Mod Menu mod list thing (parent), rather than the Minecraft screen when the player clicks done.
    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parent);
    }

}

//I just wanted to add a smiley face here :)