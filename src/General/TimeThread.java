package General;

public class TimeThread extends Thread {

    private int maxTimeInMinutes;
    private Thread main;


    public TimeThread(int maxTimeInMinutes, Thread main){
        this.maxTimeInMinutes = maxTimeInMinutes;
        this.main = main;
    }

    public void run() {
        System.out.println("My name is: " + getName());

        try {
            Thread.sleep(maxTimeInMinutes * 60 * 1000);
            main.stop();

        } catch (InterruptedException ex) {
            // main_thread.interrupt
        }
    }





}
