package com.stores.stridestar.services;

import com.stores.stridestar.models.Cart;
import com.stores.stridestar.models.CartItem;
import com.stores.stridestar.models.Product;
import com.stores.stridestar.repositories.CartRepository;
import com.stores.stridestar.repositories.ProductRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Transactional
    public void addToCart(long userId, Product product, Long variantId, int quantity) {
        // Tìm cart của userId từ cơ sở dữ liệu
        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            // Nếu không tìm thấy cart, tạo mới cart
            cart = new Cart();
            cart.setUserId(userId);
            cart = cartRepository.save(cart); // Lưu cart vào cơ sở dữ liệu và nhận cart đã lưu lại
        }

        // Tạo mới một CartItem và thêm vào cart
        CartItem item = new CartItem();
        item.setCart(cart); // Thiết lập cart cho CartItem
        item.setQuantity(quantity);
        item.setProduct(product);
        item.setVariantId(variantId);

        cart.addItem(item); // Thêm CartItem vào danh sách items của cart

        // Lưu lại cart sau khi thêm CartItem vào
        cartRepository.save(cart);
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }
    public List<Cart> getCartItems() {
        return cartRepository.findAll();
    }
    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId).orElse(null);
    }

    // Other methods as needed
}
