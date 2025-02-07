package com.example.demo.ulti;

public class StringHelper {
    private static final char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

    public static String generateString(int len) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            stringBuilder.append(chars[(int) (Math.random() * chars.length)]);
        }
        return stringBuilder.toString();
    }
}
