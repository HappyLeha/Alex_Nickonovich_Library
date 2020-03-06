package alexnickonovich.library.managment;
import alexnickonovich.library.entities.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import alexnickonovich.library.enums.Gender;
import alexnickonovich.library.exceptions.AlreadyExistEntityException;
import alexnickonovich.library.exceptions.CountOfInstanceException;
import alexnickonovich.library.exceptions.RestrictException;
import org.apache.log4j.Logger;


public class Main {
    private static final Logger logger=Logger.getLogger(Manager.class);
    public static void main(String[] args) {
        try {
            Manager manager = new Manager();
            manager.addAuthor(new Author("Herbert","Schildt",null,null,null));
            manager.addPublishing(new Publishing(null,"St.Peterburg",null,null));
            manager.addBook(new Book("Java the complete reference",2011,
                    Optional.empty(),null,1,
                    null, Optional.empty(),Optional.empty(),
                    Optional.empty(),Optional.empty()));
            manager.addBook(new Book("Philosophy of java",2013,
                    Optional.empty(),null,1,
                    null, Optional.empty(),Optional.empty(),
                    Optional.empty(),Optional.empty()));
            Reader readerWithRent=new Reader("Martin","Stolz",null,"alexnovak@gmail.com",null,null,Optional.empty());
            manager.addReader(new Reader("Alex","Novak",null,"martinstolz@gmail.com",null,null,Optional.empty()));
            manager.addReader(readerWithRent);
            manager.addReader(new Reader("Anna","Polonskaya",null,"annapolonskaya@gmail.com",null,null,Optional.empty()));
            LocalDate startDate=LocalDate.of(2020,2,28);
            LocalDate endDate=LocalDate.of(2020,9,9);
            readerWithRent.setRent("Java the complete reference",startDate,endDate);
            manager.addRent(readerWithRent.getRent().get());
            manager.setAuthor("Herbert","Schildt",new Author("Herbert","Schildt",null,Gender.MAN,null));
            manager.setPublishing("St.Peterburg",new Publishing("Russia","St.Peterburg",null,null));
            ArrayList<String> firstNames=new ArrayList();
            ArrayList<String> lastNames=new ArrayList();
            firstNames.add("Herbert");
            lastNames.add("Schildt");
            manager.setBook("Java the complete reference",new Book("Java the complete reference",2013,
                    Optional.ofNullable("St.Peterburg"),null,1,
                    null, Optional.ofNullable(firstNames),Optional.ofNullable(lastNames),
                    Optional.empty(),Optional.empty()));
            manager.setBook("Philosophy of java",new Book("Philosophy of java",2011,
                    Optional.empty(),null,2,
                    null, Optional.empty(),Optional.empty(),
                    Optional.empty(),Optional.empty()));
            manager.setReader("Alex","Novak",new Reader("Alex","Novak",1995,"alexnovak@gmail.com",null,null,Optional.empty()));
            manager.setReader("Martin","Stolz",new Reader("Martin","Stolz",1998,"martinstolz@gmail.com",null,null,Optional.empty()));
            manager.setReader("Anna","Polonskaya",new Reader("Anna","Polonskaya",2001,"annapolonskaya@gmail.com",null,null,Optional.empty()));
            ArrayList<Publishing> publishings=manager.getPublishings();
            ArrayList<Author> authors=manager.getAuthors();
            ArrayList<Book> books=manager.getBooks();
            ArrayList<Reader> readers=manager.getReaders();
            for (Publishing publishing:publishings){
                logger.info(publishing.toString());
            }
            for (Author author:authors){
                logger.info(author);
            }
            for (Book book:books){
                logger.info(book);
            }
            for (Reader reader:readers){
                logger.info(reader);
            }
            logger.info(manager.getAuthor(authors.get(0).getFirstName(),authors.get(0).getLastName()));
            logger.info(manager.getPublishing(publishings.get(0).getAddress()));
            logger.info(manager.getBook(books.get(0).getTitle()));
            logger.info(manager.getReader(readers.get(2).getFirstName(),readers.get(2).getLastName()));
            logger.info(manager.getRent(readers.get(2).getFirstName(),readers.get(2).getLastName()));
            ArrayList<Book> searched=manager.search("Java the complete reference",null,null,null,"Stolz");
            for (Book book:searched) {
                logger.info("Result of searching "+book);
            }
            manager.removeRent(readerWithRent.getRent().get().getReader().getFirstName(),readerWithRent.getRent().get().getReader().getLastName());
            for (Reader reader:readers) {
                manager.removeReader(reader.getFirstName(),reader.getLastName());
            }
            for (Book book:books) {
                manager.removeBook(book.getTitle());
            }
            for (Publishing publishing:publishings) {
                manager.removePublishing(publishing.getAddress());
            }
            for (Author author:authors) {
                manager.removeAuthor(author.getFirstName(),author.getLastName());
            }
            manager.closeConnection();
        }
        catch (SQLException | AlreadyExistEntityException| RestrictException| CountOfInstanceException e) {
           logger.error(e.getMessage());
        }
    }
}
