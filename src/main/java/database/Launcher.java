package database;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class Launcher {
    public static void main(String[] args) {
        // todo add database.DBConnection.java see github
        DBConnection dbConnection = new DBConnection();
        Session session = dbConnection.getSessionFactory().getCurrentSession();
        VariousObject variousObject = new VariousObject("sensor1");

        Transaction transaction = session.beginTransaction();
        session.persist(variousObject);
        transaction.commit();
        session.close();
    }
}
