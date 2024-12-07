package com.example.demo;

import com.example.demo.User;
import com.example.demo.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class UserController {

    @Autowired
    private UserService userService;

    // Handle CORS preflight request for /signup
    @RequestMapping(value = "/signup", method = RequestMethod.OPTIONS)
    public void handleSignupOptions() {
        // Preflight requests are automatically handled here
    }

    // Handle CORS preflight request for /login
    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public void handleLoginOptions() {
        // Preflight requests are automatically handled here
    }

    // Sign-up API
    @PostMapping("/signup")
    public Map<String, String> signup(@RequestBody User user) {
        String message = userService.registerUser(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        response.put("role", user.getRole());  // Return the role of the user
        return response;
    }

    // Sign-in API
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<User> user = userService.authenticateUser(email, password);
        Map<String, Object> response = new HashMap<>();

        if (user.isPresent()) {
            response.put("message", "Login successful");
            response.put("token", "dummy-token");  // You can integrate JWT if needed
            response.put("role", user.get().getRole());
            response.put("name", user.get().getName());
        } else {
            response.put("message", "Invalid email or password");
        }
        return response;
    }

    // Get the user role by email
    @GetMapping("/role/{email}")
    public Map<String, String> getUserRole(@PathVariable("email") String email) {
        Optional<User> user = userService.findByEmail(email);
        Map<String, String> response = new HashMap<>();

        if (user.isPresent()) {
            response.put("role", user.get().getRole());
        } else {
            response.put("message", "User not found");
        }

        return response;
    }
    
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

}
