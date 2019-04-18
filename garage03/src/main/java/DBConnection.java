import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DBConnection {

    public static final DBConnection INSTANCE = new DBConnection();

    private SessionFactory sessionFactory;
    private Session s;

    private DBConnection(){
        try{
            sessionFactory = new Configuration().configure().buildSessionFactory();
            generate();
            System.out.println("БД подключена успешно!");
        }
        catch (Exception e){
            System.err.println("Ошибка подключения к БД!");
            e.printStackTrace();
        }
    }

    public void generate(){
        try{
            start();
            s.beginTransaction();
            s.persist(new Car("Toyota", "Corolla", "1997"));
            s.persist(new Car("Hyundai", "Solaris", "2018"));
            s.persist(new Car("Kia", "Rio", "2017"));
            s.getTransaction().commit();
            close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<Car> getCars(){
        try{
            return s.createQuery("from Car").list();
        }
        catch (Exception e){
            System.err.println("Statement 'getCars' Failed");
            e.printStackTrace();
        }

        return new LinkedList<>();
    }


    public List<Garage> getGarageCars(){
        try{
            return s.createQuery("from Garage").list();
        }
        catch (Exception e){
            System.err.println("Statement 'getCars' Failed");
            e.printStackTrace();
        }

        return new LinkedList<>();
    }

    public void addCarToGarage(Car car){
        try{
            s.beginTransaction();
            s.persist(new Garage(car));
            s.getTransaction().commit();
        }
        catch (Exception e){
            System.err.println("Statement 'addCarToGarage' Failed");
            e.printStackTrace();
        }
    }

    public void deleteCarFromGarage(Garage garage){
        try{
            s.beginTransaction();
            s.delete(garage);
            s.getTransaction().commit();
        }
        catch (Exception e){
            System.err.println("Statement 'addCarToGarage' Failed");
            e.printStackTrace();
        }
    }

    public void start(){
        s = sessionFactory.openSession();
    }

    public void close(){
        s.close();
        s = null;
    }
}
