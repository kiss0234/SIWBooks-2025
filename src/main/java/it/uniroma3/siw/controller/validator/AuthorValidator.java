package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.service.AuthorService;

@Component
public class AuthorValidator implements Validator{
	@Autowired
	private AuthorService authorService;

	@Override
	public void validate(Object o, Errors errors) {
		Author author = (Author)o;
		if (author.getName()!=null && author.getSurname()!=null && authorService.existsByNameAndSurname(author.getName(), author.getSurname())) {
			errors.reject("author.duplicate", "This author already exists");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Author.class.equals(aClass);
	}
}
