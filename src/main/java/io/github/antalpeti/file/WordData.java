package io.github.antalpeti.file;

import java.util.TreeMap;

public class WordData {
  private int processedWordNumber;
  private int individualWordNumber;
  private TreeMap<String, Integer> wordFrequency;
  private StringBuilder contents;

  public WordData() {
    processedWordNumber = 0;
    individualWordNumber = 0;
    wordFrequency = new TreeMap<>();
    contents = new StringBuilder();
  }

  /**
   * Create a summary message.
   * 
   * @return the created summary message
   */
  public String getAfterProcessMessage() {
    StringBuilder message = new StringBuilder();
    message.append("-----Summary-----");
    message.append("\n");
    message.append("Found individual words: ");
    message.append(individualWordNumber);
    message.append("\n");
    message.append("Processed words: ");
    message.append(processedWordNumber);
    return message.toString();
  }

  public int getProcessedWordNumber() {
    return processedWordNumber;
  }

  public void setProcessedWordNumber(int processedWordNumber) {
    this.processedWordNumber = processedWordNumber;
  }

  public int getIndividualWordNumber() {
    return individualWordNumber;
  }

  public void setIndividualWordNumber(int individualWordNumber) {
    this.individualWordNumber = individualWordNumber;
  }

  public TreeMap<String, Integer> getWordFrequency() {
    return wordFrequency;
  }

  public void setWordFrequency(TreeMap<String, Integer> wordFrequency) {
    this.wordFrequency = wordFrequency;
  }

  public StringBuilder getContents() {
    return contents;
  }

  public void setContents(StringBuilder contents) {
    this.contents = contents;
  }
}
