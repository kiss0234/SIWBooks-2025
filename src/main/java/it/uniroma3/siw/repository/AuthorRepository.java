package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {

	boolean existsByNameAndSurname(String name, String surname);

	Author findByNameAndSurname(String name, String surname);

	@Query(value = "select * from author a where a.id not in"
			+ " (select authors_id from book_authors ba where ba.books_id = :bookId)", nativeQuery=true)
	List<Author> findAuthorsNotInBook(Long bookId);
}
