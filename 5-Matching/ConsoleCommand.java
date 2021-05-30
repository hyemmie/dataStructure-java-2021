import java.io.*;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// reference : Assignment2 MovieDatabase ConsoleCommand.java

/******************************************************************************
 * Console 을 통해 HashTable을 조작하는 인터페이스.
 */
public interface ConsoleCommand {
	/**
	 * input 을 해석하는 공통 인터페이스.
	 * @param input {@code String} 타입의 입력 문자열
	 * @throws CommandParseException 입력 규칙에 맞지 않는 입력이 들어올 경우 발생
	 */
	String parse(String input) throws CommandParseException;

	/**
	 * 명령을 HashTable 에 적용하고 결과를 출력하는 인터페이스를 정의한다.
	 * @param ht 조작할 HashTable 인스턴스
	 * @throws Exception 일반 오류
	 */
	void apply(HashTable<SubAVLTree<String, SubStringList<String>>> ht, String input) throws Exception;

    /**
	 * input의 ASCII code를 합하고 100으로 나눈 나머지를 반환핟여 hash한다
	 * @param input hash할 string
	 */
	int hash(String input);


}

/******************************************************************************
 * 명령들의 해석 규칙이 동일하므로, 코드 중복을 없애기 위한 추상 클래스.
 */
abstract class AbstractConsoleCommand implements ConsoleCommand {
	/**
	 * 공통 명령 해석 규칙을 담고 있다. {@code input} 을 분해하여 String[] 으로 만들고, 
	 * {@link AbstractConsoleCommand.parseArguments} 로 인자를 전달한다.
	 * 
	 * 만약 어떤 명령이 별도의 해석 규칙이 필요한 경우 이 메소드를 직접 오버라이드하면 된다. 
	 */
	static final int SUBSTRING_LENGTH = 6;

	@Override
	public String parse(String input) throws CommandParseException {
		return input.substring(2, input.length());
	}

    @Override
    public int hash(String input) {
        int sum = 0;
        for (int i = 0; i < input.length(); i++) {
            sum += (int)input.charAt(i);
        }
        return sum % 100;
    }
}

/******************************************************************************
 * 아래부터 각 명령어별로 과제 스펙에 맞는 구현을 한다.
 */

/******************************************************************************
 * < %FILENAME% 
 */
class InsertCmd extends AbstractConsoleCommand {

    @Override
    public void apply(HashTable<SubAVLTree<String, SubStringList<String>>> ht, String input) throws Exception {
        String filename = parse(input);

        FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);

        // new bucket for each line
        // use linkedlist to use iterator (opeartion per line)
		LinkedList<String> inputList = new LinkedList<String>();
        String newLine = new String();
        while((newLine = br.readLine()) != null) {
            inputList.add(newLine);
        }

        Iterator<String> it = inputList.iterator();
        int lineIndex = 0;
        while(it.hasNext()) {
            String currLine = it.next();
            lineIndex++;
            for (int i = 1; i <= currLine.length() - SUBSTRING_LENGTH + 1; i++) {
                String subString = currLine.substring(i-1, i + SUBSTRING_LENGTH - 1);
                String subStringIndex = new String("(" + lineIndex + ", " + i + ")");
                insert(ht, subString, subStringIndex);
            }
        }
    }

    private void insert(HashTable<SubAVLTree<String, SubStringList<String>>> ht, String subString, String subStringIndex) throws Exception {
        int hasedString = hash(subString);
        if (ht.search(hasedString) == null) {
            SubStringList<String> newList = new SubStringList<>(subString);
            newList.add(subStringIndex);
            SubAVLTree<String, SubStringList<String>> newSubAVLTree = new SubAVLTree<>();
            newSubAVLTree.insert(newList);
            ht.insert(hasedString, newSubAVLTree);
        } else {
            SubStringList<String> newList = new SubStringList<>(subString);
            if (ht.search(hasedString).search(newList.getKey()).item == null) {
                newList.add(subStringIndex);
                ht.search(hasedString).insert(newList);
            } else {
                ht.search(hasedString).search(newList.getKey()).item.add(subStringIndex);
            }
        }
    }
}

/******************************************************************************
 * @ %INDEX NUMBER% 
 */
class PrintCmd extends AbstractConsoleCommand {

    static final String EMPTY_SLOT = "EMPTY";

	@Override
	public void apply(HashTable<SubAVLTree<String, SubStringList<String>>> ht, String input) throws Exception {
        int indexNumber = Integer.parseInt(parse(input));
        if (indexNumber < 0 || indexNumber >= 100) throw new Exception();
        else print(ht, indexNumber);
	}

    private StringBuilder traversal(AVLNode<SubStringList<String>> node) {
        StringBuilder sb = new StringBuilder();
        if (node.item != null) {
            sb.append(node.item.getKey());
			sb.append(" ");
			sb.append(traversal(node.getLeft()));
			sb.append(traversal(node.getRight()));
        }
        return sb;
    }

    private void print(HashTable<SubAVLTree<String, SubStringList<String>>> ht, int indexNumber) {
        String res = new String();
        if (ht.search(indexNumber) == null) {
            res = EMPTY_SLOT;
        } else {
            StringBuilder sb = traversal(ht.search(indexNumber).getRoot());
            if (sb.length() == 0) {
                sb.append(EMPTY_SLOT);
            } else {
                sb.deleteCharAt(sb.length() - 1);
            }
            res = sb.toString();
        }
        System.out.println(res);
    }
}

