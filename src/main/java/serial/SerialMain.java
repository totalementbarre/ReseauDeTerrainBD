package serial;

public class SerialMain {
    public static void main(String[] args) {
        System.setProperty("gnu.io.rxtx.SerialPorts","/dev/ttyACM1" );


        TwoWaySerialComm twoWaySerialComm = new TwoWaySerialComm();
        try {
            twoWaySerialComm.connect( "/dev/ttyACM1" );
        } catch( Exception e ) {
            e.printStackTrace();
        }



    }

}
