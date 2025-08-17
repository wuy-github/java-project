package com.project.futabuslines.service;

import com.project.futabuslines.dtos.UpdateWatchDTO;
import com.project.futabuslines.dtos.WatchDTO;
import com.project.futabuslines.dtos.WatchImageDTO;
import com.project.futabuslines.enums.WatchStatus;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.InvalidParamException;
import com.project.futabuslines.models.*;
import com.project.futabuslines.repositories.*;
import com.project.futabuslines.responses.WatchUserViewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchService implements IWatchService{
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final WatchRepository watchRepository;
    private final FavoriteRepository favoriteRepository;
    private final WatchImageRepository watchImageRepository;

    @Override
    // Them dong ho moi
    public Watch createWatch(WatchDTO watchDTO) throws DataNotFoundException {
        Brand existingBrand = brandRepository.findById(watchDTO.getBrandId())
                .orElseThrow(()-> new DataNotFoundException("Cannot found brand with id = " + watchDTO.getBrandId()));
        Category existingCategory = categoryRepository.findById(watchDTO.getCategoryId())
                .orElseThrow(()-> new DataNotFoundException("Cannot found category with id = " + watchDTO.getCategoryId()));
        User existingUser = userRepository.findById(watchDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found user with id = " + watchDTO.getUserId()));
        Watch newWatch = Watch.builder()
                .name(watchDTO.getName())
                .user(existingUser)
                .brand(existingBrand)
                .category(existingCategory)
                .price(watchDTO.getPrice())
                .quantity(watchDTO.getQuantity())
                .status(WatchStatus.AVAILABLE.toString())
                .isActive(false)
                .build();
        return watchRepository.save(newWatch);
    }

    @Override
    public List<Watch> getAllWatch() {
        return watchRepository.findAll();
    }

    @Override
    public Watch getWatchById(long id) throws DataNotFoundException {
        return watchRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot found or empty"));
    }

    @Override
    public Watch updateWatch(long id, UpdateWatchDTO watchDTO) throws DataNotFoundException {
        Watch existingWatch = getWatchById(id);

        Brand brand = brandRepository.findById(watchDTO.getBrandId())
                .orElseThrow(() -> new DataNotFoundException("Brand not found"));

        Category category = categoryRepository.findById(watchDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Category not found"));

        existingWatch.setName(watchDTO.getName());
        existingWatch.setBrand(brand);
        existingWatch.setPrice(watchDTO.getPrice());
        existingWatch.setCategory(category);
        existingWatch.setQuantity(watchDTO.getQuantity());

        return watchRepository.save(existingWatch);
    }


    @Override
    public void deleteWatch(long id) throws DataNotFoundException {
        watchRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Watch Id cannot found or not empty"));
        watchRepository.deleteById(id);
    }

    @Override
    public void updateStatus(long id, WatchStatus status) throws DataNotFoundException {
        Watch watch = watchRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Watch Id cannot found or not empty"));
        watch.setStatus(status.getValue()); // dùng getValue() thay vì toString()
        watchRepository.save(watch);
    }

    @Override
    public List<WatchUserViewResponse> getAllWatchesForUser(Long userId) {
        List<Watch> watches = watchRepository.findAll();
        List<Favorite> favorites = favoriteRepository.findByUserIdAndIsActiveTrue(userId);

        Set<Long> favoriteWatchIds = favorites.stream()
                .map(fav -> fav.getWatch().getId())
                .collect(Collectors.toSet());

        return watches.stream()
                .map(watch -> {
                    boolean isFavorite = favoriteWatchIds.contains(watch.getId());

                    Optional<WatchImage> watchImage = watchImageRepository.findFirstByWatchId(watch.getId());
                    String imageUrl = watchImage.map(WatchImage::getImageUrl).orElse(null);

                    return WatchUserViewResponse.fromWatchView(watch, isFavorite, imageUrl);
                })
                .collect(Collectors.toList());
    }

    public List<WatchUserViewResponse> getAllWatches(Long userId) {
        List<Watch> watches = watchRepository.findAll();
        List<Favorite> favorites = userId != null ? favoriteRepository.findByUserIdAndIsActiveTrue(userId) : new ArrayList<>();
        Set<Long> favoriteWatchIds = favorites.stream()
                .map(fav -> fav.getWatch().getId())
                .collect(Collectors.toSet());

        return watches.stream().map(watch -> {
            boolean isFavorite = userId != null && favoriteWatchIds.contains(watch.getId());

            Optional<WatchImage> watchImage = watchImageRepository.findFirstByWatchId(watch.getId());
            String imageUrl = watchImage.map(WatchImage::getImageUrl).orElse(null);

            return WatchUserViewResponse.fromWatchView(watch, isFavorite, imageUrl);
        }).collect(Collectors.toList());
    }



    @Override
    public WatchImage uploadWatchImage(
            long watchId,
            WatchImageDTO watchImageDTO
    ) throws Exception {
        Watch watch = watchRepository.findById(watchId)
                .orElseThrow(() -> new DataNotFoundException("Watch Id cannot found or not empty"));
        WatchImage newWatchImage = WatchImage.builder()
                .watch(watch)
                .imageUrl(watchImageDTO.getImageUrl())
                .build();
        int size = watchImageRepository.findByWatchId(watchId).size();
        if(size >= WatchImage.MAXIMUM_IMAGES_PER_WATCH){
            throw new InvalidParamException("Number of images must be <= " + WatchImage.MAXIMUM_IMAGES_PER_WATCH);
        }
        return watchImageRepository.save(newWatchImage);
    }

}
