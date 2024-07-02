package com.stores.stridestar.controllers.api;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.stores.stridestar.extensions.ResourceNotFoundException;
import com.stores.stridestar.models.CartItem;
import com.stores.stridestar.models.Order;
import com.stores.stridestar.models.OrderDetail;
import com.stores.stridestar.models.ProductVariant;
import com.stores.stridestar.models.User;
import com.stores.stridestar.models.VariantAttribute;
import com.stores.stridestar.models.enums.OrderStatus;
import com.stores.stridestar.models.enums.Payment;
import com.stores.stridestar.services.CartService;
import com.stores.stridestar.services.OrderDetailService;
import com.stores.stridestar.services.OrderService;
import com.stores.stridestar.services.UserService;
import com.stores.stridestar.services.VNPayService;
import com.stores.stridestar.services.VariantAttributeService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/cart")
public class CartApiController {
    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private VariantAttributeService attributeService;

    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/add/{id}")
    public ResponseEntity<CartItem> addItem(@PathVariable("id") Long id, @RequestParam("quantity") int quantity, Authentication authentication) {
        if(authentication == null) {
            return new ResponseEntity<CartItem>(HttpStatus.UNAUTHORIZED);
        }

        if (quantity <= 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Product with ID does not exist."));
        
        VariantAttribute variantAttribute = attributeService.getVariantAttributeById(id).orElseThrow(() -> new ResourceNotFoundException("Product with ID does not exist."));

        List<CartItem> cartItems = cartService.getCartItems(user);

        for (CartItem cartItem : cartItems) {
            if (cartItem.getProductVariant().getId().equals(variantAttribute.getProductVariant().getId())) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartService.saveCartItem(cartItem);
                return new ResponseEntity<CartItem>(cartItem, HttpStatus.OK);
            }
        }

        CartItem cartItem = new CartItem();
        cartItem.setUserId(user.getId());
        cartItem.setQuantity(quantity);
        cartItem.setProductVariant(variantAttribute.getProductVariant());
        
        cartService.addCartItem(cartItem);
        return new ResponseEntity<CartItem>(cartItem, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CartItem>> list(Authentication authentication) {
        if(authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Product with ID does not exist."));
        
        List<CartItem> cartItems = cartService.getCartItems(user);
        return new ResponseEntity<List<CartItem>>(cartItems, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CartItem> deleteItem(@PathVariable("id") Long id, Authentication authentication) {
        if(authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Product with ID does not exist."));
        
        CartItem cartItem = cartService.getCartItemById(id);
        if(cartItem == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(cartItem.getUserId() != user.getId()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        cartService.deleteCartItem(id);
        return new ResponseEntity<CartItem>(cartItem, HttpStatus.OK);
    }

    @PostMapping("/updateQuantity/{id}")
    public ResponseEntity<CartItem> updateQuantity(@PathVariable("id") Long id, @RequestParam("quantity") int quantity, Authentication authentication) {
        if(authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (quantity <= 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Product with ID does not exist."));
        
        CartItem cartItem = cartService.getCartItemById(id);
        if(cartItem == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(cartItem.getUserId() != user.getId()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        cartItem.setQuantity(quantity);
        cartService.saveCartItem(cartItem);
        return new ResponseEntity<CartItem>(cartItem, HttpStatus.OK);
    }

    @DeleteMapping("/removeAll")
    public ResponseEntity<List<CartItem>> deleteAll(Authentication authentication) {
        if(authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Product with ID does not exist."));
        
        List<CartItem> cartItems = cartService.getCartItems(user);
        cartItems.forEach(cartItem -> cartService.deleteCartItem(cartItem.getId()));
        return new ResponseEntity<List<CartItem>>(cartItems, HttpStatus.OK);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody Order order, Authentication authentication, HttpServletRequest request) {
        if(authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Product with ID does not exist."));
        // Assuming 'item' is an instance of a class that contains 'productVariant'
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDetails(new ArrayList<>());
        order.setPaymentStatus(false);
        order.setCreatedDate(java.time.LocalDateTime.now());
        Order newOrder = orderService.saveOrder(order);
        
        cartService.getCartItems(user).forEach(cartItem -> {
            OrderDetail orderDetail = new OrderDetail();

            ProductVariant productVariant = cartItem.getProductVariant();

            String colors = productVariant.getVariantAttributes().stream()
                .filter(attribute -> "Màu sắc".equals(attribute.getProductAttributeValue().getProductAttribute().getName()))
                .map(attribute -> attribute.getProductAttributeValue().getValue())
                .collect(Collectors.joining(", "));

            String sizes = productVariant.getVariantAttributes().stream()
                .filter(attribute -> "Size".equals(attribute.getProductAttributeValue().getProductAttribute().getName()))
                .map(attribute -> attribute.getProductAttributeValue().getValue())
                .collect(Collectors.joining(", "));

            String result = colors + " - " + sizes;

            orderDetail.setName(productVariant.getProduct().getName());
            orderDetail.setAvatar(productVariant.getProduct().getImage());
            orderDetail.setCategory(productVariant.getProduct().getCategory().getName());

            orderDetail.setOrder(newOrder);
            orderDetail.setAttributes(result);
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getProductVariant().getPrice() * cartItem.getQuantity());
            orderDetailService.saveOrderDetail(orderDetail);
            cartService.deleteCartItem(cartItem.getId());
        });

        if(order.getPayment().equals(Payment.VNPAY)) {
            return ResponseEntity.status(HttpStatus.OK).body(getVnPayLink(request, order.getId(), order.getTotalPrice()));
        }

        return ResponseEntity.status(HttpStatus.OK).body("/cart/checkout/completed?orderId=" + order.getId() + "&orderDate=" + order.getCreatedDate());
    }

    public String getVnPayLink(HttpServletRequest request, long orderId, double price) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(request, price, orderId, baseUrl);
        return vnpayUrl;
    }

    @GetMapping("/vnpay-payment-return")
    public RedirectView paymentCompleted(@RequestParam("vnp_ResponseCode") String vnp_ResponseCode,
                                        @RequestParam("vnp_TxnRef") String vnp_TxnRef) {
        Long orderId = Long.parseLong(vnp_TxnRef);
        Order order = orderService.getOrderById(orderId);
        String redirectUrl;
        if (vnp_ResponseCode.equals("00")) {
            order.setPaymentStatus(true);
            orderService.saveOrder(order);
            redirectUrl = "/cart/checkout/completed?orderId=" + orderId + "&orderDate=" + order.getCreatedDate();
        } else {
            order.getOrderDetails().forEach(orderDetail -> {
                orderDetailService.deleteOrderDetail(orderDetail.getId());
            });
            orderService.deleteOrder(orderId);
            redirectUrl = "/payment-failed";
        }
        return new RedirectView(redirectUrl);
    }
}