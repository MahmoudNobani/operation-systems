package osp;
// A threaded class for first come first serve algorithm that takes scheduled values and does its page replacment algorithm into them.
public class FCFS implements Runnable {

    int mem_layout[][];
    int hit;
    int fault;
    int buff[];
    int x;
    int pointer;
    int[] Traces;
    int memorySize;

    @Override
    public void run() { // the method run class which has the algorithm inside.
        
        int frames;
       
        frames = memorySize;
        
        for (int i = 0; i < Traces.length; i++) {
            int search = -1;
            for (int j = 0; j < frames; j++) {
                if (buff[j] == Traces[i]) {//check if its there
                    search = j;
                    hit++;
                    break;
                }
            }
            if (search == -1) {//if not a fault occur, context switch is needed
                buff[pointer] = Traces[i];
                fault++;
                pointer++;
                if (pointer == frames)
                    pointer = 0;
            }
            for (int j = 0; j < frames; j++) {
                mem_layout[x][j] = buff[j];
                System.out.println(buff[j]);
            }
            System.out.println();
            x++;
        }
        //System.out.println("Pointer:" + pointer);

    }
// basic constructor followed by typical setters and getters.
    public FCFS(int[][] mem_layout, int hit, int fault, int x, int[] buff, int pointer, int[] traces, int memorySize) {
        super();
        this.mem_layout = mem_layout;
        this.hit = hit;
        this.fault = fault;
        this.x = x;
        this.buff = buff;
        this.pointer = pointer;
        Traces = traces;
        this.memorySize = memorySize;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
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

    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public int[] getTraces() {
        return Traces;
    }

    public void setTraces(int[] traces) {
        Traces = traces;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

}
