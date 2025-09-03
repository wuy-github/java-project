package com.project.futabuslines.services;

import com.project.futabuslines.dtos.UpdateWatchDetailDTO;
import com.project.futabuslines.dtos.WatchDetailDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.WatchDetail;
import com.project.futabuslines.responses.WatchDetailResponse;
import com.project.futabuslines.responses.WatchDetailViewResponse;
import com.project.futabuslines.responses.WatchDetailUserViewResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IWatchDetailService {
    WatchDetailResponse createWatchDetail(WatchDetailDTO watchDetailDTO) throws DataNotFoundException;
    List<WatchDetail> getAllWatchDetails();
    WatchDetailResponse getWatchDetailByWatchId(long watchId) throws DataNotFoundException;
    WatchDetailResponse getWatchDetailById(long id) throws DataNotFoundException;
    WatchDetailViewResponse getWatchDetailView(long watchId) throws DataNotFoundException;
    WatchDetailUserViewResponse getWatchDetailForUser(Long watchDetailId, Long userId) throws DataNotFoundException;
    WatchDetail updateWatchDetail(long id, UpdateWatchDetailDTO watchDetailDTO) throws DataNotFoundException;
    WatchDetail updatePartial(Long id, Map<String, Object> updates) throws DataNotFoundException;
    void deleteWatchDetail(long id) throws DataNotFoundException;


}
