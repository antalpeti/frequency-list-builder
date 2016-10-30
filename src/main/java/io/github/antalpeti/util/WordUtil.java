package io.github.antalpeti.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.swt.widgets.Text;

import io.github.antalpeti.constant.DirectionOrderConstant;
import io.github.antalpeti.file.WordData;

public class WordUtil {
  private static WordUtil instance = null;

  private WordUtil() {}

  public static WordUtil getInstance() {
    if (instance == null) {
      instance = new WordUtil();
    }
    return instance;
  }

  /**
   * Sort the words according values descending or ascending orders and count individual words.
   * 
   * @param wordData the input words
   * @param directionOrder ascending or descending order and numbers of individual words
   */
  public void createContents(WordData wordData, DirectionOrderConstant directionOrder) {
    int individualWordNumber = 0;;
    SortedSet<Entry<String, Integer>> entriesSortedByValues =
        WordUtil.getInstance().entriesSortedByValues(wordData.getWordFrequency(), directionOrder);
    for (Map.Entry<String, Integer> entry : entriesSortedByValues) {
      ++individualWordNumber;
      String key = entry.getKey();
      Integer value = entry.getValue();
      wordData.getContents().append(key + " " + value + "\n");
    }
    wordData.setIndividualWordNumber(individualWordNumber);
  }

  /**
   * Order a {@link Map} values ascending or decending order.
   * 
   * @param map the map
   * @param order an direction order
   * @return the ordered map according the direction order
   */
  public <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map,
      DirectionOrderConstant orderDirection) {
    final int orderMultiplier = DirectionOrderConstant.DESCENDING.equals(orderDirection) ? -1 : 1;
    SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(new Comparator<Map.Entry<K, V>>() {
      @Override
      public int compare(Map.Entry<K, V> entry1, Map.Entry<K, V> entry2) {
        int result = entry1.getValue().compareTo(entry2.getValue());
        return result != 0 ? orderMultiplier * result : 1;
      }
    });
    sortedEntries.addAll(map.entrySet());
    return sortedEntries;
  }

  /**
   * Process content of files.
   * 
   * @param directoryPath the selected directory path
   * @param fileNames the selected file(s)
   * @param wordData the connected word data
   * @return return the processed content
   */
  public WordData processFiles(String directoryPath, String[] fileNames, WordData wordData, Text log) {
    String separator = File.separator;
    if (fileNames.length != 0) {

      for (String filename : fileNames) {
        String pathname = directoryPath + separator + filename;
        processFile(pathname, wordData, log);
      }
      WordUtil.getInstance().createContents(wordData, DirectionOrderConstant.DESCENDING);
    }
    return wordData;
  }

  /**
   * Processed the file content and collect individual words. Also count the processed words.
   * 
   * @param pathname the file path
   * @param wordData the word data
   * @param log the log view
   */
  private void processFile(String pathname, WordData wordData, Text log) {
    ControlUtil.getInstance().addLogMessage(log, "Actual file: " + pathname);
    ControlUtil.getInstance().addLogMessage(log, "File process started.");
    File file = null;
    try {
      file = new File(pathname);
      try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
        String line;
        int startProcessedWordNumber = wordData.getProcessedWordNumber();

        TagRemover tagRemover = TagRemover.getInstance();
        CharacterRemover characterRemover = CharacterRemover.getInstance();

        while ((line = br.readLine()) != null) {
          line = line.trim();

          boolean emptyLine = line.isEmpty();
          boolean srtTimeLine = line.matches("[\\d:,\\s->]*");

          if (!emptyLine && !srtTimeLine) {
            line = line.toLowerCase();
            line = tagRemover.removeBoldItalicFontOpenAndCloseTags(line);

            String[] words = line.split("[\\s,;:\\.]");
            for (String word : words) {
              word = tagRemover.removeBoldItalicFontOpenAndCloseTags(word);

              word = characterRemover.removeFrontAndBackNonLetterCharacters(word);

              if (word.isEmpty()) {
                continue;
              }
              Integer occurence = wordData.getWordFrequency().get(word);
              if (occurence == null) {
                occurence = new Integer(0);
              }
              wordData.setProcessedWordNumber(wordData.getProcessedWordNumber() + 1);
              wordData.getWordFrequency().put(word, ++occurence);
            }
          }
        }
        int processedWordNumber = wordData.getProcessedWordNumber() - startProcessedWordNumber;
        ControlUtil.getInstance().addLogMessage(log, "File process ended.");
        ControlUtil.getInstance().addLogMessage(log, "Processed words: " + processedWordNumber);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
