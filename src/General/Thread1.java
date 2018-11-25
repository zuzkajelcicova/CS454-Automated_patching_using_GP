package General;

public class Thread1 extends Thread {

    private int maxTimeInMinutes = 1;
    private Thread main;

    /*
    In MAIN, call this thread like this.

    Thread currThread = Thread.currentThread();
    Thread1 t1 = new Thread1(1,currThread);
    t1.start();

     */

    public Thread1(int maxTimeInMinutes, Thread main){
        this.maxTimeInMinutes = maxTimeInMinutes;
        this.main = main;
    }

    public void run() {
            System.out.println("My name is: " + getName());

            try {
                Thread.sleep(maxTimeInMinutes * 1000);
                main.stop();

            } catch (InterruptedException ex) {
                // main_thread.interrupt
            }
    }





}
