package com.project.futabuslines.services;

import com.project.futabuslines.dtos.FavoriteDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.ResourceAlreadyExistsException;
import com.project.futabuslines.models.Favorite;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.Watch;
import com.project.futabuslines.repositories.FavoriteRepository;
import com.project.futabuslines.repositories.UserRepository;
import com.project.futabuslines.repositories.WatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService implements IFavoriteService{

    private final FavoriteRepository favoriteRepository;
    private final WatchRepository watchRepository;
    private final UserRepository userRepository;

    @Override
    public Favorite addFavorite(FavoriteDTO favoriteDTO) throws DataNotFoundException, ResourceAlreadyExistsException {
        User existingUser = userRepository.findById(favoriteDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found user with id = " +favoriteDTO.getUserId()));

        Watch existingWatch = watchRepository.findById(favoriteDTO.getWatchId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found watch with id = " +favoriteDTO.getWatchId()));

        Optional<Favorite> existingFavorite = favoriteRepository.findByUserAndWatch(existingUser, existingWatch);
        if (existingFavorite.isPresent()) {
            throw new ResourceAlreadyExistsException("Đã lưu yêu thích sản phẩm này rồi.");
        }

        Favorite favorite = Favorite.builder()
                .user(existingUser)
                .watch(existingWatch)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();

        return favoriteRepository.save(favorite);
    }


    @Override
    public List<Favorite> getFavoriteByUserId(long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    @Override
    public void deleteFavorite(long id) {
        favoriteRepository.deleteById(id);
    }
}