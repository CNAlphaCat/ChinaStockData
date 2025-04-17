package cn.alphacat.chinastockdata.enums;

public enum KLineType {
  DAILY(1),
  WEEKLY(2),
  MONTHLY(3);

  private final int value;

  KLineType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
