package io.github.antalpeti.constant;

public enum DirectionOrder {
  ASCENDING("asc"), DESCENDING("desc");

  private final String direction;

  DirectionOrder(String order) {
    this.direction = order;
  }
}
