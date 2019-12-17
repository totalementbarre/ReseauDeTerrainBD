package database;

import lombok.*;

import javax.persistence.*;

@Entity
@DiscriminatorValue("2")
public class VariousObject {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String idSensor;

    public VariousObject(String idSensor) {
        this.idSensor = idSensor;
    }

    public String getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(String idSensor) {
        this.idSensor = idSensor;
    }
}
