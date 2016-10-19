package io.github.antalpeti.util;

import io.github.antalpeti.constant.TextConstant;

public class TagRemover {
  private static TagRemover instance = null;

  private TagRemover() {}

  public static TagRemover getInstance() {
    if (instance == null) {
      instance = new TagRemover();
    }
    return instance;
  }

  /**
   * 
   * @param text
   * @return
   */
  public String removeBoldItalicFontOpenAndCloseTags(String text) {
    while (hasBoldItalicOpenTag(text) || hasBoldItalicCloseTag(text) || hasFontColorOpenTag(text)
        || hasFontCloseTag(text)) {
      text = hasBoldItalicOpenTag(text) ? removeBoldItalicOpenTag(text) : text;
      text = hasBoldItalicCloseTag(text) ? removeBoldItalicCloseTag(text) : text;
      text = hasFontColorOpenTag(text) ? removeFontColorOpenTag(text) : text;
      text = hasFontCloseTag(text) ? removeFontCloseTag(text) : text;
    }
    return text;
  }

  /**
   * 
   * @param text
   * @return
   */
  private boolean hasBoldItalicOpenTag(String text) {
    return text.length() > 3 && text.matches(".*(<i>|<b>)+.*");
  }

  /**
   * 
   * @param text
   * @return
   */
  private String removeBoldItalicOpenTag(String text) {
    return text.replaceAll("(<i>|<b>)", TextConstant.EMPTY_STRING);
  }

  /**
   * 
   * @param text
   * @return
   */
  private boolean hasBoldItalicCloseTag(String text) {
    return text.length() > 3 && text.matches(".*(<\\/i>|<\\/b>)+.*");
  }

  /**
   * 
   * @param text
   * @return
   */
  private String removeBoldItalicCloseTag(String text) {
    return text.replaceAll("(<\\/i>|<\\/b>)", TextConstant.EMPTY_STRING);
  }

  /**
   * 
   * @param text
   * @return
   */
  private boolean hasFontColorOpenTag(String text) {
    return text.length() > 9 && text.matches(".*(<font color=.{9}>)+.*");
  }

  /**
   * 
   * @param text
   * @return
   */
  private String removeFontColorOpenTag(String text) {
    return text.replaceAll("(<font color=.{9}>)", TextConstant.EMPTY_STRING);
  }

  /**
   * 
   * @param text
   * @return
   */
  private boolean hasFontCloseTag(String text) {
    return text.length() > 3 && text.matches(".*(<\\/font>)+.*");
  }

  /**
   * 
   * @param text
   * @return
   */
  private String removeFontCloseTag(String text) {
    return text.replaceAll("(<\\/font>)", TextConstant.EMPTY_STRING);
  }
}
