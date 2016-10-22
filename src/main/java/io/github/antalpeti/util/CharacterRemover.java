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

  /**
   * Remove front and back special characters form the word.
   * 
   * @param word the word
   * @return a filtered word
   */
  public String removeFrontAndBackSpecialCharacters(String word) {
    word = removeFrontSpecialCharacters(word);
    word = removeBackSpecialCharacters(word);
    return word;
  }

  /**
   * Remove front special characters form the word.
   * 
   * @param word the word
   * @return a filtered word
   */
  public String removeFrontSpecialCharacters(String word) {
    while (word.length() > 0 && word.matches("^[\"\\(\\[\\{]+.*")) {
      word = word.substring(1);
    }
    return word;
  }

  /**
   * Remove back special characters form the word.
   * 
   * @param word the word
   * @return a filtered word
   */
  public String removeBackSpecialCharacters(String word) {
    while (word.length() > 0 && word.matches(".*[\"\\)\\]\\}\\.,\\?!:$]+$")) {
      word = word.substring(0, word.length() - 1);
    }
    return word;
  }

  /**
   * Remove only hypen character form the word.
   * 
   * @param word the word
   * @return a filtered word
   */
  public String removeOnlyHypenCharacter(String word) {
    if (word.length() > 0 && word.matches("-+")) {
      word = word.replaceAll("-", "");
    }
    return word;
  }

  /**
   * Remove the fornt hypen character form the word.
   * 
   * @param word the word
   * @return a filtered word
   */
  public String removeFrontHypenCharacter(String word) {
    if (word.length() > 0 && word.matches("^-.*")) {
      word = word.replace("-", "");
    }
    return word;
  }
}
