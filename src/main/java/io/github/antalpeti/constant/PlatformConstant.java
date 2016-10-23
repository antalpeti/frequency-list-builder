package io.github.antalpeti.constant;

public enum PlatformConstant {
  WIN32("win32"), ROOT_DIRECTORY("/"), ROOT_DIRECTORY_WIN32("c:\\");

  private final String value;

  PlatformConstant(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
