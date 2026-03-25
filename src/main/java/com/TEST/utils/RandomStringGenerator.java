package com.TEST.utils;

import java.util.Random;

public class RandomStringGenerator {
    public static String generateRandomString(int length) {
        // Define the characters to use in the random string
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            // Pick a random character and append it to the string
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }

    public static void main(String[] args) {
        // Example usage
        String randomStr = generateRandomString(12);
        System.out.println("Generated random string: " + randomStr);
    }
}
