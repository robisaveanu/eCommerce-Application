package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.model.persistence.Cart;
import com.udacity.ecommerce.model.persistence.Item;
import com.udacity.ecommerce.model.persistence.User;
import com.udacity.ecommerce.model.persistence.repositories.CartRepository;
import com.udacity.ecommerce.model.persistence.repositories.ItemRepository;
import com.udacity.ecommerce.model.persistence.repositories.UserRepository;
import com.udacity.ecommerce.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CartControllerTest {
    private CartController cartController;
    private final ItemRepository itemRepo = mock(ItemRepository.class);
    private final CartRepository cartRepo = mock(CartRepository.class);
    private final UserRepository userRepo = mock(UserRepository.class);

    @BeforeEach
    public void setUp() {
        cartController = new CartController(userRepo, cartRepo, itemRepo);
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        List<Item> items = new ArrayList<>();
        for (long i = 0L; i < 3L; i++) {
            Item item = new Item();
            item.setId(i);
            item.setDescription("test");
            item.setName("test");
            item.setPrice(new BigDecimal("2.99"));
            cart.addItem(item);
        }
        cart.setItems(items);
        user.setCart(cart);

        return user;
    }

    private Item createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setDescription("test");
        item.setName("test");
        item.setPrice(new BigDecimal("2.99"));

        return item;
    }

    private ModifyCartRequest createModifyCartRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(3);
        modifyCartRequest.setUsername("test");

        return modifyCartRequest;
    }

    @Test
    public void validateAddToCart() {
        when(userRepo.findByUsername("test")).thenReturn(createUser());
        when(itemRepo.findById(1L)).thenReturn(Optional.of(createItem()));

        ResponseEntity<Cart> response = cartController.addToCart(createModifyCartRequest());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart actualCart = response.getBody();
        assertNotNull(actualCart);
        assertEquals("test", actualCart.getUser().getUsername());
    }

    @Test
    public void validateRemoveFromCart() {
        when(userRepo.findByUsername("test")).thenReturn(createUser());
        when(itemRepo.findById(1L)).thenReturn(Optional.of(createItem()));

        ResponseEntity<Cart> response = cartController.removeFromCart(createModifyCartRequest());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart actualCart = response.getBody();
        assertNotNull(actualCart);
        assertEquals("test", actualCart.getUser().getUsername());
    }
}
