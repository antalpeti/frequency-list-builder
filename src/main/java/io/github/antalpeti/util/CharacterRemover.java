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
   * Remove front and back non letter characters form the word.
   * 
   * @param word the word
   * @return a filtered word
   */
  public String removeFrontAndBackNonLetterCharacters(String word) {
    word = removeFrontNonLetterCharacters(word);
    word = removeBackNonLetterCharacters(word);
    return word;
  }

  /**
   * Remove front non letter characters form the word.
   * 
   * @param word the word
   * @return a filtered word
   */
  public String removeFrontNonLetterCharacters(String word) {
    while (word.length() > 0 && word.matches("^[^a-zA-Z]+.*")) {
      word = word.substring(1);
    }
    return word;
  }

  /**
   * Remove back non letter characters form the word.
   * 
   * @param word the word
   * @return a filtered word
   */
  public String removeBackNonLetterCharacters(String word) {
    while (word.length() > 0 && word.matches(".*[^a-zA-Z]+$")) {
      word = word.substring(0, word.length() - 1);
    }
    return word;
  }
}
