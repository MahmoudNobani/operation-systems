package osp;

import java.util.ArrayList;

public class LRU implements Runnable {
    int mem_layoutLRU[][];
    private int buff2[];
    private int pointer2 = 0;
    private int hitLRU = 0, faultLRU = 0;
    private ArrayList<Integer> stack = new ArrayList<Integer>();
    private Boolean isFull = false;
    private int ref_len = 0;
    private int y = 0;
    private int memorySize;
    private int[] Traces;

    
    //the idea her is the same as FCFS excepted we use a stack to hold into the previous value the are recently used
    @Override
    public void run() {
        int frames;
        frames = memorySize;

        for (int i = 0; i < Traces.length; i++) {
            // System.out.print(Traces[i]+" ");
            if (stack.contains(Traces[i])) {
                stack.remove(stack.indexOf(Traces[i]));
            }//if a new p that already existed is requested, we remove it from stack to add it in the top of it
            stack.add(Traces[i]);
            int search = -1;
            for (int j = 0; j < frames; j++) {
                if (buff2[j] == Traces[i]) {//if there increse hit, no fault
                    search = j;
                    hitLRU++;
                    break;
                }
            }
            if (search == -1) {
                if (isFull) {//if full we need to do context switch, here we check with the stack
                    int min_loc = ref_len;
                    for (int j = 0; j < frames; j++) {
                        if (stack.contains(buff2[j])) {
                            int temp = stack.indexOf(buff2[j]);//check were it is in stack
                            if (temp < min_loc) {
                                min_loc = temp;
                                pointer2 = j;
                            }
                        }
                    }
                }//if search also is -1, we have a fault
                buff2[pointer2] = Traces[i];
                faultLRU++;
                pointer2++;
                if (pointer2 == frames) {
                    pointer2 = 0;
                    isFull = true;
                }
            }
//has the output
            for (int j = 0; j < frames; j++) {
                mem_layoutLRU[y][j] = buff2[j];
                 System.out.println(buff2[j]);
            }
             System.out.println();
            y++;

        }

    }

    public LRU(int[][] mem_layoutLRU, int[] buff2, int pointer2, int hitLRU, int faultLRU, ArrayList<Integer> stack,
            Boolean isFull, int ref_len, int y, int memorySize, int[] traces) {
        super();
        this.mem_layoutLRU = mem_layoutLRU;
        this.buff2 = buff2;
        this.pointer2 = pointer2;
        this.hitLRU = hitLRU;
        this.faultLRU = faultLRU;
        this.stack = stack;
        this.isFull = isFull;
        this.ref_len = ref_len;
        this.y = y;
        this.memorySize = memorySize;
        Traces = traces;
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

    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public int[] getTraces() {
        return Traces;
    }

    public void setTraces(int[] traces) {
        Traces = traces;
    }

}