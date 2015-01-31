package com.github.bingoohuang.utils.lang;

public class Str {
    public static StringBuilder padding(String s, char letter, int repeats) {
        StringBuilder sb = new StringBuilder(s);
        while (repeats-- > 0) {
            sb.append(letter);
        }

        return sb;
    }

    public static StringBuilder removeLastLetters(String s, char letter) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.charAt(sb.length() - 1) == letter)
            sb.deleteCharAt(sb.length() - 1);

        return sb;
    }
}
