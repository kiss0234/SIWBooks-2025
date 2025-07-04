package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.BookValidator;
import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AuthService;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.ReviewService;
import jakarta.validation.Valid;

@Controller
public class BookController {

	@Autowired BookService bookService;
	@Autowired BookValidator bookValidator;
	@Autowired AuthService authService;
	@Autowired ReviewService reviewService;
	@Autowired AuthorService authorService;
	
	@GetMapping("/books")
	public String getBooks(Model model) {
		model.addAttribute("books", this.bookService.findAll());
		return "books";
	}
	
	@GetMapping("/book/{id}")
	public String getBook(@PathVariable("id") Long id, Model model) {
		Book book = bookService.findById(id);
		
		User currentUser = authService.getCurrentUser();
		boolean canReview = false;

		if (currentUser != null) {
		    canReview = !reviewService.userHasReviewedBook(book.getId(), currentUser.getId());
		}
		model.addAttribute("canReview", canReview);
		
	    if (!model.containsAttribute("review")) {
	        model.addAttribute("review", new Review());
	    }
		model.addAttribute("book", book);
		
		return "book";
	}
	
	@GetMapping("/searchBook")
	public String searchBook(@RequestParam("booktitle") String title, Model model) {
		model.addAttribute("books", bookService.findAllByTitleContaining(title));
		return "books";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/admin/formNewBook")
	public String formNewBook(Model model) {
			model.addAttribute("book", new Book());
			return "admin/formNewBook";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/admin/newBook")
	public String newBook(
	    @Valid @ModelAttribute Book book,
	    BindingResult bindingResult,
	    @RequestParam("thumbnail") MultipartFile imageFile,
	    Model model) {
		
	    if (imageFile == null || imageFile.isEmpty()) {
	        model.addAttribute("imageError", "Must upload an image.");
	        return "admin/formNewBook";
	    }
	    
	    this.bookValidator.validate(book, bindingResult);
	    
	    if (bindingResult.hasErrors()) {
	        return "admin/formNewBook";
	    }
	    
	    try {
	        Image image = new Image();
	        image.setData(imageFile.getBytes());
	        book.setCover(image);

	    } catch (Exception e) {
	        model.addAttribute("uploadError", "Error rendering the image");
	        return "admin/formNewBook";
	    }

	    this.bookService.save(book);
	    return "redirect:/book/" + book.getId();
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/admin/deleteBook/{bookId}")
	public String deleteBook(@PathVariable("bookId") Long bookId, Model model) {
		this.bookService.deleteBookById(bookId);
		return "redirect:/books";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/admin/editBook/{bookId}")
	public String getEditBookForm(@PathVariable("bookId") Long bookId, Model model) {
		model.addAttribute("book", bookService.findById(bookId));
		return "admin/editBook";
	}
	
	@PostMapping("/admin/editBook")
	public String editBook(@Valid @ModelAttribute Book book,
	                         BindingResult bindingResult,
	                         @RequestParam("thumbnail") MultipartFile cover,
	                         Model model) {

	    if (bindingResult.hasErrors()) {
	        return "admin/editBook";
	    }

	    Book existingBook = bookService.findById(book.getId());
	    
	    if (existingBook == null) {
	        model.addAttribute("error", "Book not found.");
	        return "admin/editBook";
	    }
	    
	    Book duplicate = bookService.findByTitle(book.getTitle());
	    
	    if (duplicate != null && !duplicate.getId().equals(book.getId())) {
	        model.addAttribute("duplicateBook", "There is already a book with the same title");
	        return "admin/editBook";
	    }

	    existingBook.setTitle(book.getTitle());
	    existingBook.setPublicationDate(book.getPublicationDate());
	    existingBook.setDescription(book.getDescription());
	    
	    if (cover != null && !cover.isEmpty()) {
	        try {
	            Image image = new Image();
	            image.setData(cover.getBytes());
	            existingBook.setCover(image);
	        } catch (Exception e) {
	            model.addAttribute("uploadError", "Error rendering the image");
	            return "admin/editBook";
	        }
	    }

	    bookService.save(existingBook);

	    return "redirect:/book/" + existingBook.getId();
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("admin/editBook/{bookId}/authors")
	public String manageBookAuthors(@PathVariable("bookId") Long bookId, Model model) {
		List<Author> availableAuthors = authorService.findAuthorsNotInBook(bookId);
		
		model.addAttribute("availableAuthors", availableAuthors);
		model.addAttribute("book", bookService.findById(bookId));
		
		return "admin/manageBookAuthors";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("admin/removeAuthorFromBook/{bookId}/{authorId}")
	public String removeAuthorFromBook(@PathVariable("bookId") Long bookId, @PathVariable("authorId") Long authorId, Model model) {
		
		Book book = bookService.findById(bookId);
		Author author = authorService.findById(authorId);
		
		List<Author> authors = book.getAuthors();
		authors.remove(author);
		
		bookService.save(book);
		
		return "redirect:/admin/editBook/" + bookId + "/authors";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("admin/addAuthorToBook/{bookId}/{authorId}")
	public String addAuthorToBook(@PathVariable("bookId") Long bookId, @PathVariable("authorId") Long authorId, Model model) {
		
		Book book = bookService.findById(bookId);
		Author author = authorService.findById(authorId);
		
		List<Author> authors = book.getAuthors();
		authors.add(author);
		
		bookService.save(book);
		
		return "redirect:/admin/editBook/" + bookId + "/authors";
	}
}