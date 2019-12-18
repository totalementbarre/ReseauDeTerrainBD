package serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import serial.Runnable.ListenerTCP;
import serial.Runnable.WriterTCP;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TCPInterface {
    public static final String SOURCE_TAG = "local";
    private Socket socket;
    private PrintWriter outputStream;
    private InputStreamReader inputStream;
    private boolean isConnected;
    private List<BufferedReader> inputStreamsFromSerials;
    private List<PrintWriter> outputStreamsToSerials;
    private WriterTCP writerTCP;
    private ListenerTCP listenerTCP;

    public TCPInterface() {
        this.inputStreamsFromSerials = new ArrayList<BufferedReader>();
        this.outputStreamsToSerials = new ArrayList<PrintWriter>();
        this.isConnected = false;
        this.socket = null;
        this.writerTCP = null;
        this.listenerTCP = null;
    }

    /**
     * Open a socket to start communication with the server
     *
     * @param address
     * @param port
     */
    public void establishServerCommunications(String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (socket != null) {
            try {
                this.outputStream = new PrintWriter(this.socket.getOutputStream());
                this.inputStream = new InputStreamReader(socket.getInputStream());
                this.isConnected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Failed to open socket");
        }

    }

    /**
     * Open all the ports of the different components
     * Start a thread and transmit their content to the server
     * It's only a copy for now
     *
     * @param portNames
     */
    public void connectSensorsToServer(List<String> portNames) {
        List<BufferedReader> inputStreamsFromSerials = new ArrayList<BufferedReader>();
        List<PrintWriter> outputStreamsToSerials = new ArrayList<PrintWriter>();
        for (String portName : portNames) {
            SerialPort serialPort = null;
            try {
                serialPort = connectSerialPort(portName);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Can't open port : " + portName);
            }
            if (serialPort != null) {
                try {
                    inputStreamsFromSerials.add(
                            new BufferedReader(new InputStreamReader(serialPort.getInputStream())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStreamsToSerials.add(new PrintWriter(serialPort.getOutputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        this.inputStreamsFromSerials = inputStreamsFromSerials;
        this.outputStreamsToSerials = outputStreamsToSerials;
        this.outputStream.println("," + SOURCE_TAG + "," + (new Date()).getTime() / 1000 + ",AUTH,,," + SOURCE_TAG);
        this.outputStream.flush();

        (new Thread(new WriterTCP(this.outputStream, inputStreamsFromSerials))).start();
        (new Thread(new ListenerTCP(this.inputStream, outputStreamsToSerials, this))).start();


    }

    private SerialPort connectSerialPort(String portName) throws Exception {
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

    /**
     * End properly all threads and close all streams
     *
     * @return
     * @throws IOException
     */
    public Boolean disconnect() throws IOException {
        this.writerTCP.setShouldWrite(false);
        this.listenerTCP.setShouldListen(false);
        // TODO maybe add a while here to wait for the thread to end properly by adding a done boolean to the threads
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
