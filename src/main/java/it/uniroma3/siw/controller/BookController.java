package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.siw.model.Book;

import it.uniroma3.siw.service.BookService;
import jakarta.validation.Valid;

@Controller
public class BookController {

	@Autowired BookService bookService;
	
	@GetMapping("/books")
	public String getBooks(Model model) {
		model.addAttribute("books", this.bookService.findAll());
		return "books";
	}
	
	@GetMapping("/book/{id}")
	public String getBook(@PathVariable("id") Long id, Model model) {
		Book book = bookService.findById(id);
		model.addAttribute("book", book);
		return "book";
	}
	
	@GetMapping("/admin/formNewBook")
	public String formNewMovie(Model model) {
		model.addAttribute("book", new Book());
		return "admin/formNewBook";
	}
	
	@PostMapping("/admin/book")
	public String newBook(@Valid @ModelAttribute Book book, BindingResult bindingResult, Model model) {
		//TODO: aggiungere validation book per duplicati
		if(bindingResult.hasErrors())
			return "admin/formNewBook";
		this.bookService.save(book);
		return "redirect:book/" + book.getId();
	}
}
