package database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TransmissionFrame {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String destination;
    private String source;
    private long timeStamp;
    private String type;
    private int priority;
    private String sensorId;
    private String value;

    public TransmissionFrame(String destination, String source, long timeStamp, String type, int priority, String sensorId, String value) {
        this.destination = destination;
        this.source = source;
        this.timeStamp = timeStamp;
        this.type = type;
        this.priority = priority;
        this.sensorId = sensorId;
        this.value = value;
    }

    public TransmissionFrame(String frame) {
        String[] splicedStrings = frame.split(",");
        if (splicedStrings.length != 7) {
            System.err.println("Wrong frame size : " + frame);
            this.destination = "";
            this.source = "";
            this.timeStamp = -1;
            this.type = "";
            this.priority = 0;
            this.sensorId = "";
            this.value = "";
        } else {
            this.destination = splicedStrings[0];
            this.source = splicedStrings[1];
            this.timeStamp = Long.parseLong(splicedStrings[2]);
            this.type = splicedStrings[3];
            this.priority = Integer.parseInt(splicedStrings[4]);
            this.sensorId = splicedStrings[5];
            this.value = splicedStrings[6];
        }


    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
