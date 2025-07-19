package com.project.futabuslines.service;

import com.project.futabuslines.dtos.CartDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Cart;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.Watch;
import com.project.futabuslines.repositories.CartRepository;
import com.project.futabuslines.repositories.UserRepository;
import com.project.futabuslines.repositories.WatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
    private final CartRepository cartRepository;
    private final WatchRepository watchRepository;
    private final UserRepository userRepository;
    
    @Override
    public Cart addCart(CartDTO cartDTO) throws DataNotFoundException {
        User existingUser = userRepository.findById(cartDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found user with id = " +cartDTO.getUserId()));

        Watch existingWatch = watchRepository.findById(cartDTO.getWatchId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found watch with id = " +cartDTO.getWatchId()));

        if (cartDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        // Neu het hang thi sao
        if (existingWatch.getQuantity() < cartDTO.getQuantity()) {
            throw new IllegalArgumentException("Not enough quantity in stock");
        }
        Optional<Cart> optionalCart = cartRepository.findByUserIdAndWatchId(existingUser.getId(), existingWatch.getId());

        Cart cart;

        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
            int newQuantity = cart.getQuantity() + cartDTO.getQuantity();

            if (existingWatch.getQuantity() < newQuantity) {
                throw new IllegalArgumentException("Not enough quantity in stock after update");
            }

            cart.setQuantity(newQuantity);
        } else {
            cart = Cart.builder()
                    .user(existingUser)
                    .watch(existingWatch)
                    .quantity(cartDTO.getQuantity())
                    .isActive(true)
                    .build();
        }

        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> getCartByUserId(long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public void deleteCart(long id) {
        cartRepository.deleteById(id);
    }
}
