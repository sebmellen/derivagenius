import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {
  public static void main(String[] args) {
    // Print instructions
    PrintInstructions();

    Scanner scanner = new Scanner(System.in);
    String validatedInput = null;

    while (validatedInput == null) {
      System.out.println("➡ Please enter your function below:\n");
      String input = scanner.nextLine();

      try {
        validatedInput = ValidateStringInput(input);
      } catch (IllegalArgumentException e) {
        System.out.println("\n▕! " + e.getMessage());
        // Inform the user to try again
        System.out.println("▕! Please try again.\n");
      }
    }

    scanner.close();

    System.out.println("\n▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁");
    safeSleep(200);
    System.out.println("\n▞ You entered:\n  " + validatedInput);
    safeSleep(700);
    System.out.println("\n▚ Now calculating to find:\n  f'(x) for " + validatedInput + "\n");

    ArrayList<DifferentiableTerm> items = ParseStringInput(validatedInput);
    ArrayList<DifferentiableTerm> differentiatedItems = new ArrayList<>();
    for (DifferentiableTerm term : items) {
      DifferentiableTerm differentiatedTerm = Differentiator.differentiateTerm(term);
      differentiatedItems.add(differentiatedTerm);
    }

    safeSleep(700);
    System.out.println("▞ DerivaGenius found f'(x) for your function!\n");
    safeSleep(700);
    System.out.println("▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔");
    System.out.println("\n➡ Result:\n" + "  " +
    // Call the function to assembl the differentiated terms and print the result
        assembleDifferentiatedFunction(differentiatedItems));
  }

  private static String assembleDifferentiatedFunction(ArrayList<DifferentiableTerm> differentiatedItems) {
    StringBuilder assembledFunction = new StringBuilder();
    for (DifferentiableTerm term : differentiatedItems) {
      String termStr = term.getTerm();
      Sign sign = term.getSign();

      // Skip terms with a "0" value
      if (!termStr.equals("0")) {
        // Convert terms like "1.0x" to "x" or "2.0" to "2"
        termStr = termStr.replaceAll("1.0x", "x").replaceAll("\\.0", "");

        // Append the term with its sign to the assembled string
        assembledFunction.append(sign).append(" ").append(termStr).append(" ");
      }
    }

    if (assembledFunction.toString().trim().startsWith("+")) {
      assembledFunction.substring(1);
      assembledFunction.insert(0, "f'(x) = ");
      return assembledFunction.toString().trim();
    } else {
      assembledFunction.insert(0, "f'(x) = ");
      return assembledFunction.toString().trim();
    }
  }

  public static ArrayList<DifferentiableTerm> DifferentiateTerms(ArrayList<DifferentiableTerm> items) {
    ArrayList<DifferentiableTerm> differentiatedItems = new ArrayList<>();

    for (DifferentiableTerm term : items) {
      DifferentiableTerm differentiatedTerm = Differentiator.differentiateTerm(term);
      differentiatedItems.add(differentiatedTerm);
    }

    return differentiatedItems;
  }

  public static ArrayList<DifferentiableTerm> ParseStringInput(String functionToDifferentiate) {
    // Remove 'f(x)=' and trim whitespace
    String processed = functionToDifferentiate.replace("f(x) =", "").trim();

    // Handling the case where the string starts with a positive term without a +
    // sign
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

  public static String ValidateStringInput(String input) throws IllegalArgumentException {
    // Regex pattern for allowed characters and sequences
    Pattern pattern = Pattern.compile("^[f()xe0-9+\\-=\\^−.sincta\\s]+$");

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

  // This method is used to print instructions to the user when the program
  // launches
  public static void PrintInstructions() {
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
    System.out.println("    d. <CONSTANT> (e.g. 21)");
    System.out.println("\n░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░\n");
  }

  public static void safeSleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt(); // Preserve interrupt status
      // Optionally log the exception or handle it as needed
    }
  }

}
