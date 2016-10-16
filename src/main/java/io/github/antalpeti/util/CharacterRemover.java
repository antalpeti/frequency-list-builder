package io.github.antalpeti.util;

public class CharacterRemover {
  private static CharacterRemover instance = null;

  private CharacterRemover() {}

  public static CharacterRemover getInstance() {
    if (instance == null) {
      instance = new CharacterRemover();
    }
    return instance;
  }

  public String removeFrontAndBackSpecialCharacters(String word) {
    word = removeFrontSpecialCharacters(word);
    word = removeBackSpecialCharacters(word);
    return word;
  }


  public String removeFrontSpecialCharacters(String word) {
    while (word.length() > 0 && word.matches("^[\"\\(\\[\\{]+.*")) {
      word = word.substring(1);
    }
    return word;
  }

  public String removeBackSpecialCharacters(String word) {
    while (word.length() > 0 && word.matches(".*[\"\\)\\]\\}\\.,\\?!:$]+$")) {
      word = word.substring(0, word.length() - 1);
    }
    return word;
  }

  public String removeOnlyHypenCharacter(String word) {
    if (word.length() > 0 && word.matches("-+")) {
      word = word.replaceAll("-", "");
    }
    return word;
  }
}
