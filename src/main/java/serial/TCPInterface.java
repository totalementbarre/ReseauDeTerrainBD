package serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import serial.Runnable.writerTCP;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPInterface {
    private Socket socket;
    private PrintWriter outputStream;
    private InputStreamReader inputStream;
    private boolean isConnected;

    public TCPInterface() {
        this.isConnected = false;
        this.socket = null;
    }

    /**
     * Open a socket to start communication with the server
     * @param address
     * @param port
     */
    public void establishServerCommunications(String address, int port){
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(socket != null){
            try {
                this.outputStream = new PrintWriter(this.socket.getOutputStream());
                this.inputStream = new InputStreamReader(socket.getInputStream());
                this.isConnected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("Failed to open socket");
        }

    }

    /**
     * Open all the ports of the different components
     * Start a thread and transmit their content to the server
     * It's only a copy for now
     * @param portNames
     */
    public void connectSensorsToServer(List<String> portNames) {
        List<BufferedReader> bufferedReaders = new ArrayList<BufferedReader>();
        for (String portName : portNames) {
            SerialPort serialPort = null;
            try {
                serialPort = connect(portName);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Can't open port : " + portName);
            }
            if (serialPort != null) {
                try {
                    bufferedReaders.add(
                            new BufferedReader(new InputStreamReader(serialPort.getInputStream())));
                    // TODO ADD LIST OF READER TO AND START THE READING THREAD
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        (new Thread(new writerTCP(this.outputStream, bufferedReaders))).start();
    }

    private SerialPort connect(String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier
                .getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            int timeout = 2000;
            CommPort commPort = portIdentifier.open(this.getClass().getName(), timeout);

            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

                return serialPort;

            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
        return null;
    }

    public Boolean disconnect() throws IOException {
        this.outputStream.close();
        this.inputStream.close();
        this.socket.close();
        this.isConnected = false;
        return true;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
