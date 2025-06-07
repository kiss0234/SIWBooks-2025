package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;

@Service
public class AuthService {

	@Autowired CredentialsRepository credentialsRepository;
	
    public Credentials getCurrentCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails user = (UserDetails) authentication.getPrincipal();
            return credentialsRepository.findByUsername(user.getUsername()).get();
        }
        return null;
    }

    public User getCurrentUser() {
        Credentials credentials = getCurrentCredentials();
        return credentials != null ? credentials.getUser() : null;
    }
}
