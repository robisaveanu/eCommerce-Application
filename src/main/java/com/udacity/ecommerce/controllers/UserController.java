package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.model.persistence.Cart;
import com.udacity.ecommerce.model.persistence.User;
import com.udacity.ecommerce.model.persistence.repositories.CartRepository;
import com.udacity.ecommerce.model.persistence.repositories.UserRepository;
import com.udacity.ecommerce.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository, CartRepository cartRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        log.info("Username used to find user is ", username);

        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        log.info("Username set with {}", createUserRequest.getUsername());

        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);

        boolean passwordLengthSatisfied = createUserRequest.getPassword().length() >= 7;
        boolean passwordEqualsConfirm = createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword());
        if (!passwordLengthSatisfied || !passwordEqualsConfirm) {
            if (!passwordLengthSatisfied && !passwordEqualsConfirm) {
                log.error("CreateUser failure: Password does not equal confirmPassword. Password length less than required length of 7 characters. Cannot create user {}. ", createUserRequest.getUsername());
            } else if (!passwordEqualsConfirm) {
                log.error("CreateUser failure: Password does not equal confirmPassword. Cannot create user {}. ", createUserRequest.getUsername());
            } else {
                log.error("CreateUser failure: Password length less than required length of 7 characters. Cannot create user {}. ", createUserRequest.getUsername());
            }
            return ResponseEntity.badRequest().build();
        }

        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(user);
        log.info("CreateUser success: User {} created with requested username, cart, and encoded password", createUserRequest.getUsername());
        return ResponseEntity.ok(user);
    }

}
