package com.steveprojects.literalura.service;


import com.steveprojects.literalura.model.Author;
import com.steveprojects.literalura.model.Book;
import com.steveprojects.literalura.repositories.AuthorRepository;
import com.steveprojects.literalura.repositories.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;


    @Autowired
    public BookService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public String verifyAndStoreBook (Book book) {
        if (book == null) {
            return "";
        }
        // Verify if book already exists by title
        Optional<Book> existingBook = bookRepository.findByTitle(book.getTitle());

        if (existingBook.isPresent()) {
            return "Book already exists in database";
        }
        // Verify if author exists by name
        try {
            Optional<Author> existingAuthor = authorRepository.findByName(book.getAuthor().getName());

            Author author;
            if (existingAuthor.isPresent()) {
                author = existingAuthor.get();
            } else {
                //Stores author if it doesn't already exist
                author = book.getAuthor();
                authorRepository.save(author);
            }
            book.setAuthor(author);
            //Store book once author has been verified
            bookRepository.save(book);
            return "Book recorded successfully";

        } catch (DataAccessException e) {
            e.printStackTrace();
            return "Error while recording book: " + e.getMessage();

    }
    }


}
