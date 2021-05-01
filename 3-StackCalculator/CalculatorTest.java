import java.io.*;
import java.util.Stack;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorTest
{

	public static final String ERROR_MSG = "ERROR";
  
	// public static final Pattern DEVIDE_ZERO = Pattern.compile("*[0-9][/%]0");

    // implement this
    // public static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\s*([+-])?\\s*([0-9]+)\\s*([-+*])\\s*([+-])?\\s*([0-9]+)\\s*");


	public static int priority(char ch) {
		switch (ch) {
			case '(' : return 0;
			case ')' : return 0;
			case '^' : return 1;
			case '~' : return 2;
			case '*' : return 3;
			case '/' : return 3;
			case '%' : return 3;
			case '+' : return 4;
			case '-' : return 4;
			default : return -1;
		}
	}

	public static String infixToPostfix(String input) {
		Stack<Object> infixStack = new Stack<>();	
		StringBuilder sb = new StringBuilder();
		// boolean oprPreviously = false;
		boolean digitPreviously = false;

		for (int i = 0; i < input.length(); i++) {
			char temp = input.charAt(i);
			boolean checkEquality = temp == '^' || temp == '~';

			// handling digits
			if (priority(temp) < 0) {
				// oprPreviously = false;
				sb.append(temp);
				digitPreviously = true;
			// handling (, )
			} else if (priority(temp) == 0) {
				// oprPreviously = false;
				if (temp == '(') {
					infixStack.push(temp);
				} else {
					while (!infixStack.empty()) {
						if ((char)infixStack.peek() == '(') {
							// System.out.println(ERROR_MSG);
							System.out.println(infixStack.peek());
							infixStack.pop();
							System.out.println(infixStack.peek());

							break;
						} 
						char top = (char)infixStack.pop();
						if (digitPreviously) sb.append(' ');
						sb.append(top);
					}
				}
				digitPreviously = false;	
			}
			// handling operators
			else if (priority(temp) > 0) {
				if (infixStack.empty()) {
					// if (sb.toString().length() == 0 && temp == '-') {
					if (!digitPreviously && temp == '-') {
						temp = '~';
					}
					infixStack.push(temp);
					if (digitPreviously) sb.append(' ');
				} else {
					while (true) {
						// if (temp == '-' && oprPreviously) {
						if (temp == '-' && !digitPreviously) {
							temp = '~';
						}
						// if (priority((char)infixStack.peek()) > priority(temp) || ((char)infixStack.peek() == temp && checkEquality)) {
						// if (priority((char)infixStack.peek()) > priority(temp) || !digitPreviously || checkEquality) {
						if (priority((char)infixStack.peek()) > priority(temp) || !digitPreviously || checkEquality) {
							break;
						}
						char top = (char)infixStack.pop();
						// if (sb.toString().charAt(sb.toString().length()-1) != ' ') sb.append(' ');
						if (digitPreviously) sb.append(' ');
						sb.append(top);
						if (infixStack.empty()) {
							break;
						}
					}
					infixStack.push(temp);
					if (digitPreviously) sb.append(' ');
				}
				// oprPreviously = true;
				digitPreviously = false;
			}
		}

		// pop remaining char
		while (true) {
			if (infixStack.empty()) {
				break;
			}
			sb.append(' ');
			sb.append(infixStack.pop());
		}

		return sb.toString();
	}

	public static void checkValidInput(String input) {

		// Matcher match = DEVIDE_ZERO.matcher(input);

        // if (!input.matches("*[0-9][/%]0")) {
    	// 	throw new IllegalArgumentException();
    	// }

	}

	public static long calculate(long a, long b, char opr) {
		switch (opr) {
			case '+' : return a + b;
			case '-' : return a - b;
			case '*' : return a * b;
			case '/' : return a / b;
			case '%' : return a % b;
			case '^' : return (long)Math.pow(a, b);
			default : return -1;
		}
	}

	// reference: stack class note 51 page sample code
	public static long evaluate(String post) {
		long a, b;
		Stack<Long> tmpStack = new Stack<>();
		boolean digitPreviously = false;
		for (int i = 0; i < post.length(); i++) {
			char ch = post.charAt(i);
			// ch is digit
			if (Character.isDigit(ch)) {
				if (digitPreviously) {
					long temp = tmpStack.pop();
					temp = 10 * temp + (long)(ch - '0');
					tmpStack.push(temp);
				} else {
					tmpStack.push((long)(ch - '0'));
				}
				digitPreviously = true;
				// ch is operator
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
				// ch is blank
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
				// String postfix;
				// long result;
				// postfix = infixToPostfix(input.replaceAll("\\s+",""));
				// result = evaluate(postfix);
				// System.out.println(postfix);
				// System.out.println(result);

			}
			catch (Exception e)
			{
				// System.out.println(ERROR_MSG);
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input)
	{
		// checkValidInput(input);
		String postfix;
		postfix = infixToPostfix(input.replaceAll("\\s+",""));
		System.out.println(postfix);
		System.out.println(evaluate(postfix));
		// TODO : 아래 문장을 삭제하고 구현해라.
		// System.out.println("<< command 함수에서 " + input + " 명령을 처리할 예정입니다 >>");
	}
}
