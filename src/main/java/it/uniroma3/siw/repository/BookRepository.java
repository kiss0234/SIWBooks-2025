package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

	boolean existsByTitle(String title);

	List<Book> findAllByTitleContaining(String title);

}
