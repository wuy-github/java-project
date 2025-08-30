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
import com.project.futabuslines.responses.FavoriteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService implements IFavoriteService{

    private final FavoriteRepository favoriteRepository;
    private final WatchRepository watchRepository;
    private final UserRepository userRepository;
    private final EntityFinder entityFinder;

    @Override
    public FavoriteResponse addFavorite(FavoriteDTO favoriteDTO, Long userId) throws DataNotFoundException, ResourceAlreadyExistsException {
        User user = entityFinder.findUserById(userId);
        Watch watch = entityFinder.findWatchById(favoriteDTO.getWatchId());

        Optional<Favorite> existingFavorite = favoriteRepository.findByUserAndWatch(user, watch);
        if (existingFavorite.isPresent()) {
            throw new ResourceAlreadyExistsException("Đã lưu yêu thích sản phẩm này rồi.");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .watch(watch)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();

        favoriteRepository.save(favorite);
        return FavoriteResponse.fromFavorite(favorite);
    }


    @Override
    public List<FavoriteResponse> getFavoriteByUserId(long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        return favorites.stream()
                .map(FavoriteResponse::fromFavorite)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFavorite(long id) {
        favoriteRepository.deleteById(id);
    }
}
