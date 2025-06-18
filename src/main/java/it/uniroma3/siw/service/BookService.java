package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.repository.BookRepository;

@Service
public class BookService {
	@Autowired BookRepository bookRepository;
	
	public List<Book> findAll() {
		List<Book> books = (List<Book>) this.bookRepository.findAll();
		return books;
	}
	
	public void save(Book book) {
		this.bookRepository.save(book);
	}
	
	public Book findById(Long id) {
		return this.bookRepository.findById(id).get();
	}

	public boolean existsByTitle(String title) {
		return this.bookRepository.existsByTitle(title);
	}

	public List<Book> findAllByTitleContaining(String title) {
		List<Book> books = (List<Book>) this.bookRepository.findAllByTitleContaining(title);
		return books;
	}

	public void deleteBookById(Long bookId) {
		bookRepository.deleteById(bookId);
	}

	public Book findByTitle(String title) {
		return bookRepository.findByTitle(title);
	}

	public List<Book> findBooksNotInAuthor(Long authorId) {
		return (List<Book>) bookRepository.findBooksNotInAuthor(authorId);
	}
}
