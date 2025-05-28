package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

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
}
