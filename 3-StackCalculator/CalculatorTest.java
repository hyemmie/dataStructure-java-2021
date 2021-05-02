import java.io.*;
import java.util.Stack;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorTest
{
	public static final String ERROR_MSG = "ERROR";

	// return operator's priority
	public static int priority(char ch) {
		switch (ch) {
			case '(' : return 0;
			case ')' : return 0;
			case '^' : return 4;
			case '~' : return 3;
			case '*' : return 2;
			case '/' : return 2;
			case '%' : return 2;
			case '+' : return 1;
			case '-' : return 1;
			default : return -1;
		}
	}

	// transform infix string to postfix string
	public static String infixToPostfix(String input) {
		Stack<Object> infixStack = new Stack<>();	
		StringBuilder sb = new StringBuilder();
		boolean digitPreviously = false;

		for (int i = 0; i < input.length(); i++) {
			char temp = input.charAt(i);

			// CASE 1 : handling digits
			if (priority(temp) < 0) {
				sb.append(temp);
				digitPreviously = true;
			// CASE 2 : handling (, )
			} else if (priority(temp) == 0) {
				if (temp == '(') {
					infixStack.push(temp);
					digitPreviously = false;	
				} else {
					boolean foundOpener = false;
					while (!infixStack.empty()) {
						if ((char)infixStack.peek() == '(') {
							foundOpener = true;
							infixStack.pop();
							break;
						} 
						char top = (char)infixStack.pop();
						if (digitPreviously) sb.append(' ');
						sb.append(top);
					}
					digitPreviously = true;	
					if (!foundOpener) {
						// invalid case : different number of ( and )
						throw new IllegalArgumentException();
					}
				}
			}
			// CASE 3 : handling operators
			else if (priority(temp) > 0) {
				if (infixStack.empty()) {
					if (!digitPreviously && temp == '-') {
						temp = '~';
					}
					infixStack.push(temp);
					if (digitPreviously) sb.append(' ');
				} else {
					while (!infixStack.empty()) {
						if (!digitPreviously && temp == '-') {
							temp = '~';
						}
						if (priority((char)infixStack.peek()) < priority(temp) || temp == '^' || temp == '~') {
							break;
						}
						char top = (char)infixStack.pop();
						if (digitPreviously) sb.append(' ');
						sb.append(top);
					}
					infixStack.push(temp);
					if (digitPreviously) sb.append(' ');
				}
				digitPreviously = false;
			}
		}

		// pop remaining chars
		while (!infixStack.empty()) {
			char temp = (char)infixStack.pop();
			if (temp == '(') {
				// invalid case : different number of ( and )
				throw new IllegalArgumentException();
			} else {
				sb.append(' ');
				sb.append(temp);
			}
		}

		return sb.toString();
	}

	// check invalid input
	public static void checkValidInput(String input) {

		// invalid case : devide zero or no operator 
		if (input.matches("^.*[0-9]\\s*[/%]\\s*[0].*$") || input.matches("^.*[0-9]\\s+[0-9].*$")) {
			throw new IllegalArgumentException();
    	}

	}

	// simple calculate method
	public static long calculate(long a, long b, char opr) {
		switch (opr) {
			case '+' : return a + b;
			case '-' : return a - b;
			case '*' : return a * b;
			case '/' : return a / b;
			case '%' : return a % b;
			case '^' : 
			// invalid case : negative power of zero
			if (a == 0 && b < 0) throw new IllegalArgumentException();
			else {
				return (long)Math.pow(a, b);
			}
			default : return -1;
		}
	}

	// calculate postfix string 
	// reference: stack class note 51 page sample code (evaluate)
	// some edits with reference
	public static long evaluate(String post) {
		long a, b;
		Stack<Long> tmpStack = new Stack<>();
		boolean digitPreviously = false;
		for (int i = 0; i < post.length(); i++) {
			char ch = post.charAt(i);
			// CASE 1 : ch is digit
			if (Character.isDigit(ch)) {
				if (digitPreviously) {
					long temp = tmpStack.pop();
					temp = 10 * temp + (long)(ch - '0');
					tmpStack.push(temp);
				} else {
					tmpStack.push((long)(ch - '0'));
				}
				digitPreviously = true;
				// CASE 2 : ch is operator
			} else if (priority(ch) > 0) {
				if (ch == '~') {
					a = tmpStack.pop();
					a *= -1;
					tmpStack.push(a);
				} else {
					a = tmpStack.pop();
					b = tmpStack.pop();
					long ret = calculate(b, a, ch);
					tmpStack.push(ret);
				}
				digitPreviously = false;
				// CASE 3 : ch is blank
			} else {
				digitPreviously = false;
			}
		}
		return tmpStack.pop();
	}


	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);

			}
			catch (Exception e)
			{
				System.out.println(ERROR_MSG);
			}
		}
	}

	private static void command(String input)
	{
		try {
			checkValidInput(input);
			String postfix;
			long result;
			postfix = infixToPostfix(input.replaceAll("\\s+",""));
			result = evaluate(postfix);

			System.out.println(postfix);
			System.out.println(result);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
}
