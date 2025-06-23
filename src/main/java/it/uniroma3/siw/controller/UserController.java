package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AuthService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserController {

	@Autowired AuthService authService;
	@Autowired UserService userService;
	@Autowired CredentialsService credentialsService;

	@GetMapping("/userArea")
	public String userArea(Model model) {
		model.addAttribute("user", authService.getCurrentUser());
		return "userArea";
	}

	@GetMapping("/profile/edit")
	public String getEditProfileForm(Model model) {
		model.addAttribute("user", authService.getCurrentUser());
		return "editProfile";
	}

	@PostMapping("/profile/edit")
	public String editProfile(@Valid @ModelAttribute User updatedUser, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors())
			return "editProfile";

		userService.updateUser(updatedUser);

		return "redirect:/userArea";
	}

	@GetMapping("/profile/password")
	public String getChangePasswordForm(Model model) {
		return "changePassword";
	}

	@PostMapping("/profile/password")
	public String changePassword(@RequestParam("newPassword") String newPassword,
								@RequestParam("confirmNewPassword") String confirmNewPassword, 
								Model model,
								RedirectAttributes redirectAttributes,
								HttpServletRequest request,
								HttpServletResponse response) {

		if (!newPassword.equals(confirmNewPassword)) {
			model.addAttribute("mismatchingPassword", "Password must be the same");
			return "changePassword";
		}

		Credentials currentUserCredentials = authService.getCurrentCredentials();
		credentialsService.updatePassword(currentUserCredentials, newPassword);

		new SecurityContextLogoutHandler().logout(request, response,
				SecurityContextHolder.getContext().getAuthentication());
		redirectAttributes.addFlashAttribute("passwordChanged", true);
		return "redirect:/login?password=changed";
	}
}