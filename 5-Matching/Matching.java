import java.io.*;

public class Matching
{

	// 프로그램에서 사용할 HashTable 객체를 생성한다.
	static HashTable<AVLTree<String, SubStringList<String>>> ht;
	public static void main(String args[]) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input) throws Exception
	{
		ConsoleCommand command = null;

		switch (input.charAt(0)) {
			case '<' : {
				ht = new HashTable<AVLTree<String, SubStringList<String>>>();
				command = new InsertCmd();
				command.apply(ht, input);
				break;
			}
			case '@' : {
				command = new PrintCmd();
				command.apply(ht, input);
				break;
			}
			case '?' : {
				command = new SearchCmd();
				command.apply(ht, input);
				break;
			}
			default : 
				throw new IOException(input);
		}
	}
}
