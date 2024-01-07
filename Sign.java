public enum Sign {
  POSITIVE("+"),
  NEGATIVE("-");

  private final String symbol;

  Sign(String symbol) {
    this.symbol = symbol;
  }

  public static Sign fromString(String text) {
    for (Sign s : Sign.values()) {
      if (s.symbol.equals(text)) {
        return s;
      }
    }
    throw new IllegalArgumentException("No constant with text " + text + " found");
  }
}