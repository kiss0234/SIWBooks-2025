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

import it.uniroma3.siw.controller.validator.AuthorValidator;
import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import jakarta.validation.Valid;

@Controller
public class AuthorController {
	
	@Autowired AuthorService authorService;
	@Autowired AuthorValidator authorValidator;
	@Autowired BookService bookService;
	
	@GetMapping("/authors")
	public String getAuthors(Model model) {
		model.addAttribute("authors", authorService.findAll());
		return "authors";
	}
	
	@GetMapping("/author/{id}")
	public String getAuthor(@PathVariable("id") Long id, Model model) {
		model.addAttribute("author", authorService.findById(id));
		return "author";
	}
	
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/admin/formNewAuthor")
	public String formNewAuthor(Model model) {
			model.addAttribute("author", new Author());
			return "admin/formNewAuthor";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/admin/newAuthor")
	public String newAuthor(
	    @Valid @ModelAttribute Author author,
	    BindingResult bindingResult,
	    @RequestParam("thumbnail") MultipartFile imageFile,
	    Model model) {
		
	    if (imageFile == null || imageFile.isEmpty()) {
	        model.addAttribute("imageError", "Must upload an image.");
	        return "admin/formNewAuthor";
	    }
	    
	    this.authorValidator.validate(author, bindingResult);
	    
	    if (bindingResult.hasErrors()) {
	        return "admin/formNewAuthor";
	    }
	    
	    try {
	        Image image = new Image();
	        image.setData(imageFile.getBytes());
	        author.setAuthorPhoto(image);

	    } catch (Exception e) {
	        model.addAttribute("uploadError", "Error rendering the image");
	        return "admin/formNewAuthor";
	    }

	    this.authorService.save(author);
	    return "redirect:/author/" + author.getId();
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/admin/deleteAuthor/{authorId}")
	public String deleteAuthor(@PathVariable("authorId") Long authorId) {
		Author author = authorService.findById(authorId);
		
		for(Book book : author.getBooks()) {
			book.getAuthors().remove(author);
			bookService.save(book);
		}
		
		this.authorService.deleteAuthorById(authorId);
		return "redirect:/authors";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/admin/editAuthor/{authorId}")
	public String editAuthor(@PathVariable("authorId") Long authorId, Model model) {
		model.addAttribute("author", authorService.findById(authorId));
		return "admin/editAuthor";
	}
	
	@PostMapping("/admin/editAuthor")
	public String editAuthor(@Valid @ModelAttribute Author author,
	                         BindingResult bindingResult,
	                         @RequestParam("thumbnail") MultipartFile authorPhoto,
	                         Model model) {

	    if (bindingResult.hasErrors()) {
	        return "admin/editAuthor";
	    }

	    Author existingAuthor = authorService.findById(author.getId());
	    
	    if (existingAuthor == null) {
	        model.addAttribute("error", "Author not found.");
	        return "admin/editAuthor";
	    }
	    
	    Author duplicate = authorService.findByNameAndSurname(author.getName(), author.getSurname());
	    
	    if (duplicate != null && !duplicate.getId().equals(author.getId())) {
	        model.addAttribute("duplicateAuthor", "There is already an author with the same name and surname");
	        return "admin/editAuthor";
	    }

	    existingAuthor.setName(author.getName());
	    existingAuthor.setSurname(author.getSurname());
	    existingAuthor.setNationality(author.getNationality());
	    existingAuthor.setDateOfBirth(author.getDateOfBirth());
	    existingAuthor.setDateOfDeath(author.getDateOfDeath());
	    existingAuthor.setInfos(author.getInfos());
	    
	    if (authorPhoto != null && !authorPhoto.isEmpty()) {
	        try {
	            Image image = new Image();
	            image.setData(authorPhoto.getBytes());
	            existingAuthor.setAuthorPhoto(image);
	        } catch (Exception e) {
	            model.addAttribute("uploadError", "Error rendering the image");
	            return "admin/editAuthor";
	        }
	    }

	    authorService.save(existingAuthor);

	    return "redirect:/author/" + existingAuthor.getId();
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("admin/editAuthor/{authorId}/books")
	public String manageAuthorBooks(@PathVariable("authorId") Long authorId, Model model) {
		List<Book> availableBooks = bookService.findBooksNotInAuthor(authorId);
		
		model.addAttribute("availableBooks", availableBooks);
		model.addAttribute("author", authorService.findById(authorId));
		
		return "admin/manageAuthorBooks";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("admin/removeBookFromAuthor/{authorId}/{bookId}")
	public String removeBoomFromAuthor(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId, Model model) {
		
		Book book = bookService.findById(bookId);
		Author author = authorService.findById(authorId);
		
		List<Author> authors = book.getAuthors();
		authors.remove(author);
		
		bookService.save(book);
		
		return "redirect:/admin/editAuthor/" + authorId + "/books";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("admin/addBookToAuthor/{authorId}/{bookId}")
	public String addBookToAuthor(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId, Model model) {
		
		Book book = bookService.findById(bookId);
		Author author = authorService.findById(authorId);
		
		List<Author> authors = book.getAuthors();
		authors.add(author);
		
		bookService.save(book);
		
		return "redirect:/admin/editAuthor/" + authorId + "/books";
	}
}
