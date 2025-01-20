package com.steveprojects.literalura.repositories;

import com.steveprojects.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String nombre);

    @Query("SELECT y FROM Author y WHERE " +
            "(y.birthYear <= :year AND (y.deathYear >= :year OR y.deathYear IS NULL))")
    List<Author> findByYear(@Param("year") int year);

}
