package com.blackcubes.skarger.goldmine.backend;

import com.blackcubes.skarger.goldmine.backend.model.Booster;

public class TestUtils {

    public static void printHighlighted(String text, Object... args) {
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf(text, args);
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    public static Booster getRandomBooster() {
        final Booster[] values = Booster.values();
        final int length = values.length;
        long number = Math.round(Math.random() * length);
        if (number >= length) number = length - 1;
        return values[(int) number];
    }
}
