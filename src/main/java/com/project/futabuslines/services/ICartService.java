package com.project.futabuslines.services;

import com.project.futabuslines.dtos.CartDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Cart;
import com.project.futabuslines.responses.CartResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICartService {
    CartResponse addCart(CartDTO cartDTO, Long userId) throws DataNotFoundException;
    List<CartResponse> getCartByUserId(long userId);
    CartResponse decreaseQuantity(CartDTO cartDTO, Cart cart) throws DataNotFoundException;
    void deleteCart(long id) throws DataNotFoundException;
}
