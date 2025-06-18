package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

	boolean existsByTitle(String title);

	List<Book> findAllByTitleContaining(String title);

	Book findByTitle(String title);

	@Query(value = "select * from book b where b.id not in"
			+ " (select books_id from book_authors ba where ba.authors_id = :authorId)", nativeQuery=true)
	List<Book> findBooksNotInAuthor(Long authorId);

}
