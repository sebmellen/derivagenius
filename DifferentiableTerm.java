// This file contains the DifferentiableTerm class, which represents a single term in a differentiable function.
public class DifferentiableTerm {
  private Sign sign; // Using the Sign enum
  private String term;

  // Constructor
  public DifferentiableTerm(Sign sign, String term) {
    this.sign = sign;
    this.term = term;
  }

  // Getters

  // Return the sign as a Sign
  public Sign getSign() {
    return sign;
  }

  // Return the sign as a string
  public String getSignString() {
    return sign.toString();
  }

  // Return the term as a string
  public String getTerm() {
    return term;
  }
}