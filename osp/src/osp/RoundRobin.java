package osp;

import java.util.ArrayList;
// A round robin class with 3 constructors, one for normal scheduling, one for scheduling with FCFS in synchronized time and another with LRU.
public class RoundRobin implements Runnable {
	private  int p[];
	private  int a[];
	private  int b[];
	private  int quantum;
	private  process processArray[];
	private  process processArr[];
	private ArrayList<queue> queue1;
	int mem_layout[][];
	int hit;
	int fault;
	int buff[];
	int x;
	int pointer;
	int memorySize;
	int picker=0;
	int mem_layoutLRU[][];
	private int buff2[];
	private int pointer2 = 0;
	private int hitLRU = 0, faultLRU = 0;
	private ArrayList<Integer> stack = new ArrayList<Integer>();
	private Boolean isFull = false;
	private int ref_len = 0;
	private int y = 0;
	private int faultPLRU[];
	private int faultPFCFS[];


	@Override
	public void run() {
		FCFS mem1[] = new FCFS[100];
		LRU LruPr[] = new LRU[100];
		Thread threadFC[] = new Thread[100];
		Thread threadLR[] = new Thread[100];
		// result of average times
		int res = 0;
		int resc = 0;
		int k = 0;
		// for sequence storage
		String seq = new String();
		int traceIndex[]=new int[p.length];
		int traceIndex2[]=new int[p.length];
		for (int i=0; i<p.length; i++) {
			traceIndex[i]=0;
			 traceIndex2[i]=0;
		}

		// copy the burst array and arrival array
		// for not effecting the actual array
		int burstTime[] = new int[p.length];
		int arrivalTime[] = new int[p.length];

		for (int i = 0; i < p.length; i++) {
			burstTime[i] = b[i];
			arrivalTime[i] = a[i];
		}

		// critical time of system
		int t = 0;

		// for store the waiting time
		int w[] = new int[p.length];

		// for store the Completion time
		int comp[] = new int[p.length];

		while (true) {//we will keep going trough all process untill they are done
			boolean flag = true;//end indecation
			for (int i = 0; i < p.length; i++) {

				// these condition for if
				// arrival is not on zero

				// check that if there come before qtime
				if (arrivalTime[i] <= t) {
					if (arrivalTime[i] <= quantum) {
						if (burstTime[i] > 0) {//didnt finish
							flag = false;
							if (burstTime[i] > quantum) {//if burst > quant, its needs another time
								// make decrease the b time
								t = t + quantum;
								burstTime[i] = burstTime[i] - quantum;
								arrivalTime[i] = arrivalTime[i] + quantum;//inc arrival time, as afictional queue
								seq += "," + p[i];
								int traces[] = new int[quantum*2];
								System.out.println("***TRACES that just finished scheduling:");
								for (int T=0; T<quantum*2; T++) {//to get the needed traces 
									traces[T]=processArr[p[i]].getOneTrace(traceIndex[p[i]]);
									System.out.println("TRACE:"+processArr[p[i]].getOneTrace(traceIndex[p[i]]));
									traceIndex[p[i]]++;
								}
								queue1.add(new queue(p[i],quantum,false,t,traces));
								//queue1.add(new queue(p[i],burstTime[i],true,t,traces));
								//synchro part
								if (picker==1) {
									FCFSsyn(mem1,threadFC);
								}
								else if(picker==2) {
									LRUsyn(LruPr,threadLR);
								}
								
							} else { //if less than quantum, its done

								// for last time
								t = t + burstTime[i];

								// store comp time
								comp[i] = t - a[i];

								// store wait time
								w[i] = t - b[i] - a[i];
								int traces[] = new int[burstTime[i]*2];
								System.out.println("***TRACES that just finished scheduling:");
								for (int T=0; T<burstTime[i]*2; T++) {//needed traces
									traces[T]=processArr[p[i]].getOneTrace(traceIndex[p[i]]);
									System.out.println("TRACE:"+processArr[p[i]].getOneTrace(traceIndex[p[i]]));
									traceIndex[p[i]]++;
								}
								queue1.add(new queue(p[i],burstTime[i],true,t,traces));
								if (picker==1) {//and synch
									FCFSsyn(mem1,threadFC);
								}
								else if(picker==2) {
									LRUsyn(LruPr,threadLR);
								}
								burstTime[i] = 0;
								// add sequence
								seq += "D" + p[i];
								processArray[k] = processArr[p[i]];
								k++;
							}
						}
					} else if (arrivalTime[i] > quantum) {

						// is any have less arrival time
						// the coming process then execute them
						for (int j = 0; j < p.length; j++) {

							// compare
							if (arrivalTime[j] < arrivalTime[i]) {//check arrival times, which acts as a fictional q
								//compare if any came before it to continue
								if (burstTime[j] > 0) {//continue the same procedue
									flag = false;
									if (burstTime[j] > quantum) {
										t = t + quantum;
										burstTime[j] = burstTime[j] - quantum;
										arrivalTime[j] = arrivalTime[j] + quantum;
										seq += "," + p[j];
										int traces[] = new int[quantum*2];
										System.out.println("***TRACES that just finished scheduling:");
										for (int T=0; T<quantum*2; T++) {
											traces[T]=processArr[p[j]].getOneTrace(traceIndex[p[j]]);
											System.out.println("TRACE:"+processArr[p[j]].getOneTrace(traceIndex[p[j]]));
											traceIndex[p[j]]++;
										}
										queue1.add(new queue(p[j],quantum,false,t,traces));
										if (picker==1) {
											FCFSsyn(mem1,threadFC);
										}
										else if(picker==2) {
											LRUsyn(LruPr,threadLR);
										}
									} else {
										t = t + burstTime[j];
										comp[j] = t - a[j];
										w[j] = t - b[j] - a[j];
										int traces[] = new int[burstTime[j]*2];
										System.out.println("***TRACES that just finished scheduling:");
										for (int T=0; T<burstTime[j]*2; T++) {
											traces[T]=processArr[p[j]].getOneTrace(traceIndex[p[j]]);
											System.out.println("TRACE:"+processArr[p[j]].getOneTrace(traceIndex[p[j]]));
											traceIndex[p[j]]++;
										}
										queue1.add(new queue(p[j],burstTime[j],true,t,traces));
										if (picker==1) {
											FCFSsyn(mem1,threadFC);
										}
										else if(picker==2) {
											LRUsyn(LruPr,threadLR);
										}

										burstTime[j] = 0;
										seq += "D" + p[j];
										
										processArray[k] = processArr[p[j]];
										k++;
									}
								}
							}
						}

						// now the previous porcess according to
						// ith is process, which excute whats left
						if (burstTime[i] > 0) {
							flag = false;

							// Check for greaters, then continue with the same procedure
							if (burstTime[i] > quantum) {
								t = t + quantum;
								burstTime[i] = burstTime[i] - quantum;
								arrivalTime[i] = arrivalTime[i] + quantum;
								seq += "h" + p[i];
								int traces[] = new int[quantum*2];
								System.out.println("***TRACES that just finished scheduling:");
								for (int T=0; T<quantum*2; T++) {
									traces[T]=processArr[p[i]].getOneTrace(traceIndex[p[i]]);
									System.out.println("TRACE:"+processArr[p[i]].getOneTrace(traceIndex[p[i]]));
									traceIndex[p[i]]++;
								}
								queue1.add(new queue(p[i],quantum,false,t,traces));
								//queue1.add(new queue(p[i],burstTime[i],true,t,traces));
								if (picker==1) {
									FCFSsyn(mem1,threadFC);
								}
								else if(picker==2) {
									LRUsyn(LruPr,threadLR);
								}
							} else {
								t = t + burstTime[i];
								comp[i] = t - a[i];
								w[i] = t - b[i] - a[i];
								int traces[] = new int[burstTime[i]*2];
								System.out.println("***TRACES that just finished scheduling:");
								for (int T=0; T<burstTime[i]*2; T++) {
									traces[T]=processArr[p[i]].getOneTrace(traceIndex[p[i]]);
									
									System.out.println("TRACE:"+processArr[p[i]].getOneTrace(traceIndex[p[i]]));
									traceIndex[p[i]]++;
								}
								queue1.add(new queue(p[i],burstTime[i],true,t,traces));
								//queue1.add(new queue(p[i],burstTime[i],true,t,traces));
								if (picker==1) {
								FCFSsyn(mem1,threadFC);
								}
								else if(picker==2) {
									LRUsyn(LruPr,threadLR);
								}
								burstTime[i] = 0;
								seq += "D" + p[i];
								processArray[k] = processArr[p[i]];
								k++;
							}
						}
					}
				}

				// if no process is came, increase t
				else if (arrivalTime[i] > t) {
					t++;
					i--;
				}
			}

			// for exit the while loop
			if (flag) {
				break;
			}
		}
		
		int ta=0;
		int tt=0;

		System.out.println("name  ctime  waitingTime  TurnAround");
		for (int i = 0; i < p.length; i++) {
			tt=comp[i]-w[i];
			System.out.println(" " + p[i] + "    " + comp[i] + "    " + w[i]+"    " + tt);
			res = res + w[i];
			resc = resc + comp[i];
			ta=ta+tt;
		}

		System.out.println("Average waiting time is " + (float) res / p.length);
		System.out.println("Average compilation  time is " + (float) resc / p.length);
		System.out.println("Average TA time is " + (float) ta / p.length);
		System.out.println("Sequence is like that " + seq);
	}
	
