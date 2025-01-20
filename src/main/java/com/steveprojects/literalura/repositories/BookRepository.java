package com.steveprojects.literalura.repositories;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.steveprojects.literalura.model.Book;
import com.steveprojects.literalura.model.Languages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle (String title);

    @Query("SELECT b FROM Book b WHERE b.languages = :languages")
    List<Book> findByLanguage(@Param("languages") Languages language);

    @Query("SELECT b FROM Book b WHERE LOWER(b.author.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Book> findByName(@Param("name") String name);

}
