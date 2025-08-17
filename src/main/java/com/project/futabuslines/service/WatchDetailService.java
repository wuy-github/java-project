package com.project.futabuslines.service;

import com.project.futabuslines.dtos.UpdateWatchDetailDTO;
import com.project.futabuslines.dtos.WatchDetailDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.*;
import com.project.futabuslines.repositories.WatchDetailRepository;
import com.project.futabuslines.repositories.WatchImageRepository;
import com.project.futabuslines.repositories.WatchRepository;
import com.project.futabuslines.responses.WatchDetailViewResponse;
import com.project.futabuslines.responses.WatchDetailUserViewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WatchDetailService implements IWatchDetailService{
    private final WatchRepository watchRepository;
    private final WatchDetailRepository watchDetailRepository;
    private final FavoriteRepository favoriteRepository;
    private final WatchImageRepository watchImageRepository;

    @Override
    public WatchDetail createWatchDetail(WatchDetailDTO watchDetailDTO) throws DataNotFoundException {
        Watch watch = watchRepository.findById(watchDetailDTO.getWatchId())
                .orElseThrow(()-> new DataNotFoundException("Cannot found watch with id = " + watchDetailDTO.getWatchId()));
        WatchDetail newWatch = WatchDetail.builder()
                .watch(watch)
                .gender(watchDetailDTO.getGender())
                .shape(watchDetailDTO.getShape())
                .movementType(watchDetailDTO.getMovementType())
                .style(watchDetailDTO.getStyle())
                .glassType(watchDetailDTO.getGlassType())
                .diameter(watchDetailDTO.getDiameter())
                .caseMaterial(watchDetailDTO.getCaseMaterial())
                .bandMaterial(watchDetailDTO.getBandMaterial())
                .waterResistance(watchDetailDTO.getWaterResistance())
                .features(watchDetailDTO.getFeatures())
                .dialColor(watchDetailDTO.getDialColor())
                .origin(watchDetailDTO.getOrigin())
                .build();
        return watchDetailRepository.save(newWatch);
    }

    @Override
    public List<WatchDetail> getAllWatchDetails() {
        return watchDetailRepository.findAll();
    }

    @Override
    public WatchDetail getWatchDetailByWatchId(long watchId) throws DataNotFoundException {
        return watchDetailRepository.findByWatchId(watchId)
                .orElseThrow(()->new DataNotFoundException("Cannot found watch or not empty"));
    }

    @Override
    public WatchDetailViewResponse getWatchDetailView(long watchId) throws DataNotFoundException {
        WatchDetail details = watchDetailRepository.findByWatchId(watchId)
                .orElseThrow(() -> new DataNotFoundException("Watch not found"));
        return WatchDetailViewResponse.fromWatchDetail(details);
    }


    @Override
    public WatchDetailUserViewResponse getWatchDetailForUser(Long watchId, Long userId) throws DataNotFoundException {
        WatchDetail watchDetail = watchDetailRepository.findByWatchId(watchId)
                .orElseThrow(() -> new DataNotFoundException("Watch detail not found"));

        boolean isFavorite = false;

        if (userId != null) {
            isFavorite = favoriteRepository.findByUserIdAndIsActiveTrue(userId)
                    .stream()
                    .anyMatch(fav -> fav.getWatch().getId().equals(watchDetail.getWatch().getId()));
        }

        return WatchDetailUserViewResponse.fromWatchDetail(watchDetail, isFavorite);
    }




    @Override
    public WatchDetail getWatchDetailById(long id) throws DataNotFoundException {
        return watchDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found or empty"));
    }

    @Override
    public WatchDetail updateWatchDetail(long id, UpdateWatchDetailDTO watchDetailDTO) throws DataNotFoundException {
        WatchDetail existingWatchDetail = getWatchDetailById(id);

        existingWatchDetail.setGender(watchDetailDTO.getGender());
        existingWatchDetail.setShape(watchDetailDTO.getShape());
        existingWatchDetail.setMovementType(watchDetailDTO.getMovementType());
        existingWatchDetail.setStyle(watchDetailDTO.getStyle());
        existingWatchDetail.setGlassType(watchDetailDTO.getGlassType());
        existingWatchDetail.setDiameter(watchDetailDTO.getDiameter());
        existingWatchDetail.setCaseMaterial(watchDetailDTO.getCaseMaterial());
        existingWatchDetail.setBandMaterial(watchDetailDTO.getBandMaterial());
        existingWatchDetail.setWaterResistance(watchDetailDTO.getWaterResistance());
        existingWatchDetail.setFeatures(watchDetailDTO.getFeatures());
        existingWatchDetail.setDialColor(watchDetailDTO.getDialColor());
        existingWatchDetail.setOrigin(watchDetailDTO.getOrigin());

        return watchDetailRepository.save(existingWatchDetail);
    }

    @Override
    public WatchDetail updatePartial(Long id, Map<String, Object> updates) throws DataNotFoundException {
        WatchDetail existing = getWatchDetailById(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "gender" -> existing.setGender((String) value);
                case "shape" -> existing.setShape((String) value);
                case "movementType" -> existing.setMovementType((String) value);
                case "style" -> existing.setStyle((String) value);
                case "glassType" -> existing.setGlassType((String) value);
                case "diameter" -> existing.setDiameter((String) value);
                case "caseMaterial" -> existing.setCaseMaterial((String) value);
                case "bandMaterial" -> existing.setBandMaterial((String) value);
                case "waterResistance" -> existing.setWaterResistance((String) value);
                case "features" -> existing.setFeatures((String) value);
                case "dialColor" -> existing.setDialColor((String) value);
                case "origin" -> existing.setOrigin((String) value);
            }
        });

        return watchDetailRepository.save(existing);
    }


    @Override
    public void deleteWatchDetail(long id) throws DataNotFoundException {
        watchDetailRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Watch Id cannot found or not empty"));
        watchDetailRepository.deleteById(id);
    }




}
