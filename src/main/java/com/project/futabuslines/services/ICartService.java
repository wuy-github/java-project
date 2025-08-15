package com.project.futabuslines.services;

import com.project.futabuslines.dtos.CartDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Cart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICartService {
    Cart addCart(CartDTO cartDTO) throws DataNotFoundException;
    List<Cart> getCartByUserId(long userId);
    void deleteCart(long id);
}