	//constructor for a normal rr
	public RoundRobin(int[] p, int[] a, int[] b, int quantum, process[] processArray, process[] processArr, ArrayList<queue> q1) {
		super();
		this.p = p;
		this.a = a;
		this.b = b;
		this.quantum = quantum;
		this.processArray = processArray;
		this.processArr = processArr;
		this.queue1=q1;
	}
	//cons for a rr Synch with fcfs

		public RoundRobin(int[] p, int[] a, int[] b, int quantum, process[] processArray, process[] processArr,
				ArrayList<queue> queue1, int[][] mem_layout, int hit, int fault, int[] buff, int x, int pointer,
				int memorySize,int picker,int[] faultPFCFS) {
			super();
			this.p = p;
			this.a = a;
			this.b = b;
			this.quantum = quantum;
			this.processArray = processArray;
			this.processArr = processArr;
			this.queue1 = queue1;
			this.mem_layout = mem_layout;
			this.hit = hit;
			this.fault = fault;
			this.buff = buff;
			this.x = x;
			this.pointer = pointer;
			this.memorySize = memorySize;
			this.picker=picker;
			this.faultPFCFS=faultPFCFS;
		}
		
		//cons for a rr Synch with lru

		public RoundRobin(int[] p, int[] a, int[] b, int quantum, process[] processArray, process[] processArr,
				ArrayList<queue> queue1, int[][] mem_layoutLRU, int[] buff2, int pointer2,
				int hitLRU, int faultLRU, ArrayList<Integer> stack, Boolean isFull, int ref_len, int y,int faultPLRU[], int memorySize, int picker) {
			super();
			this.p = p;
			this.a = a;
			this.b = b;
			this.quantum = quantum;
			this.processArray = processArray;
			this.processArr = processArr;
			this.queue1 = queue1;
			this.memorySize = memorySize;
			this.picker = picker;
			this.mem_layoutLRU = mem_layoutLRU;
			this.buff2 = buff2;
			this.pointer2 = pointer2;
			this.hitLRU = hitLRU;
			this.faultLRU = faultLRU;
			this.stack = stack;
			this.isFull = isFull;
			this.ref_len = ref_len;
			this.y = y;
			this.faultPLRU=faultPLRU;
		}

	
	public ArrayList<queue> getQueue1() {
		return queue1;
	}


