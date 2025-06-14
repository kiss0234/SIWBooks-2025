package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AuthService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.ReviewService;
import jakarta.validation.Valid;

@Controller
public class ReviewController {

	@Autowired ReviewService reviewService;
	@Autowired AuthService authService;
	@Autowired BookService bookService;
	
	@PostMapping("/newReview/{bookId}")
	public String newReview(@PathVariable("bookId") Long bookId,
	                        @Valid @ModelAttribute("review") Review review,
	                        BindingResult bindingResult,
	                        RedirectAttributes redirectAttributes,
	                        Model model) {

	    if (bindingResult.hasErrors()) {
	        // Passa il review con gli errori e i messaggi
	        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.review", bindingResult);
	        redirectAttributes.addFlashAttribute("review", review);
	        return "redirect:/book/" + bookId;
	    }

	    Book book = bookService.findById(bookId);
	    User user = authService.getCurrentUser();
	    review.setBook(book);
	    review.setUser(user);
	    reviewService.save(review);

	    return "redirect:/book/" + bookId;
	}
	
	@PostMapping("/deleteReview/{bookId}/{reviewId}")
	public String deleteReview(@PathVariable("bookId") Long bookId, @PathVariable("reviewId") Long reviewId) {
		reviewService.deleteById(reviewId);
		return "redirect:/book/" + bookId;
	}

	
}
