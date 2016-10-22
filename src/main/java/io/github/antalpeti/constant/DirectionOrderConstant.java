package io.github.antalpeti.constant;

/**
 * Constants related of the ordering.
 */
public enum DirectionOrderConstant {
  ASCENDING("asc"), DESCENDING("desc");

  private final String direction;

  DirectionOrderConstant(String order) {
    this.direction = order;
  }
}
