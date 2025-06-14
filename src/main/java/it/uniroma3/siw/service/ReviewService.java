package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ReviewRepository;
@Service
public class ReviewService {

	@Autowired ReviewRepository reviewRepository;
	
	public boolean userHasReviewedBook(Long bookId, Long userId) {
		return reviewRepository.existsByBookReviewedIdAndUserId(bookId, userId);
	}

	public void save(Review review) {
		reviewRepository.save(review);
	}

	public void deleteById(Long reviewId) {
		reviewRepository.deleteById(reviewId);
	}
}
