package util;

import alexnickonovich.library.entities.Author;
import alexnickonovich.library.entities.Book;
import alexnickonovich.library.entities.Publishing;
import alexnickonovich.library.entities.Reader;
import alexnickonovich.library.enums.Gender;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class Controller {
    private final static String SQL_GET_ALLAUTHORS = "select * from authors";
    private final static String SQL_GET_ALLPUBLISHINGS = "select * from publishings";
    private final static String SQL_GET_ALLBOOKS = "select * from books";
    private final static String SQL_GET_ALLREADERS = "select * from readers";
    private final static String SQL_GET_AUTHORS_BY_BOOKS = "select author_firstName,author_lastName from books_has_authors where book=?";
    private final static String SQL_GET_RENT_BY_READER = "select book,startDate,endDate from rents where reader_firstName=? and reader_lastName=?";
    private Connection connection;
    public Controller() throws SQLException {
        this.connection = ConnectorDB.getConnection();
    }
    public void closeConnection() throws SQLException {
        connection.close();
    }
    public ArrayList<Author> getAllAuthors() throws SQLException {

            ArrayList<Author> authors=new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet=statement.executeQuery(SQL_GET_ALLAUTHORS);
            while (resultSet.next()) {

                authors.add(new Author(resultSet.getString(1),resultSet.getString(2),resultSet.getInt(3),Gender.valueOf(resultSet.getString(4))));
            }
            return authors;



    }
    public ArrayList<Publishing> getAllPublishings() throws SQLException{

            ArrayList<Publishing> publishings=new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet=statement.executeQuery(SQL_GET_ALLPUBLISHINGS);
            while (resultSet.next()) {
                publishings.add(new Publishing(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getInt(4),resultSet.getString(5)));
            }
            return publishings;
    }
    public ArrayList<Book> getAllBooks() throws SQLException{

        ArrayList<Book> books=new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet=statement.executeQuery(SQL_GET_ALLBOOKS);
        while (resultSet.next()) {
            PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_AUTHORS_BY_BOOKS);
            preparedStatement.setString(1,resultSet.getString(1));
            ResultSet resultSubSet=preparedStatement.executeQuery();
            ArrayList<String> firstNames=new ArrayList<>();
            ArrayList<String> lastNames=new ArrayList<>();
            while(resultSubSet.next()) {
                firstNames.add(resultSubSet.getString(1));
                lastNames.add(resultSubSet.getString(2));
            }
            LocalDate localDate=resultSet.getDate(4)==null?null:resultSet.getDate(4).toLocalDate();
            books.add(new Book(resultSet.getString(1),resultSet.getInt(2),resultSet.getString(3),localDate,resultSet.getInt(5),resultSet.getBlob(6),firstNames,lastNames));
        }
        return books;
    }
    public ArrayList<Reader> getAllReaders() throws SQLException {

        ArrayList<Reader> readers=new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet=statement.executeQuery(SQL_GET_ALLREADERS);
        while (resultSet.next()) {

            readers.add(new Reader(resultSet.getString(1),resultSet.getString(2),resultSet.getInt(3),resultSet.getString(4),resultSet.getString(5),resultSet.getBlob(6)));
            PreparedStatement preparedStatement=connection.prepareStatement(SQL_GET_RENT_BY_READER);
            preparedStatement.setString(1,resultSet.getString(1));
            preparedStatement.setString(2,resultSet.getString(2));
            ResultSet resultSubSet=preparedStatement.executeQuery();
            while (resultSubSet.next()) {
                LocalDate localDateStart=resultSubSet.getDate(2)==null?null:resultSubSet.getDate(2).toLocalDate();
                LocalDate localDateEnd=resultSubSet.getDate(3)==null?null:resultSubSet.getDate(3).toLocalDate();
                readers.get(readers.size()-1).setRent(Optional.ofNullable(readers.get(readers.size()-1).new Rent(resultSubSet.getString(1),localDateStart,localDateEnd)));
            }

        }
        return readers;
    }
}
