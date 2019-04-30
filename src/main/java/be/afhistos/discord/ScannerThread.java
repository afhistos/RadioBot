package be.afhistos.discord;

import java.util.Scanner;

public class ScannerThread extends Thread {
    private Scanner scanner = Main.scanner;
    @Override
    public void run() {
        System.out.println("Scanner Thread: " + (this.isInterrupted()? "not running" : "Running"));
        super.run();
        System.out.println("while inner");
        System.out.println("running from ScannerThread.java: "+Main.running);
        try {
            if (scanner.hasNext()) {
                System.out.println("hasNextLine "+scanner.nextLine());
                System.out.println("Command 'stop' call triggered in ScannerThread.java");
                scanner.close();
                this.interrupt();
                Main.consoleCommand(scanner.nextLine());
            }
        }catch (Exception ign){
            Main.consoleCommand("stop");
        }
    }

}
