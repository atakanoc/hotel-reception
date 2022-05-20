package hotelproject.controllers.utils;

import javafx.scene.text.Font;

/**
 * This class is used for retrieving the default font for UI design.
 */
public class Default {
    public static final String SFPath = "file:assets/font/SF_Pro.ttf";

    public static Font getSFPro(int size) {
        return Font.loadFont(SFPath, size);
    }
}
