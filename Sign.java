// This file contains the Sign enum, which represents the sign of a term in a differentiable function.
public enum Sign {
  // Define two possible enum values
  POSITIVE("+"),
  NEGATIVE("-");

  // Define a private String variable to store the sign
  private final String symbol;

  // Define a constructor to initialize the sign
  Sign(String symbol) {
    this.symbol = symbol;
  }

  // fromString method to convert a string to a Sign enum value
  public static Sign fromString(String text) {
    for (Sign s : Sign.values()) {
      if (s.symbol.equals(text)) {
        return s;
      }
    }
    // Throw an error if the string cannot be converted to an enum value
    throw new IllegalArgumentException("No constant with text " + text + " found");
  }

  // toString method to return the symbol of the enum value
  public String toString() {
    return symbol;
  }

  // Method to flip the sign for trigonometric differentiation
  public Sign flip() {
    if (this == POSITIVE) {
      return NEGATIVE;
    } else {
      return POSITIVE;
    }
  }

}