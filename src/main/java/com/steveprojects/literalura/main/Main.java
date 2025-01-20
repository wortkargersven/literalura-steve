package com.steveprojects.literalura.main;

import com.steveprojects.literalura.model.Author;
import com.steveprojects.literalura.model.Book;
import com.steveprojects.literalura.model.Languages;
import com.steveprojects.literalura.records.BookData;
import com.steveprojects.literalura.records.Data;
import com.steveprojects.literalura.repositories.AuthorRepository;
import com.steveprojects.literalura.repositories.BookRepository;
import com.steveprojects.literalura.service.BookService;
import com.steveprojects.literalura.service.ConsumeAPI;
import com.steveprojects.literalura.service.ConvertData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class Main {
    private static final String BASE_URL = "https://gutendex.com/books/";
    private ConsumeAPI consumeAPI = new ConsumeAPI();
    private ConvertData converter = new ConvertData();
    private Scanner keyboard = new Scanner(System.in);
    private final BookService bookService;
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public Main(ConsumeAPI consumeAPI, ConvertData converter, BookService bookService,
                     BookRepository bookRepository, AuthorRepository authorRepository) {
        this.consumeAPI = consumeAPI;
        this.converter = converter;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() {
        var welcome = """
                   *****************************************************
                   *         Welcome to Literalura                     *
                   *****************************************************
                   """;
        System.out.println(welcome);
        var option = -1;
        while (option != 0) {
            var menu = """
             *****************************************************
                                Menu                       
             *****************************************************
               1 - Search book by title       
               2 - Show registered books and their stats 
               3 - Show books by language                            
               4 - Show registered authors              
               5 - Show authors who were alive in a specific year         
               6 - Show books by author                
               0 - Exit                                    
             *****************************************************
            """;
            System.out.println(menu);
            try {
                option = keyboard.nextInt();
                keyboard.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid Entry");
                keyboard.nextLine();
                option = -1;
                continue;
            }

            switch (option) {
                case 1:
                    searchBookByTitle();
                    break;
                case 2:
                    showRegisteredBooks();
                    break;
                case 3:
                    searchBooksByLanguage();
                    break;
                case 4:
                    showRegisteredAuthors();
                    break;
                case 5:
                    showAuthorsByYear();
                    break;
                case 6:
                    showBooksByAuthor();
                    break;
                case 0:
                    System.out.println("Exiting app");
                    break;
                default:
                    System.out.println("Invalid entry");
            }
        }
    }

    private void searchBookByTitle() {
        System.out.println("Enter book name:");
        var bookTitle = keyboard.nextLine();
        if (bookTitle == null || bookTitle.trim().isEmpty()) {
            System.out.println("Empty entry.");
            return;
        }
        var json = consumeAPI.getData(BASE_URL + "?search=" +
                bookTitle.replace(" ", "+"));

        var prettyPrinting = converter.getPrettyPrinting(json);
        

        var dataSearch = converter.getData(json, Data.class);


        Optional<BookData> bookSearched = dataSearch.results().stream()
                .filter(b -> {
                    try {
                        if (!b.languages().isEmpty()) {
                            Languages languages = Languages.fromGutendex(b.languages().get(0));
                            return b.title().toUpperCase().contains((bookTitle.toUpperCase()));
                        } else {
                            System.out.println("Language not specified: " + b.title());
                            return false;
                        }
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                })
                .findFirst();
        Book book = null;
        if (bookSearched.isPresent()) {
            book = new Book(bookSearched.get(), null);
            System.out.println(book);
        } else {
            System.out.println("Book not found or incompatible language");
        }

        // Llamamos a la función de verificación y guardado desde BookService:
        String result = bookService.verifyAndStoreBook(book);

        // Mostrar el resultado en la consola:
        System.out.println(result);
    }

    public void showRegisteredBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            System.out.println("No books registered");
        } else {
            System.out.println("Books registered:");
            books.forEach(book -> System.out.println("- " + book));
        }

        
        IntSummaryStatistics stats = books.stream()
                .mapToInt(Book::getDownloadCount) 
                .summaryStatistics();  

        
//        System.out.println("\nDownload statistics:");
//        System.out.println("Average download amount: " + String.format("%.2f",stats.getAverage()));
//        System.out.println("Maximum download amount: " + stats.getMax());
//        System.out.println("Minimum download amount: " + stats.getMin());
        System.out.println("Total downloads: " + stats.getSum());
    }
    

    private void searchBooksByLanguage() {
        System.out.println("Enter a language: ");
        var language = keyboard.nextLine().trim();

        try {
            
            Languages languages = Languages.fromEnglish(Languages.normaliseText(language));
            String languageDataBase = languages.name();
            List<Book> booksByLanguage = bookRepository.findByLanguage(languages);
            System.out.println("Books written in " + languages.getEnglish() + ":");
            booksByLanguage.forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid language.");
        }
    }

    private void showRegisteredAuthors() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            System.out.println("No authors found in the database.");
        } else {
            System.out.println("Registered authors:");
            authors.forEach(author -> System.out.println("- " + author));
        }
    }

    private void showAuthorsByYear() {
        try {
            System.out.println("Enter a year: ");
            var year = keyboard.nextInt();
            if (year > LocalDate.now().getYear()) {
                System.out.println("Invalid year");
                return;
            }
            
            List<Author> livingAuthors = authorRepository.findByYear(year);

            
            if (livingAuthors.isEmpty()) {
                System.out.println("No authors alive in " + year + ".");
            } else {
                System.out.println("Authors who were alive in " + year + ":");
                livingAuthors.forEach(author -> System.out.println(author));
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: need valid entry.");
            
            keyboard.nextLine();
        }
    }

    private void showBooksByAuthor() {
        System.out.println("Enter author's name: ");
        var authorName = keyboard.nextLine();
        if (authorName == null || authorName.trim().isEmpty()) {
            System.out.println("Empty entry.");
            return;
        }
        List<Book> books = bookRepository.findByName(authorName);
        if (books.isEmpty()) {
            System.out.println("No books found by: " + authorName);
        } else {
            books.forEach(book -> System.out.println("- " + book));

        }
    }


}
