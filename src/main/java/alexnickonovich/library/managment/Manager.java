package alexnickonovich.library.managment;
import alexnickonovich.library.entities.*;
import alexnickonovich.library.enums.Gender;
import alexnickonovich.library.exceptions.AlreadyExistEntityException;
import alexnickonovich.library.exceptions.CountOfInstanceException;
import alexnickonovich.library.exceptions.RestrictException;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.apache.log4j.Logger;
import util.ConnectorDB;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@EqualsAndHashCode
public class Manager {
    private final static String SQL_GET_AUTHOR = "select * from authors where firstName=? and lastName=?";
    private final static String SQL_GET_PUBLISHING = "select * from publishings where address=?";
    private final static String SQL_GET_BOOK = "select * from books where title=?";
    private final static String SQL_GET_READER = "select * from readers where firstName=? and lastName=?";
    private final static String SQL_GET_RENT = "select * from rents where reader_firstName=? and reader_lastName=?";
    private final static String SQL_GET_AUTHORS = "select firstName,lastName,year from authors";
    private final static String SQL_GET_PUBLISHINGS = "select address from publishings";
    private final static String SQL_GET_BOOKS = "select title,date,publishing from books";
    private final static String SQL_GET_READERS = "select firstName,lastName,book from readers left join rents on firstName=reader_firstName and lastName=reader_lastName";
    private final static String SQL_GET_AUTHORS_BY_BOOK = "select author_firstName,author_lastName from books_has_authors where book=?";
    private final static String SQL_GET_RENTS_BY_BOOK="select reader_firstName,reader_lastName from rents where book=?";
    private final static String SQL_GET_READERS_WITHOUT_RENTS = "select firstName,lastName form readers except select reader_firstName,reader_lastName form rents";
    private final static String SQL_GET_BOOKS_BY_AUTHOR = "select * from books_has_authors where author_firstName=? and author_lastName=?";
    private final static String SQL_GET_BOOKS_BY_PUBLISHING = "select * from books where publishing=?";
    private final static String SQL_GET_COUNT_OF_RENTS_BY_BOOK="select count(*) from rents where book=?";
    private final static String SQL_GET_BOOK_COUNT_OF_INSTANCES = "select countOfInstances from books where title=?";
    private final static String SQL_ADD_AUTHOR = "insert into authors values(?,?,?,?,?)";
    private final static String SQL_ADD_PUBLISHING = "insert into publishings values(?,?,?,?)";
    private final static String SQL_ADD_BOOK = "insert into books values(?,?,?,?,?,?)";
    private final static String SQL_ADD_READER = "insert into readers values(?,?,?,?,?,?)";
    private final static String SQL_ADD_RENT = "insert into rents values(?,?,?,?,?)";
    private final static String SQL_ADD_BOOK_HAS_AUTHOR = "insert into books_has_authors values(?,?,?)";
    private final static String SQL_SET_AUTHOR = "update authors set firstName=?,lastName=?,year=?,gender=?,note=? where firstName=? and lastName=?";
    private final static String SQL_SET_PUBLISHING = "update publishings set country=?,address=?,postIndex=?,email=? where address=?";
    private final static String SQL_SET_BOOK="update books set title=?,year=?,publishing=?,date=?,countOfInstances=?,image=? where title=?";
    private final static String SQL_SET_READER = "update readers set firstName=?,lastName=?,year=?,email=?,phone=?,image=? where firstName=? and lastName=?";
    private final static String SQL_REMOVE_AUTHOR = " delete from authors where firstName=? and lastName=?";
    private final static String SQL_REMOVE_PUBLISHING = " delete from publishings where address=?";
    private final static String SQL_REMOVE_BOOK = " delete from books where title=?";
    private final static String SQL_REMOVE_READER = " delete from readers where firstName=? and lastName=?";
    private final static String SQL_REMOVE_RENT = " delete from rents where reader_firstName=? and reader_lastName=?";
    private final static String SQL_DECREMENT_BOOK_COUNT_OF_INSTANCES="update book set countOfInstances=countOfInstances-1 where book=?";
    private final static String SQL_PART_OF_SEARCHING_QUERY="select distinct title from searched";
    private Connection connection;
    public Manager() throws SQLException {
        this.connection = ConnectorDB.getConnection();
    }
    public void closeConnection() throws SQLException {
        connection.close();
    }
    public Author getAuthor(@NonNull String firstName,@NonNull String lastName) throws SQLException {
       PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_AUTHOR);
       preparedStatement.setString(1,firstName);
       preparedStatement.setString(2,lastName);
       ResultSet resultSet=preparedStatement.executeQuery();
       if (resultSet.next()) {
           Author author=new Author(resultSet.getString(1),resultSet.getString(2),
                   resultSet.getInt(3),Gender.valueOf(resultSet.getString(4)),
                   resultSet.getString(5));
           return author;
       }
       else {
           return null;
       }

   }
    public Publishing getPublishing(@NonNull String address) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_PUBLISHING);
        preparedStatement.setString(1,address);
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()) {
            Publishing publishing=new Publishing(resultSet.getString(1),resultSet.getString(2),
                    resultSet.getInt(3),resultSet.getString(4));
            return publishing;
        }
        else {
            return null;
        }
    }
    public Book getBook (@NonNull String title) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_BOOK);
        preparedStatement.setString(1,title);
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()) {
            ArrayList<String> firstNames=new ArrayList<>();
            ArrayList<String> lastNames=new ArrayList<>();

            preparedStatement=connection.prepareStatement(SQL_GET_AUTHORS_BY_BOOK);
            preparedStatement.setString(1,title);
            ResultSet resultSubSet=preparedStatement.executeQuery();
            while(resultSubSet.next()) {
                firstNames.add(resultSubSet.getString(1));
                lastNames.add(resultSubSet.getString(2));
            }
            LocalDate date=resultSet.getDate(4)==null?null:resultSet.getDate(4).toLocalDate();
            Book book=new Book(resultSet.getString(1),resultSet.getInt(2),Optional.ofNullable(resultSet.getString(3)),
                    date,resultSet.getInt(5),resultSet.getBlob(6),Optional.ofNullable(firstNames),Optional.ofNullable(lastNames),
                    Optional.empty(),Optional.empty());
            return book;
        }
        else {
            return null;
        }
    }
    public Reader getReader(@NonNull String firstName,@NonNull String lastName) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_READER);
        preparedStatement.setString(1,firstName);
        preparedStatement.setString(2,lastName);
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()) {
            Reader reader=new Reader(resultSet.getString(1),resultSet.getString(2),
                    resultSet.getInt(3),resultSet.getString(4),resultSet.getString(5),
                    resultSet.getBlob(6),Optional.empty());
            return reader;
        }
        else {
            return null;
        }
    }
    public Reader.Rent getRent (@NonNull String firstName,@NonNull String lastName) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_RENT);
        preparedStatement.setString(1,firstName);
        preparedStatement.setString(2,lastName);
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()) {
            Reader reader=new Reader(firstName,lastName,0,null,null,null,Optional.empty());
            LocalDate startDate=resultSet.getDate(4)==null?null:resultSet.getDate(4).toLocalDate();
            LocalDate endDate=resultSet.getDate(5)==null?null:resultSet.getDate(5).toLocalDate();
            Reader.Rent rent=reader.new Rent(resultSet.getString(3),startDate,endDate);
            return rent;
        }
        else {
            return null;
        }
    }
    public ArrayList<Author> getAuthors() throws SQLException {
        ArrayList<Author> authors=new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet=statement.executeQuery(SQL_GET_AUTHORS);
        while (resultSet.next()) {
            authors.add(new Author(resultSet.getString(1),resultSet.getString(2),resultSet.getInt(3), null,null));
        }
        return authors;
    }
    public ArrayList<Publishing> getPublishings() throws SQLException{
        ArrayList<Publishing> publishings=new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet=statement.executeQuery(SQL_GET_PUBLISHINGS);
        while (resultSet.next()) {
            publishings.add(new Publishing(null,resultSet.getString(1),0,null));
        }
        return publishings;
    }
    public ArrayList<Book> getBooks() throws SQLException{
        ArrayList<Book> books=new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet=statement.executeQuery(SQL_GET_BOOKS);
        while (resultSet.next()) {
            PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_RENTS_BY_BOOK);
            preparedStatement.setString(1,resultSet.getString(1));
            ResultSet resultSubSet=preparedStatement.executeQuery();
            ArrayList<String> firstNames=new ArrayList<>();
            ArrayList<String> lastNames=new ArrayList<>();
            while(resultSubSet.next()) {
                firstNames.add(resultSubSet.getString(1));
                lastNames.add(resultSubSet.getString(2));
            }
            LocalDate date=resultSet.getDate(2)==null?null:resultSet.getDate(2).toLocalDate();
            books.add(new Book(resultSet.getString(1),0,Optional.ofNullable(resultSet.getString(3)),
                    date,0,null, Optional.ofNullable(null),Optional.ofNullable(null),
                    Optional.ofNullable(firstNames),Optional.ofNullable(lastNames)));
        }
        return books;
    }
    public ArrayList<Reader> getReaders() throws SQLException {

        ArrayList<Reader> readers=new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet=statement.executeQuery(SQL_GET_READERS);
        while (resultSet.next()) {
            readers.add(new Reader(resultSet.getString(1),resultSet.getString(2),0,null,null,null,Optional.empty()));
            Reader reader=readers.get(readers.size()-1);
            String book=resultSet.getString(3);
            Optional<Reader.Rent> rent=book==null?Optional.ofNullable(null):Optional.ofNullable(reader.new Rent(book,null,null));
            reader.setRent(rent);
        }
        return readers;
    }
    public ArrayList<Reader> getReadersWithoutRents() throws SQLException {

        ArrayList<Reader> readers=new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet=statement.executeQuery(SQL_GET_READERS_WITHOUT_RENTS);
        while (resultSet.next()) {
            readers.add(new Reader(resultSet.getString(1),resultSet.getString(2),0,null,null,null,Optional.empty()));
            Reader reader=readers.get(readers.size()-1);
            String book=resultSet.getString(3);
            Optional<Reader.Rent> rent=book==null?Optional.ofNullable(null):Optional.ofNullable(reader.new Rent(book,null,null));
            reader.setRent(rent);
        }
        return readers;
    }
    public void addAuthor(@NonNull Author author) throws SQLException,AlreadyExistEntityException {
        PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_AUTHOR);
        preparedStatement.setString(1,author.getFirstName());
        preparedStatement.setString(2,author.getLastName());
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()) {
            throw new AlreadyExistEntityException();
        }
        else {
            String gender=author.getGender()==null?null:author.getGender().toString();
            preparedStatement=connection.prepareStatement(SQL_ADD_AUTHOR);
            preparedStatement.setString(1,author.getFirstName());
            preparedStatement.setString(2,author.getLastName());
            preparedStatement.setObject(3,author.getYear());
            preparedStatement.setString(4,gender);
            preparedStatement.setString(5,author.getNote());
            preparedStatement.execute();
        }
    }
    public void addPublishing(@NonNull Publishing publishing) throws SQLException,AlreadyExistEntityException {
        PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_PUBLISHING);
        preparedStatement.setString(1,publishing.getAddress());
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()) {
            throw new AlreadyExistEntityException();
        }
        else {
            preparedStatement=connection.prepareStatement(SQL_ADD_PUBLISHING);
            preparedStatement.setString(1,publishing.getCountry());
            preparedStatement.setString(2,publishing.getAddress());
            preparedStatement.setObject(3,publishing.getIndex());
            preparedStatement.setString(4,publishing.getEmail());
            preparedStatement.execute();
        }
    }
    public void addBook(@NonNull Book book) throws SQLException,AlreadyExistEntityException {
        PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_BOOK);
        preparedStatement.setString(1,book.getTitle());
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()) {
            throw new AlreadyExistEntityException();
        }
        else {
            String publishing=book.getPublishing().isPresent()?book.getPublishing().get():null;
            Date date=book.getDate()==null?null:new Date(book.getDate().toEpochDay());
            preparedStatement=connection.prepareStatement(SQL_ADD_BOOK);
            preparedStatement.setString(1,book.getTitle());
            preparedStatement.setObject(2,book.getYear());
            preparedStatement.setString(3,publishing);
            preparedStatement.setDate(4,date);
            preparedStatement.setObject(5,book.getCountOfInstances());
            preparedStatement.setBlob(6,book.getImage());
            preparedStatement.execute();
            if (book.getAuthorsFirstNames().isPresent()&&book.getAuthorsLastNames().isPresent()) {
                ArrayList<String> firstNames=book.getAuthorsFirstNames().get();
                ArrayList<String> lastNames=book.getAuthorsLastNames().get();
                for (int i=0;i<firstNames.size();i++) {
                    preparedStatement=connection.prepareStatement(SQL_ADD_BOOK_HAS_AUTHOR);
                    preparedStatement.setString(1,book.getTitle());
                    preparedStatement.setString(2,firstNames.get(0));
                    preparedStatement.setString(3,lastNames.get(0));
                    preparedStatement.execute();
                }
            }
        }
    }
    public void addReader(@NonNull Reader reader) throws SQLException,AlreadyExistEntityException {
        PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_READER);
        preparedStatement.setString(1,reader.getFirstName());
        preparedStatement.setString(2,reader.getLastName());
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()) {
            throw new AlreadyExistEntityException();
        }
        else {
            preparedStatement=connection.prepareStatement(SQL_ADD_READER);
            preparedStatement.setString(1,reader.getFirstName());
            preparedStatement.setString(2,reader.getLastName());
            preparedStatement.setObject(3,reader.getYear());
            preparedStatement.setString(4,reader.getEmail());
            preparedStatement.setString(5,reader.getPhone());
            preparedStatement.setBlob(6,reader.getImage());
            preparedStatement.execute();
        }
    }
    public void addRent(@NonNull Reader.Rent rent) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement(SQL_ADD_RENT);
        preparedStatement.setString(1,rent.getReader().getFirstName());
        preparedStatement.setString(2,rent.getReader().getLastName());
        preparedStatement.setString(3,rent.getBook());
        preparedStatement.setDate(4,new Date(rent.getStartDate().toEpochDay()));
        preparedStatement.setDate(5,new Date(rent.getEndDate().toEpochDay()));
        preparedStatement.execute();
    }
    public void setAuthor(@NonNull String firstName,@NonNull String lastName,@NonNull Author author) throws SQLException, RestrictException,AlreadyExistEntityException {
        PreparedStatement preparedStatement;
        if (!(firstName.equals(author.getFirstName())&&lastName.equals(author.getLastName()))) {
            preparedStatement = connection.prepareStatement(SQL_GET_BOOKS_BY_AUTHOR);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                throw new RestrictException();
            }
            preparedStatement=connection.prepareStatement(SQL_GET_AUTHOR);
            preparedStatement.setString(1,author.getFirstName());
            preparedStatement.setString(2,author.getLastName());
            resultSet=preparedStatement.executeQuery();
            if (resultSet.next()) {
                throw new AlreadyExistEntityException();
            }
        }
        preparedStatement=connection.prepareStatement(SQL_SET_AUTHOR);
        preparedStatement.setString(1,author.getFirstName());
        preparedStatement.setString(2,author.getLastName());
        preparedStatement.setObject(3,author.getYear());
        preparedStatement.setString(4,author.getGender().toString());
        preparedStatement.setString(5,author.getNote());
        preparedStatement.setString(6,firstName);
        preparedStatement.setString(7,lastName);
        preparedStatement.execute();
   }
    public void  setPublishing(@NonNull String address,Publishing publishing) throws SQLException,RestrictException,AlreadyExistEntityException {
        PreparedStatement preparedStatement;
        if (!address.equals(publishing.getAddress())) {
            preparedStatement = connection.prepareStatement(SQL_GET_BOOKS_BY_PUBLISHING);
            preparedStatement.setString(1, address);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                throw new RestrictException();
            }
            preparedStatement=connection.prepareStatement(SQL_GET_PUBLISHING);
            preparedStatement.setString(1,publishing.getAddress());
            resultSet=preparedStatement.executeQuery();
            if (resultSet.next()) {
                throw new AlreadyExistEntityException();
            }
        }
        preparedStatement=connection.prepareStatement(SQL_SET_PUBLISHING);
        preparedStatement.setString(1,publishing.getCountry());
        preparedStatement.setString(2,publishing.getAddress());
        preparedStatement.setObject(3,publishing.getIndex());
        preparedStatement.setString(4,publishing.getEmail());
        preparedStatement.setString(5,address);
        preparedStatement.execute();
    }
    public void setBook(@NonNull String title,@NonNull Book book) throws SQLException,RestrictException,AlreadyExistEntityException,CountOfInstanceException {
        PreparedStatement preparedStatement;
        if (!title.equals(book.getTitle())) {
            preparedStatement = connection.prepareStatement(SQL_GET_RENTS_BY_BOOK);
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                throw new RestrictException();
            }
            preparedStatement=connection.prepareStatement(SQL_GET_BOOK);
            preparedStatement.setString(1,book.getTitle());
            resultSet=preparedStatement.executeQuery();
            if (resultSet.next()) {
                throw new AlreadyExistEntityException();
            }
        }
        preparedStatement = connection.prepareStatement(SQL_GET_COUNT_OF_RENTS_BY_BOOK);
        preparedStatement.setString(1, title);
        ResultSet resultSet=preparedStatement.executeQuery();
        resultSet.next();
        if (book.getCountOfInstances()<resultSet.getInt(1)) throw new CountOfInstanceException();
        String publishing=book.getPublishing().isPresent()?book.getPublishing().get():null;
        Date date=book.getDate()==null?null:new Date(book.getDate().toEpochDay());
        preparedStatement=connection.prepareStatement(SQL_SET_BOOK);
        preparedStatement.setString(1,book.getTitle());
        preparedStatement.setObject(2,book.getYear());
        preparedStatement.setString(3,publishing);
        preparedStatement.setDate(4,date);
        preparedStatement.setObject(5,book.getCountOfInstances());
        preparedStatement.setBlob(6,book.getImage());
        preparedStatement.setString(7,title);
        preparedStatement.execute();
    }
    public void decrementBookCountOfInstances(@NonNull String title) throws SQLException{
       PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_BOOK_COUNT_OF_INSTANCES);
       preparedStatement.setString(1,title);
       ResultSet resultSet=preparedStatement.executeQuery();
       if (resultSet.getInt(1)==1) {
           preparedStatement=connection.prepareStatement(SQL_REMOVE_BOOK);
       }
       else {
           preparedStatement=connection.prepareStatement(SQL_DECREMENT_BOOK_COUNT_OF_INSTANCES);
       }
        preparedStatement.setString(1,title);
        preparedStatement.execute();
    }
    public void setReader(@NonNull String firstName,@NonNull String lastName,@NonNull Reader reader) throws SQLException,AlreadyExistEntityException {
       PreparedStatement preparedStatement=connection.prepareStatement(SQL_SET_READER);preparedStatement.setString(1,reader.getFirstName());preparedStatement.setString(2,reader.getLastName());preparedStatement.setObject(3,reader.getYear());preparedStatement.setString(4,reader.getEmail());preparedStatement.setString(5,reader.getPhone());preparedStatement.setBlob(6,reader.getImage());
       preparedStatement.setString(7,firstName);
       preparedStatement.setString(8,lastName);
       preparedStatement.execute();
    }
    public void removeAuthor(@NonNull String firstName,@NonNull String lastName) throws SQLException, RestrictException {
        PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_BOOKS_BY_AUTHOR);
        preparedStatement.setString(1,firstName);
        preparedStatement.setString(2,lastName);
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()) {
            throw new RestrictException();
        }
        else {
            preparedStatement=connection.prepareStatement(SQL_REMOVE_AUTHOR);
            preparedStatement.setString(1,firstName);
            preparedStatement.setString(2,lastName);
            preparedStatement.execute();
        }
    }
    public void removePublishing(@NonNull String address) throws SQLException,RestrictException {
        PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_BOOKS_BY_PUBLISHING);
        preparedStatement.setString(1,address);
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()) {
            throw new RestrictException();
        }
        else {
            preparedStatement=connection.prepareStatement(SQL_REMOVE_PUBLISHING);
            preparedStatement.setString(1,address);
            preparedStatement.execute();
        }
    }
    public void removeBook(@NonNull String title) throws SQLException,RestrictException {
        PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_RENTS_BY_BOOK);
        preparedStatement.setString(1,title);
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()) {
            throw new RestrictException();
        }
        else {
            preparedStatement=connection.prepareStatement(SQL_REMOVE_BOOK);
            preparedStatement.setString(1,title);
            preparedStatement.execute();
        }
    }
    public void removeReader(@NonNull String firstName,@NonNull String lastName) throws SQLException, RestrictException {
        PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_RENT);
        preparedStatement.setString(1,firstName);
        preparedStatement.setString(2,lastName);
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()) {
            throw new RestrictException();
        }
        else {
            preparedStatement=connection.prepareStatement(SQL_REMOVE_READER);
            preparedStatement.setString(1,firstName);
            preparedStatement.setString(2,lastName);
            preparedStatement.execute();
        }
    }
    public void removeRent (@NonNull String firstName,@NonNull String lastName) throws SQLException, RestrictException {
        PreparedStatement preparedStatement=connection.prepareStatement(SQL_REMOVE_RENT);
        preparedStatement.setString(1,firstName);
        preparedStatement.setString(2,lastName);
        preparedStatement.execute();
    }
    public ArrayList<Book> search (String title,Integer startYear,Integer endYear,String publishing,String lastName) throws SQLException {
       String query=SQL_PART_OF_SEARCHING_QUERY;
       int counter=0;
       if (title!=null) {
           counter++;
           query+=" where title='"+title+"'";
       }
       if (publishing!=null) {
           if (counter==0) {
               query+=" where" ;
           }
           else {
               counter++;
               query+=" and";
           }
           query+=" publishing='"+publishing+"'";
       }
        if (lastName!=null) {
            if (counter==0) {
                query+=" where" ;
            }
            else {
                counter++;
                query+=" and";
            }

            query+=" lastName='"+lastName+"'";
        }
       if (startYear!=null) {
           if (endYear!=null) {
               if (counter==0) {
                   query+=" where" ;
               }
               else {
                   counter++;
                   query+=" and";
               }
               query+=" year between "+startYear+" end "+endYear;
           }
           else {
               if (counter==0) {
                   query+=" where" ;
               }
               else {
                   counter++;
                   query+=" and";
               }
               query+=" year="+startYear;
           }
       }
       Statement statement=connection.createStatement();
       ResultSet resultSet=statement.executeQuery(query);
       ArrayList<String> titles=new ArrayList<>();
       ArrayList<Book> books=new ArrayList<>();
       while (resultSet.next()) {
           titles.add(resultSet.getString(1));
       }
       for (String x:titles) {
           books.add(getBook(x));
       }
       return books;
    }
}
