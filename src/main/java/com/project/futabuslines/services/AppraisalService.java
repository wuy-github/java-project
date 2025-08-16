package com.project.futabuslines.services;

import com.project.futabuslines.dtos.AppraisalDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.ResourceAlreadyExistsException;
import com.project.futabuslines.models.Appraisal;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.Watch;
import com.project.futabuslines.repositories.AppraisalRepository;
import com.project.futabuslines.repositories.UserRepository;
import com.project.futabuslines.repositories.WatchRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppraisalService implements IAppraisalService{
    private final WatchRepository watchRepository;
    private final UserRepository userRepository;
    private final AppraisalRepository appraisalRepository;
    private final EntityFinder entityFinder;
    private ModelMapper modelMapper;

    @Override
    public Appraisal createAppraisal(AppraisalDTO appraisalDTO) throws DataNotFoundException, ResourceAlreadyExistsException {
        User user = entityFinder.findUserById(appraisalDTO.getUserId());
        Watch watch = entityFinder.findWatchById(appraisalDTO.getWatchId());

        Optional<Appraisal> existingAppraisal = appraisalRepository.findByUserAndWatch(user, watch);

        if(existingAppraisal.isPresent()) {
            throw new ResourceAlreadyExistsException("Da tham dinh san pham nay roi!");
        }

        Appraisal appraisal = modelMapper.map(appraisalDTO, Appraisal.class);
        appraisal.setUser(user);
        appraisal.setWatch(watch);
        return appraisalRepository.save(appraisal);
    }

    @Override
    public List<Appraisal> getAllAppraisal() {
        return appraisalRepository.findAll();
    }

    @Override
    public Appraisal getAppraisalById(long id) throws DataNotFoundException {
        return appraisalRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Khong tim thay ket qua voi id nay"));
    }

    @Override
    public List<Appraisal> getAppraisalByUserId(long userId) {
        return appraisalRepository.findByUserId(userId);
    }

    @Override
    public List<Appraisal> getAppraisalByWatchId(long watchId) {
        return appraisalRepository.findByWatchId(watchId);
    }

    @Override
    public Appraisal updateAppraisal(long id, AppraisalDTO appraisalDTO) throws DataNotFoundException {
        Appraisal existingAppraisal = getAppraisalById(id);
        User user = entityFinder.findUserById(appraisalDTO.getUserId());
        Watch watch = entityFinder.findWatchById(appraisalDTO.getWatchId());

        modelMapper.map(appraisalDTO, existingAppraisal);
        return appraisalRepository.save(existingAppraisal);
    }

    @Override
    public void deleteAppraisal(long id) {
        appraisalRepository.deleteById(id);
    }
}
