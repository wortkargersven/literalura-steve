package com.steveprojects.literalura.model;

import java.text.Normalizer;
import java.util.Arrays;

public enum Languages {

    ENGLISH("en","English"),
    SPANISH("es","Spanish"),
    PORTUGUESE("pt","Portuguese"),
    FRENCH("fr","French"),
    ITALIAN("it", "Italian"),
    GERMAN("de", "German");


    private String gutendexLanguage;

    private String english;

    Languages (String gutendexLanguage, String english){
        this.gutendexLanguage = gutendexLanguage;
        this.english = english;
    }

    public static Languages fromGutendex (String text) {
        for (Languages languages : Languages.values()) {
            if (languages.gutendexLanguage.equals(text)) {
                return languages;
            }
        }
        throw new IllegalArgumentException("Language not found: " + text);
    }

    public static Languages fromEnglish(String text) {
        String normalisedText = normaliseText(text);
        for (Languages languages : Languages.values()) {
            if (normaliseText(languages.english).equals(normalisedText)) {
                return languages;
            }
        }
        throw new IllegalArgumentException("Language not found: " + text + ". Available languages: " +
                Arrays.toString(Languages.values()));
    }

    public static String normaliseText(String input){
        if (input == null) {
            return null;
        }
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}","")
                .toLowerCase();
    }

    public String getGutendexLanguage() {
        return gutendexLanguage;
    }

    public void setGutendexLanguage(String gutendexLanguage) {
        this.gutendexLanguage = gutendexLanguage;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}

