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

  private boolean hasBoldItalicOpenTag(String text) {
    return text.length() > 3 && text.matches(".*(<i>|<b>)+.*");
  }

  private String removeBoldItalicOpenTag(String text) {
    return text.replaceAll("(<i>|<b>)", TextConstant.EMPTY_STRING);
  }

  private boolean hasBoldItalicCloseTag(String text) {
    return text.length() > 3 && text.matches(".*(<\\/i>|<\\/b>)+.*");
  }

  private String removeBoldItalicCloseTag(String text) {
    return text.replaceAll("(<\\/i>|<\\/b>)", TextConstant.EMPTY_STRING);
  }

  private boolean hasFontColorOpenTag(String text) {
    return text.length() > 9 && text.matches(".*(<font color=.{9}>)+.*");
  }

  private String removeFontColorOpenTag(String text) {
    return text.replaceAll("(<font color=.{9}>)", TextConstant.EMPTY_STRING);
  }

  private boolean hasFontCloseTag(String text) {
    return text.length() > 3 && text.matches(".*(<\\/font>)+.*");
  }

  private String removeFontCloseTag(String text) {
    return text.replaceAll("(<\\/font>)", TextConstant.EMPTY_STRING);
  }
}
