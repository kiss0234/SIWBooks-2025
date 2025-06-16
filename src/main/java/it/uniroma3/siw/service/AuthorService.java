package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.repository.AuthorRepository;
import jakarta.validation.Valid;

@Service
public class AuthorService {

	@Autowired AuthorRepository authorRepository;

	public List<Author> findAll() {
		return (List<Author>) authorRepository.findAll();
	}

	public Author findById(Long id) {
		return authorRepository.findById(id).orElseThrow();
	}

	public boolean existsByNameAndSurname(String name, String surname) {
		return authorRepository.existsByNameAndSurname(name, surname);
	}

	public void save(@Valid Author author) {
		authorRepository.save(author);
		
	}

	public void deleteAuthorById(Long authorId) {
		authorRepository.deleteById(authorId);
	}

	public Author findByNameAndSurname(String name, String surname) {
		return authorRepository.findByNameAndSurname(name, surname);
	}

	public List<Author> findAuthorsNotInBook(Long bookId) {
		return (List<Author>) authorRepository.findAuthorsNotInBook(bookId);
	}
}

