import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
  
  
public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "invalid input";
  
    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\s*([+-])?\\s*([0-9]+)\\s*([-+*])\\s*([+-])?\\s*([0-9]+)\\s*");

    private byte[] reversedNumber = new byte[101];
    private int numberLength = 0;
    private boolean isPositive = true;
  
    // constructor of caculation result array input
    public BigInteger(byte[] inputArray) {
        reversedNumber = inputArray;
    }

    // constructor of input string
    public BigInteger(String s) {
        numberLength = s.length();
        for (int i = s.length(); i > 0; i--) {
            byte digit = (byte)Character.getNumericValue(s.charAt(i - 1));
            reversedNumber[s.length() - i] = digit;
        }
    }

    public byte[] getArray() {
        return reversedNumber;
    }

    public int getLength() {
        return numberLength;
    }

    public boolean getIsPositive() {
        return isPositive;
    }

    public void setIsPositive(boolean input) {
        isPositive = input;
    }

    public boolean isInputLarger (BigInteger input) {
        if (numberLength > input.getLength()) {
            return false;
        } else if (numberLength < input.getLength()) {
            return true;
        } else {
            for (int i = numberLength - 1; i >= 0; i--) {
                if (input.getArray()[i] > reversedNumber[i]) {
                    return true;
                } else if (input.getArray()[i] < reversedNumber[i]) {
                    return false;
                }
            }
        }
        return false; 
    }
  
    public String add(BigInteger input) {
        int largerLength = isInputLarger(input) ? input.getArray().length : reversedNumber.length;
        byte[] addResult = new byte[102];
        for (int i = 0; i < largerLength; i++) {
            byte temp = (byte)(addResult[i] + input.getArray()[i] + reversedNumber[i]);
            addResult[i] = (byte)(temp % 10);
            if (temp >= 10) {
                addResult[i+1]++; 
            }
        }
        return convertString(addResult);
    }
  
    public String subtract(BigInteger input) {
        int largerLength = isInputLarger(input) ? input.getLength() : numberLength;
        byte[] largerArray = isInputLarger(input) ? input.getArray() : reversedNumber;
        byte[] smallerArray = isInputLarger(input) ? reversedNumber : input.getArray();
        byte[] subResult = new byte[102];
        for (int i = 0; i < largerLength; i++) {
            byte temp = (byte)(subResult[i] + (largerArray[i] - smallerArray[i]));
            if (temp < 0) {
                subResult[i + 1]--; 
                temp += 10;
            }
            subResult[i] = temp;
        }

        // handle negative case : input number is larger than member variable(reversedNumber)
        if (isInputLarger(input)) {   
            for (int i = largerLength - 1; i >= 0; i--) {
                if (subResult[i] != 0) {
                    subResult[i] *= -1;
                    break;
                }
            }
        }
        return convertString(subResult);
    }
  
    public String multiply(BigInteger input) {
        byte[] largerArray = isInputLarger(input) ? input.getArray() : reversedNumber;
        byte[] smallerArray = isInputLarger(input) ? reversedNumber : input.getArray();

        byte[] multiplyResult = new byte[201];
        for (int i = 0; i < smallerArray.length; i++) {
            for (int j = 0; j < largerArray.length; j++) {
                byte temp = (byte)((largerArray[j] * smallerArray[i]) + multiplyResult[i + j]); 
                multiplyResult[i + j] = (byte)(temp % 10);
                if (temp >= 10) {
                    multiplyResult[i + j + 1] += temp / 10; 
                }
            }
        }
        return convertString(multiplyResult);
    }
  
    public String convertString(byte[] inputArray) {
        StringBuilder sb = new StringBuilder();
        String result = new String();
        

        boolean isResultZero = true;
        for (int i = inputArray.length - 1; i >= 0; i--) {
            // check zero
            if (inputArray[i] != 0) {
                isResultZero = false;
            }

            sb.append(inputArray[i]);
        }

        // if result is zero, return string 0 
        // else, remove useless left zeros
        if (isResultZero) {
            result = Character.toString('0');
        } else {
            result = sb.toString().replaceAll("(^0+)", ""); 
        }

        return result;
    }


    static String evaluate(String input) throws IllegalArgumentException {
		String result = new String();
        Matcher match = EXPRESSION_PATTERN.matcher(input);

        if (!input.matches("\\s*([+-])?\\s*([0-9]+)\\s*([-+*])\\s*([+-])?\\s*([0-9]+)\\s*")) {
    		throw new IllegalArgumentException();
    	}

		while (match.find()) {
            MatchResult ms = match.toMatchResult();
			BigInteger num1 = new BigInteger(ms.group(2));
            BigInteger num2 = new BigInteger(ms.group(5));

            if (ms.group(1) != null && ms.group(1).equals(Character.toString('-'))) {
                num1.setIsPositive(false);
            }

            if (ms.group(4) != null && ms.group(4).equals(Character.toString('-'))) {
                num2.setIsPositive(false);
            }

            char operator = ms.group(3).charAt(0);

            switch(operator) {
                case '+':
                    if (num1.isPositive == num2.isPositive) {
                        result = num1.isPositive ? num1.add(num2) : '-' + num1.add(num2);
                    } else {
                        result = num1.isPositive ? num1.subtract(num2) : num2.subtract(num1);
                    }
                    break;
                case '-':
                    if (num1.isPositive == num2.isPositive) {
                        result = num1.isPositive ? num1.subtract(num2) :  num2.subtract(num1);
                    } else {
                        result = num1.isPositive ? num1.add(num2) : '-' + num1.add(num2);
                    }
                    break;
                case '*':
                    result = num1.isPositive == num2.isPositive ? num1.multiply(num2) : '-' + num1.multiply(num2);
                    break;
                default :
                    throw new IllegalArgumentException();
            }
        }	
        return result;
	}
  
    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
  
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
  
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
  
        if (quit)
        {
            return true;
        }
        else
        {
            String result = evaluate(input);
            System.out.println(result);
  
            return false;
        }
    }
  
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
