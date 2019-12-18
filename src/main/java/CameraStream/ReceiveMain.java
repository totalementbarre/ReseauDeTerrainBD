package CameraStream;


import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

public class ReceiveMain {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        VideoCapture ip_cam = new VideoCapture();

        ip_cam.open("http://127.0.0.1:56000/mjpeg");
    }
}
