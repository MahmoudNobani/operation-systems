package osp;
// Name: Ibrahim Nobani || 1190278
// Name: Mahmoud Nobani || 1180729
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
	static int numOfProcess;
	static int memorySize;
	static int minimumFrames;
	static process[] processArr;
	static process[] processArray;
	static ArrayList<queue> queue1 = new ArrayList<queue>();
	static int buffer[];
	static int reference[];
	static int mem_layout[][];
	static int hit = 0, fault = 0;
	static int x = 0, y = 0;
	static int buff[];
	static int pointer = 0;

	static int mem_layoutLRU[][];
	static int buff2[];
	static int pointer2 = 0;
	static int hitLRU = 0, faultLRU = 0;
	static ArrayList<Integer> stack = new ArrayList<Integer>();
	static Boolean isFull = false;
	static int ref_len = 0;

	static int faultPLRU[] = new int[numOfProcess];
	static int faultPFCFS[] = new int[numOfProcess];

	public static void main(String[] args) throws InterruptedException {

		readFile();// gets the inputs
		int burst[] = new int[numOfProcess];
		int arrival[] = new int[numOfProcess];
		int ID[] = new int[numOfProcess];
		int q = 2; // Quantum Value IF needed to change.

		faultPLRU = new int[numOfProcess];
		faultPFCFS = new int[numOfProcess];

		// arrays will be used for round robin
		for (int i = 0; i < numOfProcess; i++) {
			burst[i] = processArr[i].getDuration(); // file the burst array with the duration
			arrival[i] = processArr[i].getSize();
			ID[i] = i;
			ref_len += processArr[i].memoryTrace.length;
		}

		processArray = new process[numOfProcess];

		while (true) {

			Scanner sc = new Scanner(System.in);

			System.out.println("please choose one of the options:");
			System.out.println("1) FCFS synchronized with Round robin");
			System.out.println("2) LRU synchronized with Round robin");
			System.out.println("anything else) quit:\n");

			int Options = sc.nextInt();

//FCFS synchronized with Round robin
			if (Options == 1) {
				// used with fcfs
				mem_layout = new int[ref_len][memorySize];
				buff = new int[memorySize];
				for (int j = 0; j < memorySize; j++) {
					buff[j] = -1;
				}
				// used fcfs with rr as a thread
				RoundRobin rr = new RoundRobin(ID, arrival, burst, q, processArray, processArr, queue1, mem_layout, hit,
						fault, buff, x, pointer, memorySize, 1, faultPFCFS);
				Thread threadRR = new Thread(rr);
				threadRR.start();
				threadRR.join();
				// print queue info
				System.out.println(queue1.toString());

				// print output of fcfs
				System.out.println();
				System.out.println("output of the FCFS:");
				for (int i = 0; i < memorySize; i++) {
					for (int j = 0; j < ref_len; j++)
						System.out.printf("%3d ", mem_layout[j][i]);
					System.out.println();
				}

				// print fault;

				int tc = 0;
				for (int i = 0; i < numOfProcess; i++) {
					System.out.println("fault of P" + i + ":" + faultPFCFS[i]);
					tc = tc + faultPFCFS[i];
				}
				// print the clock cycle, we note that we assumed each duration is 1 sec only
				int cycle = 0;
				cycle = cycle + queue1.size() - 1;
				cycle = cycle * 5;
				cycle = cycle + queue1.get(queue1.size() - 1).timedone * 1000;
				cycle = cycle + tc * 300;
				System.out.println("cycle time spent:" + cycle);
				queue1.removeAll(queue1);
			}
			
			else if (Options == 2) {
				// used with lru
				mem_layoutLRU = new int[ref_len][memorySize];
				buff2 = new int[memorySize];
				for (int j = 0; j < memorySize; j++)
					buff2[j] = -1;
				// used lru with rr as a thread
				RoundRobin rr2 = new RoundRobin(ID, arrival, burst, q, processArray, processArr, queue1, mem_layoutLRU,
						buff2, pointer2, hitLRU, faultLRU, stack, isFull, ref_len, y, faultPLRU, memorySize, 2);
				Thread threadRR = new Thread(rr2);
				threadRR.start();
				threadRR.join();
				// print queue info
				System.out.println(queue1.toString());
				// now lru part, we print its output
				System.out.println();
				System.out.println("output of the LRU:");
				for (int i = 0; i < memorySize; i++) {
					for (int j = 0; j < ref_len; j++)
						System.out.printf("%3d ", mem_layoutLRU[j][i]);
					System.out.println();
				}

				// we print the faults
				int tc = 0;
				for (int i = 0; i < numOfProcess; i++) {
					System.out.println("fault of P" + i + ":" + faultPLRU[i]);
					tc = tc + faultPLRU[i];
				}

				// print the clock cycle, we note that we assumed each duration is 1 sec only
				int cycle = 0;
				cycle = cycle + queue1.size() - 1;
				cycle = cycle * 5;
				cycle = cycle + queue1.get(queue1.size() - 1).timedone * 1000;
				cycle = cycle + tc * 300;
				System.out.println("cycle time spent:" + cycle);
				queue1.removeAll(queue1);
			}
			else {
				break;
			}

		}

	}

	public static void readFile() { // readfile function. that reads the file and stores it in the process function.
		int i = 0, j = 0;
		int array[] = new int[3];
		String[] processArr2 = new String[5];
		try {
			File myObj = new File("processFile.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				if (i != 3) {
					String data = myReader.nextLine();
					// System.out.println(data);
					array[i] = Integer.decode(data);
					System.out.println(array[i]);
					i++;
					numOfProcess = array[0];
					memorySize = array[1];
					minimumFrames = array[2];
					processArr = new process[numOfProcess];
				} else {
					String data = myReader.nextLine();
					processArr2 = data.split(" ", 5);
					String processTrace[] = new String[Integer.decode(processArr2[3])];
					int processTrace2[] = new int[Integer.decode(processArr2[3])];
					processTrace = processArr2[4].split(" ");
					for (int k = 0; k < Integer.decode(processArr2[3]); k++) {
						processTrace[k] = processTrace[k].substring(0, 5);
						processTrace2[k] = Integer.parseInt(processTrace[k], 16);
						// System.out.println(processTrace2[k]);
					}
					processArr[j] = new process(processArr2[0], Integer.decode(processArr2[1]),
							Integer.decode(processArr2[2]), Integer.decode(processArr2[3]), processTrace2);
					System.out.println(processArr[j].printInformation());
					j++;
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

}