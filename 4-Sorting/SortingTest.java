import java.io.*;
import java.util.*;

public class SortingTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");
				}
				else
				{
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
					for (int i = 0; i < newvalue.length; i++)
					{
						System.out.println(newvalue[i]);
					}
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoBubbleSort(int[] value)
	{
		// reference : sorting class note p.10
		for (int i = value.length-1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (value[j] > value[j+1]) {
					int temp = value[j];
					value[j] = value[j+1];
					value[j+1] = temp;
				}
			}
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value)
	{	
		// reference : sorting class note p.17
		for (int i = 1; i < value.length; i++) {
			int cur = value[i];
			int j = i;
			while (j > 0 && value[j-1] > cur) {
				value[j] = value[--j];
			}
			value[j] = cur;
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoHeapSort(int[] value)
	{
		buildHeap(value);
		heapSort(value);
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// reference : sorting class note p.31
	private static int[] DoMergeSort(int[] value)
	{
		if (value.length > 1){
			int[] left = new int[value.length/2];
			int[] right = new int[value.length - value.length/2];
			for (int i = 0; i < left.length; i++)
				left[i] = value[i];
			for (int i = 0; i < right.length; i++)
				right[i] = value[left.length+i];
			
			left = DoMergeSort(left);
			right = DoMergeSort(right);
			
			value = merge(left, right);
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort(int[] value)
	{
		quickSortWithIndex(value, 0, value.length-1);
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoRadixSort(int[] value)
	{
		int maxDigit = (int)Math.log10(Math.abs(value[0]));
		for (int i = 1; i < value.length; i++) {
			int tempDigit = (int)Math.log10(Math.abs(value[i]));
			if (tempDigit > maxDigit) {
				maxDigit = tempDigit;
			}
		}

		for (int j = 0; j <= maxDigit; j++) {
			digitSort(value, j);
		}

		return (value);
	}

	
	/*
	* sub methods to implement sorting
	* percolateDown()
	* heapSort()
	* buildHeap()
	* merge()
	* quickSortWithIndex()
	* partition()
	*/	

	private static void percolateDown(int[] value, int i, int n) {
		// reference : heap class note p.54
		int child = 2 *i+1;
		if (child > n)
			return;
		if (child+1 <= n)
			if (value[child] < value[child+1])
				child++;
		if (value[i] < value[child]) {
			int temp = value[i];
			value[i] = value[child];
			value[child] = temp;
			percolateDown(value, child, n);
		}
	}
	
	static void heapSort(int[] value) {
		// reference : heap class note p.54
		for (int i = value.length-1; i > 0; i--) {
			int temp = value[0];
			value[0] = value[i];
			value[i] = temp;
			percolateDown(value, 0, i-1);
		}
	}

	static void buildHeap(int[] value) {
		// reference : heap class note p.53
		for (int i = value.length/2; i >= 0; i--)
			percolateDown(value, i, value.length-1);
	}

	static int[] merge(int[] left, int[] right) {
		// reference : sorting class note p.32
		int[] value = new int[left.length + right.length];
		int i = 0;
		int j = 0;
		while (i < left.length && j < right.length) {
			if (left[i] <= right[j])
				value[i+j] = left[i++];
			else
				value[i+j] = right[j++];
		}
		while (i < left.length && j >= right.length) {
			value[i+j] = left[i++];
		}
		while (i >= left.length && j < right.length) {
			value[i+j] = right[j++];
		}
		return value;
	}

	static void quickSortWithIndex(int[] value, int first, int last) {
	    if (first < last) {
			int pivot = partition(value, first, last);
			quickSortWithIndex(value, first, pivot-1);
			quickSortWithIndex(value, pivot+1, last);
		}
	}

	// partition -> first area (< pivot) and second area(>= pivot)
	// return pivot's index
	static int partition(int value[], int first, int last) {
		int pivot = value[last];
		int i = first-1;
		for (int j = first; j < last; j++) {
			if (value[j] < pivot) {
				int temp = value[++i];
				value[i] = value[j];
				value[j] = temp;
			}
		}
		// swap second area's first element with pivot
		int temp = value[i+1];
		value[i+1] = value[last];
		value[last] = temp;
		return i+1;
	}

	static void digitSort(int[] value, int digit) {
		Queue<Integer>[] bucket = new Queue[19];

		for (int n = 0; n < 19; n++) {
    		bucket[n] = new LinkedList();
   		}

		for (int i = 0; i < value.length; i++) {
			int temp = ((value[i]%(int)Math.pow(10, digit+1))/(int)Math.pow(10, digit))+9;
			bucket[temp].add(value[i]);
		}

		int item = 0;
        for (int j = 0; j < 19; j++) {
            while (!bucket[j].isEmpty()) {
                value[item++] = bucket[j].remove();
            }   
        }   

	}
}
