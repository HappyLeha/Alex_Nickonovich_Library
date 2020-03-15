package alexnickonovich.library.managment;
import alexnickonovich.library.entities.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import alexnickonovich.library.enums.Gender;
import alexnickonovich.library.exceptions.AlreadyExistEntityException;
import alexnickonovich.library.exceptions.CountOfInstanceException;
import alexnickonovich.library.exceptions.RestrictException;
import alexnickonovich.library.json.JsonConverter;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    private static final Logger logger=Logger.getLogger(Manager.class);
    public static void main(String[] args) {
        try {
            Manager manager = new Manager();
            new File("Excel").mkdir();
            File file=new File("Excel/Collections.xlsx");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook();
            String json;
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
            json=JsonConverter.convertToListJson(publishings);
            logger.info(json);
            publishings=JsonConverter.convertFromListJson(json,Publishing.class);
            logger.info(publishings);
            json=JsonConverter.convertToListJson(authors);
            logger.info(json);
            authors=JsonConverter.convertFromListJson(json,Author.class);
            logger.info(authors);
            json=JsonConverter.convertToListJson(books);
            logger.info(json);
            books=JsonConverter.convertFromListJson(json,Book.class);
            logger.info(books);
            json=JsonConverter.convertToListJson(readers);
            logger.info(json);
            readers=JsonConverter.convertFromListJson(json,Reader.class);
            logger.info(readers);
            XSSFSheet sheet = workbook.createSheet("Publishings");
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            XSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("Country");
            row.getCell(0).setCellStyle(headerCellStyle);
            row.createCell(1).setCellValue("Address");
            row.getCell(1).setCellStyle(headerCellStyle);
            row.createCell(2).setCellValue("Index");
            row.getCell(2).setCellStyle(headerCellStyle);
            row.createCell(3).setCellValue("Email");
            row.getCell(3).setCellStyle(headerCellStyle);
            for (int i=0;i<publishings.size();i++) {
                logger.info(publishings.get(i));
                row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(publishings.get(i).getCountry());
                row.createCell(1).setCellValue(publishings.get(i).getAddress());
                row.createCell(2).setCellValue(publishings.get(i).getIndex());
                row.createCell(3).setCellValue(publishings.get(i).getEmail());
            }
            sheet = workbook.createSheet("Authors");
            row = sheet.createRow(0);
            row.createCell(0).setCellValue("FirstName");
            row.getCell(0).setCellStyle(headerCellStyle);
            row.createCell(1).setCellValue("LastName");
            row.getCell(1).setCellStyle(headerCellStyle);
            row.createCell(2).setCellValue("Year");
            row.getCell(2).setCellStyle(headerCellStyle);
            row.createCell(3).setCellValue("Gender");
            row.getCell(3).setCellStyle(headerCellStyle);
            row.createCell(4).setCellValue("Note");
            row.getCell(4).setCellStyle(headerCellStyle);
            for (int i=0;i<authors.size();i++) {
                logger.info(authors.get(i));
                row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(authors.get(i).getFirstName());
                row.createCell(1).setCellValue(authors.get(i).getLastName());
                row.createCell(2).setCellValue(authors.get(i).getYear());
                row.createCell(3).setCellValue(authors.get(i).getGender()+"");
                row.createCell(4).setCellValue(authors.get(i).getNote());

            }
            sheet = workbook.createSheet("Books");
            row = sheet.createRow(0);
            row.createCell(0).setCellValue("Title");
            row.getCell(0).setCellStyle(headerCellStyle);
            row.createCell(1).setCellValue("Year");
            row.getCell(1).setCellStyle(headerCellStyle);
            row.createCell(2).setCellValue("Publishing");
            row.getCell(2).setCellStyle(headerCellStyle);
            row.createCell(3).setCellValue("Date");
            row.getCell(3).setCellStyle(headerCellStyle);
            row.createCell(4).setCellValue("CountOfInstances");
            row.getCell(4).setCellStyle(headerCellStyle);
            row.createCell(5).setCellValue("Image");
            row.getCell(5).setCellStyle(headerCellStyle);
            row.createCell(6).setCellValue("AuthorsFirstNames");
            row.getCell(6).setCellStyle(headerCellStyle);
            row.createCell(7).setCellValue("AuthorsLastNames");
            row.getCell(7).setCellStyle(headerCellStyle);
            row.createCell(8).setCellValue("ReadersFirstNames");
            row.getCell(8).setCellStyle(headerCellStyle);
            row.createCell(9).setCellValue("ReadersLastNames");
            row.getCell(9).setCellStyle(headerCellStyle);
            for (int i=0;i<books.size();i++) {
                logger.info(books.get(i));
                row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(books.get(i).getTitle());
                row.createCell(1).setCellValue(books.get(i).getYear());
                row.createCell(2).setCellValue(books.get(i).getPublishing()+"");
                row.createCell(3).setCellValue(books.get(i).getDate());
                row.createCell(4).setCellValue(books.get(i).getCountOfInstances());
                row.createCell(5).setCellValue(books.get(i).getImage()+"");
                row.createCell(6).setCellValue(books.get(i).getAuthorsFirstNames()+"");
                row.createCell(7).setCellValue(books.get(i).getAuthorsLastNames()+"");
                row.createCell(8).setCellValue(books.get(i).getReadersFirstNames()+"");
                row.createCell(9).setCellValue(books.get(i).getReadersLastNames()+"");
            }
            sheet = workbook.createSheet("Readers");
            row = sheet.createRow(0);
            row.createCell(0).setCellValue("FirstName");
            row.getCell(0).setCellStyle(headerCellStyle);
            row.createCell(1).setCellValue("LastName");
            row.getCell(1).setCellStyle(headerCellStyle);
            row.createCell(2).setCellValue("Year");
            row.getCell(2).setCellStyle(headerCellStyle);
            row.createCell(3).setCellValue("Email");
            row.getCell(3).setCellStyle(headerCellStyle);
            row.createCell(4).setCellValue("Phone");
            row.getCell(4).setCellStyle(headerCellStyle);
            row.createCell(5).setCellValue("Image");
            row.getCell(5).setCellStyle(headerCellStyle);
            row.createCell(6).setCellValue("Book");
            row.getCell(6).setCellStyle(headerCellStyle);
            row.createCell(7).setCellValue("StartDate");
            row.getCell(7).setCellStyle(headerCellStyle);
            row.createCell(8).setCellValue("EndDate");
            row.getCell(8).setCellStyle(headerCellStyle);
            for (int i=0;i<readers.size();i++) {
                logger.info(readers.get(i));
                row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(readers.get(i).getFirstName());
                row.createCell(1).setCellValue(readers.get(i).getLastName());
                row.createCell(2).setCellValue(readers.get(i).getYear());
                row.createCell(3).setCellValue(readers.get(i).getEmail());
                row.createCell(4).setCellValue(readers.get(i).getPhone());
                row.createCell(5).setCellValue(readers.get(i).getImage()+"");
                if (readers.get(i).getRent().isPresent()) {
                    row.createCell(6).setCellValue(readers.get(i).getRent().get().getBook());
                    row.createCell(7).setCellValue(readers.get(i).getRent().get().getStartDate());
                    row.createCell(8).setCellValue(readers.get(i).getRent().get().getEndDate());
                }
            }
            json=JsonConverter.convertToJson(manager.getAuthor(authors.get(0).getFirstName(),authors.get(0).getLastName()));
            logger.info(json);
            logger.info(JsonConverter.convertFromJson(json,Author.class));
            json=JsonConverter.convertToJson(manager.getPublishing(publishings.get(0).getAddress()));
            logger.info(json);
            logger.info(JsonConverter.convertFromJson(json,Publishing.class));
            json=JsonConverter.convertToJson(manager.getBook(books.get(0).getTitle()));
            logger.info(json);
            logger.info(JsonConverter.convertFromJson(json,Book.class));
            json=JsonConverter.convertToJson(manager.getReader(readers.get(2).getFirstName(),readers.get(2).getLastName()));
            logger.info(json);
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
            workbook.write(out);
            manager.closeConnection();
        }
        catch (SQLException | AlreadyExistEntityException| RestrictException| CountOfInstanceException| IOException e) {
           logger.error(e.getMessage());
        }
    }
}
