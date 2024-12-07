package com.example.demo;

import com.example.demo.User;
import com.example.demo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Sign-up a new user
    public String registerUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return "Email already registered!";
        }

        userRepository.save(user);  // Save user without encrypting the password
        return "User registered successfully!";
    }

    // Sign-in a user
    public Optional<User> authenticateUser(String email, String password) {
        return userRepository.findByEmail(email).filter(user -> user.getPassword().equals(password));
    }

    // Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

	public List<User> getAllUsers() {
		 return userRepository.findAll();
	}

	public void deleteUser(Long id) {
        userRepository.deleteById(id); // Delete user by id from the repository
    }

}
