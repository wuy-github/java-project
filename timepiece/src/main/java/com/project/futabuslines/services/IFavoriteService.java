package com.project.futabuslines.services;

import com.project.futabuslines.dtos.FavoriteDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.ResourceAlreadyExistsException;
import com.project.futabuslines.models.Favorite;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IFavoriteService {
    Favorite addFavorite(FavoriteDTO favoriteDTO) throws DataNotFoundException, ResourceAlreadyExistsException;
    List<Favorite> getFavoriteByUserId(long userId);
    void deleteFavorite(long id);
}