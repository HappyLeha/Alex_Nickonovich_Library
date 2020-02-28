package alexnickonovich.library.managment;

import alexnickonovich.library.entities.*;
import alexnickonovich.library.exceptions.AlreadyExistEntityException;
import alexnickonovich.library.exceptions.CountOfInstancesException;
import alexnickonovich.library.exceptions.NoSuchMainEntityException;
import alexnickonovich.library.exceptions.RestrictException;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
@NoArgsConstructor
@EqualsAndHashCode
public class Manager {
    static final Logger logger=Logger.getLogger(Manager.class);
    @NonNull  private ArrayList<Book> books=new ArrayList<>();
    @NonNull private ArrayList<Reader> readers=new ArrayList<>();
    @NonNull private ArrayList<Author> authors=new ArrayList<>();
    @NonNull private ArrayList<Publishing> publishings=new ArrayList<>();
    public void add(@NonNull Book book) {
        if (books.contains(book)) throw new AlreadyExistEntityException();
        if (book.getPublishing().isPresent()&&publishings.stream().filter(i->i.getTitle().equals(book.getPublishing().get())).count()==0) throw new NoSuchMainEntityException();
        ArrayList<String> names=new ArrayList<>();
        for (int i=0;i<book.getAuthorsFirstNames().size();i++) {
            names.add(book.getAuthorsFirstNames().get(i)+" "+book.getAuthorsLastNames().get(i));
        }
        for (String name: names) {
           if ( authors.stream().filter(i->name.equals(i.getFirstName()+" "+i.getLastName())).count()==0) throw new NoSuchMainEntityException();
        }
        books.add(book);
        logger.info("Book "+book.getTitle()+" was created and aded to collection");
    }
    public void add(@NonNull Reader reader) {
        if (readers.contains(reader)) throw new AlreadyExistEntityException();
        if (reader.getRent().isPresent()&&books.stream().filter(i->i.getTitle().equals(reader.getRent().get().getBook())).count()==0) throw new NoSuchMainEntityException();
        Book book;
        if (reader.getRent().isPresent()) {
            book = books.stream().filter(i -> i.getTitle().equals(reader.getRent().get().getBook())).findFirst().get();
            if (book.getCountOfInstances() == readers.stream().filter(i -> i.getRent().isPresent()).filter(i -> i.getRent().get().getBook().equals(book)).count())
                throw new CountOfInstancesException();
        }
        readers.add(reader);
        logger.info("Reader "+reader.getFirstName()+" "+reader.getLastName()+" was created and aded to collection");
    }
    public void add(@NonNull Author author) {
        if (authors.contains(author)) throw new AlreadyExistEntityException();
        authors.add(author);
        logger.info("Author "+author.getFirstName()+" "+author.getLastName()+" was created and aded to collection");
    }
    public void add(@NonNull Publishing publishing) {
        if (publishings.contains(publishing)) throw new AlreadyExistEntityException();
        publishings.add(publishing);
        logger.info("Publishing "+publishing.getTitle()+" was created and aded to collection");
    }
    public Object get(int index,@NonNull Class<?> type) {
        if (type==Book.class) return books.get(index);
        else if (type==Reader.class) return readers.get(index);
        else if (type==Author.class) return authors.get(index);
        else if (type==Publishing.class) return publishings.get(index);
        else return null;
    }

    public void remove (@NonNull  Book item) {

        remove(books.indexOf(item),Book.class);
    }
    public void remove (@NonNull Reader item) {

        remove(readers.indexOf(item),Reader.class);
    }
    public void remove (@NonNull Author item) {

        remove(authors.indexOf(item),Author.class);
    }
    public void remove (@NonNull Publishing item) {

        remove(publishings.indexOf(item),Publishing.class);
    }
    public void remove (int index,@NonNull Class<?> type) {
        if (type==Book.class) {

            Book book = books.get(index);
            if (readers.stream().filter(i -> i.getRent().isPresent()).filter(i -> i.getRent().get().equals(book.getTitle())).count() != 0) throw new RestrictException();
            books.remove(index);
            logger.info("Book "+book.getTitle()+" was removed");
        }
        else if (type==Reader.class) {

            Reader reader=readers.get(index);
            if (reader.getRent().isPresent()) throw new RestrictException();

            readers.remove(index);
            logger.info("Reader "+reader.getFirstName()+" "+reader.getLastName()+" was removed");
        }
        else if (type==Author.class) {
            Author author=authors.get(index);
            authors.remove(index);
            logger.info("Author "+author.getFirstName()+" "+author.getLastName()+" was removed");
        }
        else if (type==Publishing.class) {

            Publishing publishing=publishings.get(index);
            if (books.stream().filter(i->i.getPublishing().equals(publishing.getTitle())).count()!=0) throw new RestrictException();
            publishings.remove(index);
            logger.info("Publishing "+publishing.getTitle()+" was removed");
        }
        else return;
    }
    public ArrayList<Book> search(String title,int year,String publishing,String lastName) {
        ArrayList<Book> pickedBooks=supportSearch(title,publishing,lastName);
        if (year!=0) pickedBooks.stream().filter(i->i.getYear()==year).collect(Collectors.toCollection(ArrayList<Book>::new));
        for (Book book:pickedBooks){
            logger.info("Searching result "+book.toString());
        }
        return pickedBooks;
    }
    public ArrayList<Book> search(String title,int startYear,int endYear,String publishing,String lastName) {
        ArrayList<Book> pickedBooks=supportSearch(title,publishing,lastName);
        if (startYear!=0) pickedBooks.stream().filter(i->i.getYear()>=startYear).collect(Collectors.toCollection(ArrayList<Book>::new));
        if (endYear!=0) pickedBooks.stream().filter(i->i.getYear()<=endYear).collect(Collectors.toCollection(ArrayList<Book>::new));
        for (Book book:pickedBooks){
            logger.info("Searching result "+book.toString());
        }
        return pickedBooks;
    }
    private ArrayList<Book> supportSearch(String title,String publishing,String lastName) {
        ArrayList<Book> pickedBooks=books;
        if (title!=null) pickedBooks=pickedBooks.stream().filter(i->i.getTitle().equals(title)).collect(Collectors.toCollection(ArrayList<Book>::new));
        if (publishing!=null) pickedBooks=pickedBooks.stream().filter(i->i.getPublishing().isPresent()&&i.getPublishing().get().equals(publishing)).collect(Collectors.toCollection(ArrayList<Book>::new));
        if (lastName!=null) {
            Optional<Reader> reader=readers.stream().filter(j->j.getLastName().equals(lastName)).findFirst();
            if (reader.isPresent()&&reader.get().getRent().isPresent()) pickedBooks=pickedBooks.stream().filter(i->i.getTitle().equals(reader.get().getRent().get().getBook())).collect(Collectors.toCollection(ArrayList<Book>::new));
        }
        return pickedBooks;
    }
}
