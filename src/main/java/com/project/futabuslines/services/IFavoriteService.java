package com.project.futabuslines.services;

import com.project.futabuslines.dtos.FavoriteDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.ResourceAlreadyExistsException;
import com.project.futabuslines.models.Favorite;
import com.project.futabuslines.responses.FavoriteResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IFavoriteService {
    FavoriteResponse addFavorite(FavoriteDTO favoriteDTO, Long userId) throws DataNotFoundException, ResourceAlreadyExistsException;
    List<FavoriteResponse> getFavoriteByUserId(long userId);
    void deleteFavorite(long id);
}
