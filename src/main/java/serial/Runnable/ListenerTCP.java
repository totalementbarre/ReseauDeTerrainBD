package serial.Runnable;

import serial.TCPInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import static java.util.Arrays.copyOf;

public class ListenerTCP implements Runnable {
    private InputStreamReader inputStreamFromServer;
    private List<PrintWriter> outputStreamsToSerials;
    private TCPInterface tcpInterface;
    private boolean shouldListen;


    public ListenerTCP(InputStreamReader inputStreamFromServer, List<PrintWriter> outputStreamsToSerials, TCPInterface tcpInterface) {
        this.inputStreamFromServer = inputStreamFromServer;
        this.outputStreamsToSerials = outputStreamsToSerials;
        this.tcpInterface = tcpInterface;
        this.shouldListen = true;
    }

    /**
     * Listen to the server and write it to the different serial ports
     */
    @Override
    public void run() {
        char[] buf = new char[1024];
        while (this.shouldListen) {
            String messageFromServer = "";
            try {
                int numberOfRealChar = this.inputStreamFromServer.read(buf);
                char[] shortenedBuffer;
                // Connection lost from server
                if (numberOfRealChar == -1) {
                    tcpInterface.disconnect();
                } else {
                    // TODO change here, only send data to destination id serial, its a broadcast for now
                    shortenedBuffer = copyOf(buf, numberOfRealChar);
                    System.out.println("ListenerTCP read : " + String.valueOf(shortenedBuffer));
                    for (int i = 0; i < outputStreamsToSerials.size(); i++) {
                        this.outputStreamsToSerials.get(i).println(shortenedBuffer);
                        this.outputStreamsToSerials.get(i).flush();
                    }
                    buf = resetBuffer(buf, 1024);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private char[] resetBuffer(char[] b, int length) {
        for (int i = 0; i < length; i++)
            b[i] = 0;
        return b;
    }

    public boolean isShouldListen() {
        return shouldListen;
    }

    public void setShouldListen(boolean shouldListen) {
        this.shouldListen = shouldListen;
    }
}
