public class DifferentiableTerm {
  private Sign sign; // Using the Sign enum
  private String term;

  // Constructor
  public DifferentiableTerm(Sign sign, String term) {
    this.sign = sign;
    this.term = term;
  }

  // Getters
  public Sign getSign() {
    return sign;
  }

  public String getTerm() {
    return term;
  }

  // Overridden toString method for easy printing
  @Override
  public String toString() {
    return String.format("{\"sign\":\"%s\", \"term\":\"%s\"}", sign, term);
  }
}