package alexnickonovich.library.managment;
import alexnickonovich.library.entities.*;
import java.sql.SQLException;
import java.util.ArrayList;
import util.Controller;


public class Main {

    public static void main(String[] args) {
        Manager manager=new Manager();
        ArrayList<Author> authors=new ArrayList<>();
        ArrayList<Publishing> publishings=new ArrayList<>();
        ArrayList<Book> books=new ArrayList<>();
        ArrayList<Reader> readers=new ArrayList<>();
        try {
            Controller controller = new Controller();
            authors=controller.getAllAuthors();
            publishings=controller.getAllPublishings();
            books=controller.getAllBooks();
            readers=controller.getAllReaders();
        }
        catch (SQLException e) {
          System.out.println(e.getMessage());
        }
        for (Author x:authors) {
            manager.add(x);
        }
        for (Publishing x:publishings) {
            manager.add(x);
        }
        for (Book x:books) {
            manager.add(x);
        }
        for (Reader x:readers) {
            manager.add(x);
        }
        manager.search("Java the complete reference",0,null,"Stolz");

    }
}
