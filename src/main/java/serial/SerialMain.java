package serial;

import java.util.ArrayList;
import java.util.List;

public class SerialMain {
    public static void main(String[] args) {

        List<String> portNames = new ArrayList<String>();
        portNames.add("/dev/ttyACM0");
        //portNames.add("/dev/ttyACM1");
        //portNames.add("/dev/ttyACM2");
        //portNames.add("/dev/ttyUSB0");

        for (String serialName :
                portNames) {
            System.setProperty("gnu.io.rxtx.SerialPorts", serialName);

        }
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
