package serial;

import java.util.ArrayList;
import java.util.List;

public class SerialMain {
    public static void main(String[] args) {
        String serialName = "/dev/ttyACM0";

        System.setProperty("gnu.io.rxtx.SerialPorts", serialName);


//        TwoWaySerialComm twoWaySerialComm = new TwoWaySerialComm();
//        try {
//            twoWaySerialComm.connect( serialName);
//        } catch( Exception e ) {
//            e.printStackTrace();
//        }

        List<String> portNames = new ArrayList<String>();
        portNames.add(serialName);
        TCPInterface tcpInterface = new TCPInterface();
        try {
            tcpInterface.establishServerCommunications("rt.totalementbarre.fr", 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tcpInterface.isConnected()) {
            tcpInterface.connectSensorsToServer(portNames);
        }


    }

}
