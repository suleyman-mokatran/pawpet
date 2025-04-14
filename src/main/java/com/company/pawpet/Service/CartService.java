package com.company.pawpet.Service;

import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Model.Cart;
import com.company.pawpet.Model.CartItem;
import com.company.pawpet.Repository.CartRepository;
import com.company.pawpet.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AppUserService appUserService;

    @Autowired
    UserRepository userRepository;

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public Optional<Cart> getCartById(int id) {
        return cartRepository.findById(id);
    }

    public Cart saveCart(int id) {
        AppUser appUser = appUserService.getUserById(id).orElseThrow();

            Cart newCart = new Cart();
            newCart.setAppUser(appUser);
            newCart.setCartItemList(new ArrayList<>());
            newCart.setCartTotalPrice(0.0);
            return cartRepository.save(newCart);

    }

    public Cart updateCart(int id, Cart cart) {
        Cart existingCart = cartRepository.findById(id).orElseThrow();

        existingCart.setCartItemList(cart.getCartItemList());

        double total = 0.0;
        if(existingCart.getCartItemList() == null || existingCart.getCartItemList().isEmpty()){
            total = 0.0;
        }
        else {
            for (CartItem item : existingCart.getCartItemList()) {
                total += item.getTotalPrice();
            }
        }
        existingCart.setCartTotalPrice(total);
        return cartRepository.save(existingCart);
    }

    public void deleteCart(int id) {
        cartRepository.deleteById(id);
    }
}