/******************************************************************************
 * ? %pattern% 
 */
class SearchCmd extends AbstractConsoleCommand {

	@Override
	public void apply(HashTable<SubAVLTree<String, SubStringList<String>>> ht, String input) throws Exception {
        String pattern = parse(input);
        if (pattern.length() < SUBSTRING_LENGTH) throw new Exception("invalid pattern length");
        else {
            search(ht, pattern);
        }
	}

    private void search(HashTable<SubAVLTree<String, SubStringList<String>>> ht, String pattern) {

        // split input pattern up to SUBSTRING_LENGTH
        // int splitLength = (pattern.length() - 1) / SUBSTRING_LENGTH + 1;
        int splitLength = pattern.length() / SUBSTRING_LENGTH + 1;
        String[] splitedStrings = new String[splitLength];
        int[] splitedIndex = new int[splitLength];
        
        // save splited strings and it's index
        for (int i = 0; i < splitLength; i++) {
            if ((i + 1) * SUBSTRING_LENGTH < pattern.length()) {
                splitedStrings[i] = pattern.substring(i * SUBSTRING_LENGTH, (i + 1) * SUBSTRING_LENGTH);
                splitedIndex[i] = i * SUBSTRING_LENGTH;
            } else {
                splitedStrings[i] = pattern.substring(pattern.length() - SUBSTRING_LENGTH, pattern.length());
                splitedIndex[i] = pattern.length() - SUBSTRING_LENGTH;
            }
        }

        // search each splited strings in SubAVLTree
        LinkedList<SubStringList<String>> splitedStringList = new LinkedList<SubStringList<String>>();
        SubAVLTree<String, SubStringList<String>> splitedStringTree = new SubAVLTree<String, SubStringList<String>>();
        SubStringList<String> splitedIndexList = new SubStringList<String>(null);
        for (int i = 0; i < splitedStrings.length; i++) {
            splitedStringTree = ht.search(hash(splitedStrings[i]));
            if (splitedStringTree != null && (splitedIndexList = splitedStringTree.search(splitedStrings[i]).item) != null) {
                splitedStringList.add(splitedIndexList);
            } else {
                splitedStringList.clear();
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        
        if (!splitedStringList.isEmpty()) {
            SubStringList<String> first = splitedStringList.peek();
            Pattern p = Pattern.compile("^\\(*([0-9]+)\\,\\s*([0-9]+)\\)$");


            for (String firstIndex : first) {
                boolean found = true;
                int i = 0;
                for (SubStringList<String> subList : splitedStringList) {
                    if (found) {
                        found = false;
                        for (String nextIndex : subList) {
                            Matcher m1 = p.matcher(firstIndex);
                            Matcher m2 = p.matcher(nextIndex);
                            if (m1.find() && m2.find()) {
                                if (Integer.parseInt(m1.group(1)) == Integer.parseInt(m2.group(1)) && 
                                Math.addExact(Integer.parseInt(m1.group(2)),splitedIndex[i]) == Integer.parseInt(m2.group(2))) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                        i++;
                    } else {
                        break;
                    }
                }
                if (found) {
                    sb.append(firstIndex);
                    sb.append(" ");
                }
            }
        }

        if (sb.length() == 0) {
            sb.append("(0, 0)");
        } else {
            sb.deleteCharAt(sb.length() - 1);
        }
        System.out.println(sb.toString());
    }
}

/******************************************************************************
 * 아래의 코드는 ConsoleCommand 에서 사용하는 익셉션들의 모음이다. 
 * 필요하면 수정해도 좋으나 수정하지 않아도 된다. 
 *****************************************************************************/

/******************************************************************************
 * ConsoleCommand 처리 중에 발생하는 익셉션의 상위 클래스이다. 
 * {@code throws} 구문이나 {@code catch} 구문을 간단히 하는데 사용된다.  
 */
@SuppressWarnings("serial")
class ConsoleCommandException extends Exception {
	public ConsoleCommandException(String msg) {
		super(msg);
	}

	public ConsoleCommandException(String msg, Throwable cause) {
		super(msg, cause);
	}
}

/******************************************************************************
 * 명령 파싱 과정에서 발견된 오류상황을 서술하기 위한 예외 클래스 
 */
@SuppressWarnings("serial")
class CommandParseException extends ConsoleCommandException {
	private String command;
	private String input;

	public CommandParseException(String cmd, String input, String cause) {
		super(cause, null);
		this.command = cmd;
		this.input = input;
	}

	public String getCommand() {
		return command;
	}

	public String getInput() {
		return input;
	}

}

/******************************************************************************
 * 존재하지 않는 명령을 사용자가 요구하는 경우를 서술하기 위한 예외 클래스 
 */
class CommandNotFoundException extends ConsoleCommandException {
	private String command;

	public CommandNotFoundException(String command) {
		super(String.format("input command: %s", command));
		this.command = command;
	}

	private static final long serialVersionUID = 1L;

	public String getCommand() {
		return command;
	}
}

