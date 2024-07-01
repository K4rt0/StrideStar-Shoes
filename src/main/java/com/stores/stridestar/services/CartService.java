package com.stores.stridestar.services;

import com.stores.stridestar.models.CartItem;
import com.stores.stridestar.models.User;
import com.stores.stridestar.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public List<CartItem> getCartItems(User user) {
        return cartRepository.findAll().stream().filter(cartItem -> cartItem.getUserId().equals(user.getId())).toList();
    }

    public void addCartItem(CartItem cartItem) {
        cartRepository.save(cartItem);
    }

    public CartItem saveCartItem(CartItem cartItem) {
        return cartRepository.save(cartItem);
    }

    public CartItem getCartItemById(Long id) {
        return cartRepository.findById(id).orElse(null);
    }

    public void deleteCartItem(Long id) {
        cartRepository.deleteById(id);
    }
}