package osp;
// a queue class that traces go in when traces are finished from scheduling
public class queue {
    int pid;
    int timetoprocess;
    boolean done;
    int timedone;
    int traces[];
    public queue(int pid,int t, boolean tf, int td,int traces[]) { // constructor that takes all values.
        this.pid=pid;
        this.done=tf;
        this.timedone=td;
        this.timetoprocess=t;
        this.traces=traces;
    }
    // normal setters and getters.
    public int[] getTraces() {
        return traces;
    }
    public void setTraces(int[] traces) {
        this.traces = traces;
    }
    public int getPid() {
        return pid;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }
    @Override
    public String toString() {
        String r= "\nqueue [pid=" + pid + ", timetoprocess=" + timetoprocess + ", done=" + done + ", timedone=" + timedone;
        for (int i=0; i<timetoprocess*2; i++) {
            r+= ", memoryTrace: " + traces[i];
        }
        return r;
    }
    public int getTimetoprocess() {
        return timetoprocess;
    }
    public void setTimetoprocess(int timetoprocess) {
        this.timetoprocess = timetoprocess;
    }
    public boolean isDone() {
        return done;
    }
    public void setDone(boolean done) {
        this.done = done;
    }
    public int getTimedone() {
        return timedone;
    }
    public void setTimedone(int timedone) {
        this.timedone = timedone;
    }





}