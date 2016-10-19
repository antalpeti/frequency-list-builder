package io.github.antalpeti.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import io.github.antalpeti.constant.DirectionOrder;
import io.github.antalpeti.util.CharacterRemover;
import io.github.antalpeti.util.TagRemover;
import io.github.antalpeti.util.Util;

public class FileHandler {
  private static FileHandler instance = null;

  private FileHandler() {}

  public static FileHandler getInstance() {
    if (instance == null) {
      instance = new FileHandler();
    }
    return instance;
  }

  /**
   * Process content of files.
   * 
   * @param directoryPath the selected directory path
   * @param fileNames the selected file(s)
   * @param wordData the connected word data
   * @return return the processed content
   */
  public WordData processFiles(String directoryPath, String[] fileNames, WordData wordData) {
    String separator = File.separator;
    if (fileNames.length != 0) {

      for (String filename : fileNames) {
        String pathname = directoryPath + separator + filename;
        collectFileIndividualWords(pathname, wordData);
      }
      Util.getInstance().sortWords(wordData, DirectionOrder.ASCENDING);
    }
    return wordData;
  }

  /**
   * 
   * @param pathname
   * @param wordData
   */
  private void collectFileIndividualWords(String pathname, WordData wordData) {
    File file = null;
    try {
      file = new File(pathname);

      try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
        String line;

        TagRemover tagRemover = TagRemover.getInstance();
        CharacterRemover characterRemover = CharacterRemover.getInstance();

        while ((line = br.readLine()) != null) {
          line = line.trim();

          boolean emptyLine = line.isEmpty();
          boolean srtTimeLine = line.matches("[\\d:,\\s->]*");

          if (!emptyLine && !srtTimeLine) {
            line = line.toLowerCase();
            line = tagRemover.removeBoldItalicFontOpenAndCloseTags(line);

            String[] words = line.split("\\s");
            for (String word : words) {
              word = tagRemover.removeBoldItalicFontOpenAndCloseTags(word);

              word = characterRemover.removeFrontAndBackSpecialCharacters(word);

              word = characterRemover.removeOnlyHypenCharacter(word);

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
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
