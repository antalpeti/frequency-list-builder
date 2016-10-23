package io.github.antalpeti.constant;

/**
 * Constants related of the ordering.
 */
public enum DirectionOrderConstant {
  ASCENDING("asc"), DESCENDING("desc");

  private final String value;

  DirectionOrderConstant(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
