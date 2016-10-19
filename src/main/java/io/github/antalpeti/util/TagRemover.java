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
   * Remove the bold, italic and font with color attribute open and close tag(s).
   * 
   * @param text the input text
   * @return text without the tag(s)
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
   * Check there is a bold or italic open tag.
   * 
   * @param text the input text
   * @return true, if it has
   */
  private boolean hasBoldItalicOpenTag(String text) {
    return text.length() > 3 && text.matches(".*(<i>|<b>)+.*");
  }

  /**
   * Remove the bold and italic open tag(s).
   * 
   * @param text the input text
   * @return text without the tag(s)
   */
  private String removeBoldItalicOpenTag(String text) {
    return text.replaceAll("(<i>|<b>)", TextConstant.EMPTY_STRING);
  }

  /**
   * Check there is a bold or italic close tag.
   * 
   * @param text the input text
   * @return true, if it has
   */
  private boolean hasBoldItalicCloseTag(String text) {
    return text.length() > 3 && text.matches(".*(<\\/i>|<\\/b>)+.*");
  }

  /**
   * Remove the bold and italic close tag(s).
   * 
   * @param text the input text
   * @return text without the tag(s)
   */
  private String removeBoldItalicCloseTag(String text) {
    return text.replaceAll("(<\\/i>|<\\/b>)", TextConstant.EMPTY_STRING);
  }

  /**
   * Check there is a font open tag with color attribute .
   * 
   * @param text the input text
   * @return true, if it has
   */
  private boolean hasFontColorOpenTag(String text) {
    return text.length() > 9 && text.matches(".*(<font color=.{9}>)+.*");
  }

  /**
   * Remove the font open tag(s) with color attribute .
   * 
   * @param text the input text
   * @return text without the tag(s)
   */
  private String removeFontColorOpenTag(String text) {
    return text.replaceAll("(<font color=.{9}>)", TextConstant.EMPTY_STRING);
  }

  /**
   * Check there is a font close tag.
   * 
   * @param text the input text
   * @return true, if it has
   */
  private boolean hasFontCloseTag(String text) {
    return text.length() > 3 && text.matches(".*(<\\/font>)+.*");
  }

  /**
   * Remove the font close tag(s).
   * 
   * @param text the input text
   * @return text without the tag(s)
   */
  private String removeFontCloseTag(String text) {
    return text.replaceAll("(<\\/font>)", TextConstant.EMPTY_STRING);
  }
}
