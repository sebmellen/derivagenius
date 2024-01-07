import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Differentiator {

  // This is a method to differentiate a single mathematical term from a calculus
  // equation
  public static DifferentiableTerm differentiateTerm(DifferentiableTerm term) {

    // Extract the string representation and sign of the term
    String termStr = term.getTerm();
    Sign sign = term.getSign();

    // The order of operations here is:
    // 1) check for trigonometric identities,
    // 2) check for Euler's number (e),
    // 3) check for power identities,
    // 4) check for cases where the term is only a constant

    // First, handle trigonometric functions (sin, cos, tan)
    // The regex checks if the term contains sin(x), cos(x), or tan(x)
    if (termStr.matches(".*sin\\(x\\).*") || termStr.matches(".*cos\\(x\\).*") || termStr.matches(".*tan\\(x\\).*")) {
      // If there is a match, delegate to a method that is made to handle
      // trigonometric differentiation
      return differentiateTrigonometric(term);
    }

    // Use a regex to find terms with Euler's number and handle them
    // The regex exactly matches the string "e^x" in the term
    if (termStr.matches("e\\^x")) {
      // The derivative of e^x is e^x, so return the term as-is
      return new DifferentiableTerm(sign, "e^x");
    }

    // Regex to handle power terms (in the format ax^b)
    // —————
    // Explanation:
    // ——————————> ([\\d.]+)? | This captures a group of one or more digits or a
    // decimal point
    // (which is optional). This is the coefficient of x (like '2.5' in '2.5x').
    // —————
    // ——————————> x | This matches the literal character 'x'.
    // —————
    // ——————————> (\\^([-\\d]+))? | This is a group that matches the
    // power of x, if it exists. '\\^' matches the literal '^'. '([-\\d]+)' captures
    // a group of one or more digits or a minus sign, allowing for negative powers
    // (like '3' in 'x^3' or '-2' in 'x^-2'). So the regex effectively matches
    // expresions like '2.5x', 'x^3', '3x^-2', or even just 'x' (which is handled
    // above).
    Matcher matcher = Pattern.compile("([\\d.]+)?x(\\^([-\\d]+))?").matcher(termStr);
    if (matcher.find()) {
      // Parse the coefficient, defaulting to 1.0 if not present
      double coefficient = matcher.group(1) != null ? Double.parseDouble(matcher.group(1)) : 1.0;
      // Parse the power, defaulting to 1 if not present
      int power = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : 1;
      // Delegate to a method that applies the power rule for differentiation
      return differentiatePowerTerm(sign, coefficient, power);
    }

    // Finally, handle constants (numbers without variables)
    // The regex matches a string that only contains numbers and decimal points
    if (termStr.matches("[\\d.]+")) {
      // The derivative of a constant is 0
      return new DifferentiableTerm(sign, "0");
    }

    // If none of the above cases apply, the term is not supported for
    // differentiation, so we throw an error
    throw new IllegalArgumentException("Unsupported term for differentiation");
  }

  // Method to differentiate trigonometric functions
  private static DifferentiableTerm differentiateTrigonometric(DifferentiableTerm term) {

    // Extract the string representation and sign of the term
    String termStr = term.getTerm();
    Sign sign = term.getSign();

    // This regex captures a coefficient (group 1) and the trig function
    /// group 2)
    Matcher matcher = Pattern.compile("([\\d.]+)?(sin|cos|tan)\\(x\\)").matcher(termStr);
    if (matcher.find()) {
      // Parse the coefficient, defaulting to 1.0 if not present
      double coefficient = matcher.group(1) != null ? Double.parseDouble(matcher.group(1)) : 1.0;
      // Extract the trigonometric function
      String trigFunc = matcher.group(2);

      // Initialize a variable to hold the differentiated term
      String differentiatedTerm;

      // Apply specific differentiation rules based on the trig function
      switch (trigFunc) {
        case "sin":
          differentiatedTerm = "cos(x)"; // Derivative of sin(x) is cos(x)
          break;
        case "cos":
          differentiatedTerm = "-sin(x)"; // Derivative of cos(x) is -sin(x)
          break;
        case "tan":
          differentiatedTerm = "sec(x)^2"; // Derivative of tan(x) is sec(x)^2
          break;
        default:
          // This case should never be reached due to the regex pattern but I included
          // it just in case
          throw new IllegalArgumentException("Unsupported trigonometric function");
      }

      // If there was a coefficient, it needs to be multiplied with the differentiated
      // term
      if (coefficient != 1.0) {
        differentiatedTerm = coefficient + differentiatedTerm;
      }

      // Return the new DifferentiableTerm with the correct sign and
      // differentiated term
      return new DifferentiableTerm(sign, differentiatedTerm);
    }

    // If the regex does not match, then the format of the term isn't valid
    throw new IllegalArgumentException("Invalid trigonometric term");
  }

  // Method to apply the power rule for the differentiation of a polynomial term
  private static DifferentiableTerm differentiatePowerTerm(Sign sign, double coefficient, int power) {

    // Handle the special case where the power is 1
    if (power == 1) {
      // The derivative of ax is just the coefficient a
      return new DifferentiableTerm(sign, formatCoefficient(coefficient));
    } else {
      // Apply the power rule: d/dx[ax^n] = n*ax^(n-1)
      double newCoefficient = coefficient * power;
      int newPower = power - 1;

      // Format the new term with the updated coefficient and power
      String formattedCoefficient = formatCoefficient(newCoefficient);
      String newTerm = formattedCoefficient + "x" + (newPower == 1 ? "" : "^" + newPower);

      // Return the new DifferentiableTerm with the correct sign and
      // differentiated term
      return new DifferentiableTerm(sign, newTerm);
    }
  }

  // Method to format a coefficient for output
  private static String formatCoefficient(double coefficient) {
    // Check if the coefficient is an integer value by comparing the coefficient to
    // its casted integer value. If the coefficient is 3.0, casting it to a long
    // will result in 3.
    // Since 3.0 == 3 is true, we know the original number was an integer.
    // However, if the coefficient is 3.5, casting it to a long will result in 3.
    // Since 3.5 == 3 is false, we know the original number was not an integer.
    if (coefficient == (long) coefficient) {
      // If it's an integer, format as an integer (without decimal part).
      // '%d' is the format specifier for a long (integer) in Java's String.format
      // method.
      return String.format("%d", (long) coefficient);
    } else {
      // If it's not an integer, format as a floating-point number.
      // '%s' is the format specifier for a string, which will convert the
      // floating-point number to a string without changing its value or format.
      return String.format("%s", coefficient);
    }
  }
}