	public void setQueue1(ArrayList<queue> queue1) {
		this.queue1 = queue1;
	}



	public int[] getP() {
		return p;
	}
	public void setP(int[] p) {
		this.p = p;
	}
	public int[] getA() {
		return a;
	}
	public void setA(int[] a) {
		this.a = a;
	}
	public int[] getB() {
		return b;
	}
	public void setB(int[] b) {
		this.b = b;
	}
	public int getQuantum() {
		return quantum;
	}
	public void setQuantum(int quantum) {
		this.quantum = quantum;
	}


	public process[] getProcessArray() {
		return processArray;
	}


	public void setProcessArray(process[] processArray) {
		this.processArray = processArray;
	}


	public process[] getProcessArr() {
		return processArr;
	}


	public void setProcessArr(process[] processArr) {
		this.processArr = processArr;
	}

	public int[][] getMem_layout() {
		return mem_layout;
	}


	public void setMem_layout(int[][] mem_layout) {
		this.mem_layout = mem_layout;
	}


	public int getHit() {
		return hit;
	}


	public void setHit(int hit) {
		this.hit = hit;
	}


	public int getFault() {
		return fault;
	}


	public void setFault(int fault) {
		this.fault = fault;
	}


	public int[] getBuff() {
		return buff;
	}


	public void setBuff(int[] buff) {
		this.buff = buff;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getPointer() {
		return pointer;
	}


	public void setPointer(int pointer) {
		this.pointer = pointer;
	}


	public int getMemorySize() {
		return memorySize;
	}


	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}


