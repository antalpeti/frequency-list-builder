package io.github.antalpeti.util;

public class WordPartRemover {
  private static WordPartRemover instance = null;

  private WordPartRemover() {}

  public static WordPartRemover getInstance() {
    if (instance == null) {
      instance = new WordPartRemover();
    }
    return instance;
  }

  /**
   * Remove the word part after the first apostrophe.
   * 
   * @param word the word
   * @return a truncated word
   */
  public String removeAfterFirstApostrophe(String word) {
    int firstApostropheIndex = word.indexOf("'");

    if (firstApostropheIndex != -1) {
      word = word.substring(0, firstApostropheIndex);
    }

    return word;
  }
}
