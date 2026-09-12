package com.dim.fancytime;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FancyTimeConfig {
    public static HudPosition currentPosition = HudPosition.TOP_LEFT;
    public static TimeFormat currentTimeFormat = TimeFormat.HOUR_24;

    public static void save() {
        FancyTimeConfigData data = new FancyTimeConfigData(); //Creates fresh instance of the data holder
        data.position = currentPosition; //Copies the current value into the file, what we want saved.
        data.timeFormat = currentTimeFormat;

        Gson gson = new Gson(); //Creates the translator tool
        String json = gson.toJson(data); //Hands Gson the data object, and it automatically converts into a JSON-formatted string

        try {
            Path configPath = FabricLoader.getInstance().getConfigDir().resolve("dims-fancy-time.json"); //Builds the file location!
            Files.writeString(configPath, json); //Either creates or overwrites the JSON text on the disk.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("dims-fancy-time.json");
        if (Files.exists(configPath)) { //Checks if a config file actually exists, since if it did not, it would throw an error. Essentially says: "try to load if the file is actually there."
            try {
                String json = Files.readString(configPath); //Reads the file's raw content back into a string
                Gson gson = new Gson();
                FancyTimeConfigData data = gson.fromJson(json, FancyTimeConfigData.class); //Converts JSON text -> object. The FancyTimeConfigData.class part tells Gson to rebuild this as a FancyTimeConfigData object, matching the shape we saved it in.
                currentPosition = data.position; //Copy the loaded value into the shared field, so the mod can see the restored value.
                if (data.timeFormat != null){
                    currentTimeFormat = data.timeFormat;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}