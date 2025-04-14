package com.company.pawpet.Service;

import com.company.pawpet.Model.Cart;
import com.company.pawpet.Model.CartItem;
import com.company.pawpet.Model.Product;
import com.company.pawpet.Repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    public Optional<CartItem> getCartItemById(int id) {
        return cartItemRepository.findById(id);
    }

    public CartItem saveCartItem(int cartId,int productId,CartItem cartItem) {
        CartItem newCartItem = new CartItem();
        Product product = productService.getProductById(productId).orElseThrow();
        Cart cart = cartService.getCartById(cartId).orElseThrow();
        newCartItem.setCart(cart);
        newCartItem.setColor(cartItem.getColor());
        newCartItem.setSize(cartItem.getSize());
        newCartItem.setPrice(cartItem.getPrice());
        newCartItem.setQuantity(cartItem.getQuantity());
        newCartItem.setProduct(product);
        newCartItem.setTotalPrice(cartItem.getPrice()*cartItem.getQuantity());
        return cartItemRepository.save(newCartItem);
    }

    public CartItem updateCartItem(int cartItemId,int quantity) {
        CartItem newCartItem =cartItemRepository.findById(cartItemId).orElseThrow();
        newCartItem.setQuantity(quantity);
        newCartItem.setTotalPrice(newCartItem.getPrice()*quantity);
        return cartItemRepository.save(newCartItem);
    }



    public List<CartItem> getCartItemsByCart(int id){
        return cartItemRepository.findByCartId(id);
    }

    public void deleteCartItem(int id) {
        cartItemRepository.deleteById(id);
    }
}
