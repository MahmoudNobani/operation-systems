package osp;
// A process Class that takes the process info in, pid , start time, duration , size and its memory traces.
public class process {
    String pid;
    int start;
    int duration;
    int size;
    int memoryTrace []= new int[size];
    //The constructor used in putting the values.
    public process(String pid, int start, int duration, int size, int[] memoryTrace) {
        super();
        this.pid = pid;
        this.start = start;
        this.duration = duration;
        this.size = size;
        this.memoryTrace = memoryTrace;
    }
    public process() {

    }
    // Normal setters and getters.
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }


    public int[] getMemoryTrace() {
        return memoryTrace;
    }
    public void setMemoryTrace(int[] memoryTrace) {
        this.memoryTrace = memoryTrace;
    }
    // Function that prints process
    public String printInformation() { // printing the car information using a string type method with no arguments.
        String info = "\n*PID: " + pid + "\n| Start: " + start + "\n| Duration: "
                + duration + "\n| size: " + size + "\n";
        for (int i=0; i<size; i++) {
            info+= "| memoryTrace: " + memoryTrace[i];
        }
        return info;

    }
    // A function that gets one trace based on the index number, to be used in generating traces in round robin and page replacement.
    public int getOneTrace (int i) {
        return  memoryTrace[i];
    }
}