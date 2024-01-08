
// This file contains the main method, as well as helper functions to validate and parse user input. The main method prints instructions, takes a user's function, validates it, parses it into a list of differentiable terms, and then differentiates each term. The differentiated terms are then printed to the console.
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    boolean rerun;

    // Print instructions
    printInstructions();

    // Loop until the user decides to exit
    do {
      String validatedInput = null;

      // Loop until the user enters a valid function
      while (validatedInput == null) {
        // Prompt the user for input
        System.out.println("➡ Please enter your function below:\n");
        // Get user input using scanner
        String input = scanner.nextLine();

        try {
          validatedInput = validateStringInput(input);
        } catch (IllegalArgumentException e) {
          System.out.println("\n▕! " + e.getMessage());
          // Inform the user to try again
          System.out.println("▕! Please try again.\n");
        }
      }

      System.out.println("\n▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁");
      // Artificially delay the program using the safeSleep method
      // so that the user gets the experience of calculation
      // requiring effort
      safeSleep(100);
      System.out.println("\n▞ You entered:\n  " + validatedInput);
      safeSleep(350);
      System.out.println("\n▚ Now calculating to find:\n  f'(x) for " + validatedInput + "\n");

      // Parse the validated input string into a list of differentiable terms,
      // and then differentiate each term
      ArrayList<DifferentiableTerm> differentiatedItems = differentiateTerms(parseStringInput(validatedInput));

      safeSleep(350);
      System.out.println("▞ DerivaGenius found f'(x) for your function!\n");
      safeSleep(350);
      System.out.println("▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔\n➡ Result:\n" + "  " +
      // Call the function to assemble the differentiated terms and print the result
          assembleDifferentiatedFunction(differentiatedItems));

      System.out.println("░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░\n\n➡ Do you want to run again? (yes/no): ");
      String response = scanner.nextLine().trim().toLowerCase();
      rerun = response.equals("yes");
    } while (rerun);
    scanner.close();
    System.out.println("Program ended.");
  }

  // This method is used to print instructions to the user when the program
  // launches. This is isolated into a separate method to make the main method
  // easier to read.
  public static void printInstructions() {
    System.out.println("░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░\n");
    System.out.println(
        "INTRODUCTION:\n  Welcome to DerivaGenius, a Java program that differentiates simple calculus functions.\n");
    System.out.println("DISCLAIMER:\n  This is an experimental program. No warranties are made or implied.\n");
    System.out.println("INSTRUCTIONS:");
    System.out
        .println("  1. The only allowed input characters are f, (, ), x, e, sin, cos, tan, 0-9, +, -, =, ^, −, and .");
    System.out.println(
        "  2. Your input must be in the format \"f(x) = <FUNCTION>\" to be valid. For example: \"f(x) = 4x^3 - 2x^2 + 5x - 7\" would be valid input, but \"4x^3 - 2x^2 + 5x - 7\" would not be.");
    System.out.println(
        "  3. Terms in your function must be separated with + or -.");
    System.out.println(
        "  4. DerivaGenius cannot differentiate functions that require the chain rule, product rule, quotient rule, or other advanced calculus techniques to differentiate.");
    System.out.println("  5. DerivaGenius can only support the following term structures:");
    System.out.println("    a. sin(x), cos(x), tan(x)");
    System.out.println("    b. <COEFFICIENT>x^<POWER> (e.g. 2x^3)");
    System.out.println("    c. e^x");
    System.out.println("    d. <CONSTANT> (e.g. 21)");
    System.out.println("\n░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░\n");
  }

  // This method is used to validate the user's input.
  // It checks if the input is valid using a regex.
  public static String validateStringInput(String input) throws IllegalArgumentException {
    // Regex pattern for allowed characters and sequences
    Pattern pattern = Pattern.compile("^[f()xe0-9+\\-=\\^−.sincota\\s]+$");

    // Check if the input contains 'f(x) ='
    if (!input.startsWith("f(x) =")) {
      throw new IllegalArgumentException("The input must start with 'f(x) ='.");
    }

    // Split the input into individual elements for validation
    char[] characters = input.toCharArray();

    // Validate each element
    for (char character : characters) {
      Matcher matcher = pattern.matcher(Character.toString(character));
      if (!matcher.matches()) {
        throw new IllegalArgumentException("Disallowed character found: " + character);
      }
    }

    // If input passes the checks, return it
    return input;
  }

  // This method is used to parse the user's input into a list of
  // differentiable terms.
  public static ArrayList<DifferentiableTerm> parseStringInput(String functionToDifferentiate) {
    // Remove 'f(x)=' and trim whitespace
    String processed = functionToDifferentiate.replace("f(x) =", "").trim();

    // Handling the case where the string starts
    // with a positive term without a + sign
    if (!processed.startsWith("-")) {
      processed = "+" + processed;
    }

    // Split the string at + or - (keeping the sign with the term)
    ArrayList<DifferentiableTerm> items = new ArrayList<>();
    Pattern pattern = Pattern.compile("([+-][^+-]+)");
    Matcher matcher = pattern.matcher(processed);

    while (matcher.find()) {
      String match = matcher.group(1).trim();
      Sign sign = Sign.fromString(match.substring(0, 1));
      String term = match.substring(1).trim();

      items.add(new DifferentiableTerm(sign, term));
    }

    return items;
  }

  // This method is used to differentiate each term in the list of
  // differentiable terms.
  public static ArrayList<DifferentiableTerm> differentiateTerms(ArrayList<DifferentiableTerm> items) {
    ArrayList<DifferentiableTerm> differentiatedItems = new ArrayList<>();
    for (DifferentiableTerm term : items) {
      DifferentiableTerm differentiatedTerm = Differentiator.differentiateTerm(term);
      differentiatedItems.add(differentiatedTerm);
    }

    return differentiatedItems;
  }

  // This method is used to assemble the differentiated terms into a
  // single string.
  private static String assembleDifferentiatedFunction(ArrayList<DifferentiableTerm> differentiatedItems) {
    StringBuilder assembledFunction = new StringBuilder();
    for (DifferentiableTerm term : differentiatedItems) {
      String termStr = term.getTerm();
      String signStr = term.getSignString();

      // Skip terms with a "0" value
      if (!termStr.equals("0")) {
        // Convert terms like "1.0x" to "x" or "2.0" to "2"
        termStr = termStr.replaceAll("1.0x", "x").replaceAll("\\.0", "");

        // Append the term with its sign to the assembled string
        assembledFunction.append(signStr).append(" ").append(termStr).append(" ");
      }
    }

    String result = assembledFunction.toString().trim();

    if (result.trim().startsWith("+")) {
      result = "f'(x) =" + result.trim().substring(1);
      return result.toString();
    } else {
      result = "f'(x) = -" + result.trim().substring(2);
      return result.toString();
    }
  }

  // This is a small utility method to make the program wait for a specified
  // amount of time, mainly used to give the appearance of computation taking time
  // when writing print statements
  public static void safeSleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt(); // Preserve interrupt status
      // Optionally log the exception or handle it as needed
    }
  }

}
