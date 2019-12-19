package serial.Runnable;

import serial.TCPInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

public class WriterTCP implements Runnable {
    private TCPInterface tcpInterface;
    private PrintWriter outputStreamFromServer;
    private List<BufferedReader> inputsBufferedReaders;
    private boolean shouldWrite;

    public WriterTCP(PrintWriter outputStreamFromServer, List<BufferedReader> inputsBufferedReaders, TCPInterface tcpInterface) {
        this.outputStreamFromServer = outputStreamFromServer;
        this.inputsBufferedReaders = inputsBufferedReaders;
        this.tcpInterface = tcpInterface;
        this.shouldWrite = true;
    }

    /**
     * Read from serial and write it to TCP connexion.
     */
    @Override
    public void run() {
        // TODO Flush one time the bufferedReaders
        for (int i = 0; i < inputsBufferedReaders.size(); i++) {
            try {
                inputsBufferedReaders.get(i).readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (this.shouldWrite) {
            for (int i = 0; i < inputsBufferedReaders.size(); i++) {
                try {
                    String messageToSend = inputsBufferedReaders.get(i).readLine();
                    System.out.println(messageToSend);
                    this.outputStreamFromServer.println(messageToSend);
                    this.outputStreamFromServer.flush();
                } catch (IOException e) {
//                e.printStackTrace();
                    // TODO INSERT HERE CODE TO HANDLE DISCONNECTION
                    System.err.println("Connection lost with port com, index : " + i);
                    this.outputStreamFromServer.println("OTHERS,local," + (new Date()).getTime() / 1000 + ",DISC,7," + i + ",0");
                    this.outputStreamFromServer.flush();
                    try {
                        this.tcpInterface.disconnect();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean isShouldWrite() {
        return shouldWrite;
    }

    public void setShouldWrite(boolean shouldWrite) {
        this.shouldWrite = shouldWrite;
    }
}
