package serial.Runnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class writerTCP implements Runnable {
    private PrintWriter outputStream;
    private List<BufferedReader> inputsBufferedReaders;

    public writerTCP(PrintWriter outputStream, List<BufferedReader> inputsBufferedReaders) {
        this.outputStream = outputStream;
        this.inputsBufferedReaders = inputsBufferedReaders;
    }

    @Override
    public void run() {
        // TODO Flush one time the bufferedReaders
        // TODO add  finish condition
        while (true) {
            for (BufferedReader bufferedReader : inputsBufferedReaders) {
                try {
                    while (true) {
                        String messageToSend = bufferedReader.readLine();
                        System.out.println(messageToSend);
                        this.outputStream.println(messageToSend);
                        this.outputStream.flush();
                    }
                } catch (IOException e) {
//                e.printStackTrace();
                    // TODO INSERT HERE CODE TO HANDLE DISCONNECTION
                    System.err.println("Connection lost with port com");
                }
            }
        }

    }
}