	public int getPicker() {
		return picker;
	}


	public void setPicker(int picker) {
		this.picker = picker;
	}
	public int[] getFaultPFCFS() {
		return faultPFCFS;
	}


	public void setFaultPFCFS(int[] faultPFCFS) {
		this.faultPFCFS = faultPFCFS;
	}


	public int[][] getMem_layoutLRU() {
		return mem_layoutLRU;
	}


	public void setMem_layoutLRU(int[][] mem_layoutLRU) {
		this.mem_layoutLRU = mem_layoutLRU;
	}


	public int[] getBuff2() {
		return buff2;
	}


	public void setBuff2(int[] buff2) {
		this.buff2 = buff2;
	}


	public int getPointer2() {
		return pointer2;
	}


	public void setPointer2(int pointer2) {
		this.pointer2 = pointer2;
	}


	public int getHitLRU() {
		return hitLRU;
	}


	public void setHitLRU(int hitLRU) {
		this.hitLRU = hitLRU;
	}


	public int getFaultLRU() {
		return faultLRU;
	}


	public void setFaultLRU(int faultLRU) {
		this.faultLRU = faultLRU;
	}


	public ArrayList<Integer> getStack() {
		return stack;
	}


	public void setStack(ArrayList<Integer> stack) {
		this.stack = stack;
	}


	public Boolean getIsFull() {
		return isFull;
	}


	public void setIsFull(Boolean isFull) {
		this.isFull = isFull;
	}


	public int getRef_len() {
		return ref_len;
	}


	public void setRef_len(int ref_len) {
		this.ref_len = ref_len;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public int[] getFaultPLRU() {
		return faultPLRU;
	}


	public void setFaultPLRU(int[] faultPLRU) {
		this.faultPLRU = faultPLRU;
	}
	// A function if the FCFS is called, creates new FCFS and places it into the page, followed by another LRU that does the same if and LRU is called.
	public void FCFSsyn(FCFS mem1[],Thread threadFC[]) {
		mem1[queue1.size()] = new FCFS(mem_layout, hit, fault, x, buff, pointer, queue1.get(queue1.size()-1).getTraces(), memorySize);
		threadFC[queue1.size()] = new Thread(mem1[queue1.size()]);
		threadFC[queue1.size()].start();
		//pointer=mem1[queue1.size()].getPointer();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//some needed variable to keep up
		faultPFCFS[queue1.get(queue1.size()-1).pid]=faultPFCFS[queue1.get(queue1.size()-1).pid]+mem1[queue1.size()].getFault();
		pointer=mem1[queue1.size()].getPointer();
		x=mem1[queue1.size()].getX();
		}
	
	
	public void LRUsyn(LRU LruPr[],Thread threadLR[]) {
		LruPr[queue1.size()] = new LRU(mem_layoutLRU, buff2, pointer2, hitLRU, faultLRU, stack, isFull, ref_len, y, memorySize,
				queue1.get(queue1.size()-1).getTraces());
		threadLR[queue1.size()] = new Thread(LruPr[queue1.size()]);
		threadLR[queue1.size()].start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//some needed variable to keep up
		faultPLRU[queue1.get(queue1.size()-1).pid]=faultPLRU[queue1.get(queue1.size()-1).pid]+LruPr[queue1.size()].getFaultLRU();
		pointer2=LruPr[queue1.size()].getPointer2();
        stack=LruPr[queue1.size()].getStack();
        isFull=LruPr[queue1.size()].getIsFull();
        y=LruPr[queue1.size()].getY();
	}
}
