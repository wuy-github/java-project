package com.project.futabuslines.services;

import com.project.futabuslines.dtos.CartDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Cart;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.Watch;
import com.project.futabuslines.repositories.CartRepository;
import com.project.futabuslines.repositories.UserRepository;
import com.project.futabuslines.repositories.WatchRepository;
import com.project.futabuslines.responses.CartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
    private final CartRepository cartRepository;
    private final WatchRepository watchRepository;
    private final UserRepository userRepository;
    private final EntityFinder entityFinder;

    @Override
    public CartResponse addCart(CartDTO cartDTO, Long userId) throws DataNotFoundException {
        User user = entityFinder.findUserById(userId);
        Watch watch = entityFinder.findWatchById(cartDTO.getWatchId());

        if (cartDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        if(watch.getQuantity() == 0){
            throw new IllegalArgumentException("Watch is sold out");
        }

        if (watch.getQuantity() < cartDTO.getQuantity()) {
            throw new IllegalArgumentException("Not enough quantity in stock");
        }

        Optional<Cart> optionalCart = cartRepository.findByUserIdAndWatchId(user.getId(), watch.getId());
        Cart cart;

        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
            int newQuantity = cart.getQuantity() + cartDTO.getQuantity();

            if (watch.getQuantity() < newQuantity) {
                throw new IllegalArgumentException("Not enough quantity in stock after update");
            }

            cart.setQuantity(newQuantity);
        } else {
            cart = Cart.builder()
                    .user(user)
                    .watch(watch)
                    .quantity(cartDTO.getQuantity())
                    .isActive(true)
                    .build();
        }

        cartRepository.save(cart);
        return CartResponse.fromCart(cart);
    }

    @Override
    public List<CartResponse> getCartByUserId(long userId) {
        List<Cart> cart = cartRepository.findByUserId(userId);
        return cart.stream()
                .map(CartResponse::fromCart)
                .collect(Collectors.toList());
    }
    @Override
    public CartResponse decreaseQuantity(CartDTO cartDTO, Cart cart) throws DataNotFoundException {
        if (cartDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Decrease Quantity must be greater than 0");
        }
        int currentQuantity = cart.getQuantity();
        if (currentQuantity <= cartDTO.getQuantity()) {
            cartRepository.deleteById(cart.getId());
            return null;
        } else {
            cart.setQuantity(currentQuantity - cartDTO.getQuantity());
            cartRepository.save(cart);
            return CartResponse.fromCart(cart);
        }
    }

    @Override
    public void deleteCart(long id) throws DataNotFoundException {
        entityFinder.findCartById(id);
        cartRepository.deleteById(id);
    }
}
