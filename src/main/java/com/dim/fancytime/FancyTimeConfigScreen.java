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

    @Override
    protected void init() {
        this.addRenderableWidget(
                Button.builder(Component.literal("Position: " + FancyTimeConfig.currentPosition), (button) -> {
                    int nextIndex = (FancyTimeConfig.currentPosition.ordinal() + 1) % HudPosition.values().length; //In order: grabs the current position, moves forward +1, returns all enum values in the order of how I defined them, grabs the total values, modulo wraps back to 0 once we get to 2, which is the final value
                    FancyTimeConfig.currentPosition = HudPosition.values()[nextIndex]; //Grabs the enum value sitting at that array position
                    button.setMessage(Component.literal("Position: " + FancyTimeConfig.currentPosition)); //AActually updates the button's text!
                    FancyTimeConfig.save();
                }).bounds(20, 20, 200, 20).build()

        );

        int doneButtonX = (this.width - 200) / 2; //Finds the center of the screen, much like titleX
        int doneButtonY = this.height - 40; //The same for doneButtonX, but for Y
        Button doneButton = Button.builder(Component.literal("Done"), (button) -> {
            this.onClose(); //The actual close function
        }).bounds(doneButtonX, doneButtonY, 200, 20).build();

        this.addRenderableWidget(doneButton);
    }

    //init needs a little sibling...
    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) { //super.extractRenderState(...) is crucial, since it tells Screen to do its normal drawing before my mod does anything on top. Screen might not render anything if this is skipped!
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        int titleX = (this.width - this.font.width(this.title)) / 2; //Variable to find the center of the screen thing
        graphics.text(this.font, this.title, titleX, 5, 0xFFFFFFFF); //Uses the titleX variable which tells it where the center is, then puts the actual text there!
    }

    //This one redirects to the Mod Menu mod list thing (parent), rather than the Minecraft screen when the player clicks done.
    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parent);
    }

}

//I just wanted to add a smiley face here :)