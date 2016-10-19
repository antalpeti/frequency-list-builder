package io.github.antalpeti.util;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import io.github.antalpeti.constant.DirectionOrder;
import io.github.antalpeti.file.WordData;

public class Util {
  private static Util instance = null;

  private Util() {}

  public static Util getInstance() {
    if (instance == null) {
      instance = new Util();
    }
    return instance;
  }

  /**
   * Order a {@link Map} values ascending or decending order.
   * 
   * @param map the map
   * @param order an direction order
   * @return the ordered map according the direction order
   */
  public <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map,
      DirectionOrder orderDirection) {
    final int orderMultiplier = DirectionOrder.DESCENDING.equals(orderDirection) ? -1 : 1;
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
   * 
   * @param wordData
   */
  public void sortMap(WordData wordData) {
    int individualWordNumber = 0;;
    SortedSet<Entry<String, Integer>> entriesSortedByValues =
        Util.getInstance().entriesSortedByValues(wordData.getWordFrequency(), DirectionOrder.DESCENDING);
    for (Map.Entry<String, Integer> entry : entriesSortedByValues) {
      ++individualWordNumber;
      String key = entry.getKey();
      Integer value = entry.getValue();
      wordData.getContents().append(key + " " + value + "\n");
    }
    wordData.setIndividualWordNumber(individualWordNumber);
  }
}
