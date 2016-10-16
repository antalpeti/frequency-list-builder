package io.github.antalpeti;

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

  public String getStatusText() {
    StringBuilder statusText = new StringBuilder();
    statusText.append("Found individual words: " + individualWordNumber);
    statusText.append("\nProcessed words: " + processedWordNumber);
    return statusText.toString();
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
