package com.project.futabuslines.service;

import com.project.futabuslines.dtos.UpdateWatchDTO;
import com.project.futabuslines.dtos.WatchDTO;
import com.project.futabuslines.dtos.WatchImageDTO;
import com.project.futabuslines.enums.WatchStatus;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Watch;
import com.project.futabuslines.models.WatchImage;
import com.project.futabuslines.responses.WatchUserViewResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IWatchService {
    Watch createWatch(WatchDTO watchDTO) throws DataNotFoundException;
    List<Watch> getAllWatch();
    Watch getWatchById(long id) throws DataNotFoundException;
    Watch updateWatch(long id, UpdateWatchDTO watchDTO) throws DataNotFoundException;
    void deleteWatch(long id) throws DataNotFoundException;
    void updateStatus(long id, WatchStatus status) throws DataNotFoundException;
    List<WatchUserViewResponse> getAllWatchesForUser(Long userId);
    List<WatchUserViewResponse> getAllWatches(Long userId);
    WatchImage uploadWatchImage(long watchId, WatchImageDTO watchImageDTO) throws Exception;
}
