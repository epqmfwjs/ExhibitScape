package com.ExhibitScape.app.controller.community;

import java.util.Random;

public class ImageUtil {
	private static final String[] IMAGE_FILES = {"1.png", "2.png", "3.png", "5.png", "7.png", "8.png", "9.png"};

    public static String getRandomImageFileName() {
        Random random = new Random();
        int index = random.nextInt(IMAGE_FILES.length);
        return IMAGE_FILES[index];
    }
}
