package com.project.futabuslines.services;

import com.project.futabuslines.dtos.AppraisalDTO;
import com.project.futabuslines.dtos.AppraisalReportDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.ResourceAlreadyExistsException;
import com.project.futabuslines.models.Appraisal;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.Watch;
import com.project.futabuslines.repositories.AppraisalRepository;
import com.project.futabuslines.responses.AppraisalResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppraisalService implements IAppraisalService{
    private final AppraisalRepository appraisalRepository;
    private final EntityFinder entityFinder;
    private ModelMapper modelMapper;

    @Override
    public AppraisalResponse createAppraisal(AppraisalDTO appraisalDTO, Long userId) throws DataNotFoundException, ResourceAlreadyExistsException {
        User user = entityFinder.findUserById(userId);
        Watch watch = entityFinder.findWatchById(appraisalDTO.getWatchId());

        Optional<Appraisal> existingAppraisal = appraisalRepository.findByUserAndWatch(user, watch);

        if(existingAppraisal.isPresent()) {
            throw new ResourceAlreadyExistsException("Da tham dinh san pham nay roi!");
        }
        Appraisal appraisal = Appraisal.builder()
                .user(user)
                .watch(watch)
                .appraisalValue(appraisalDTO.getAppraisalValue())
                .authenticity(appraisalDTO.isAuthenticity())
                .watchCondition(appraisalDTO.getWatchCondition())
                .isActive(false)
                .build();
        Appraisal saved = appraisalRepository.save(appraisal);
        return AppraisalResponse.fromAppraisal(saved);
    }

    @Override
    public Appraisal uploadAppraisalReport(long appraisalId, AppraisalReportDTO appraisalReportDTO) throws Exception {
        Appraisal appraisal = entityFinder.findAppraisalById(appraisalId);
        appraisal.setAppraisalReport(appraisalReportDTO.getAppraisalReport());
        return appraisalRepository.save(appraisal);
    }

    @Override
    public List<AppraisalResponse> getAllAppraisal() {
        List<Appraisal> appraisals = appraisalRepository.findAll();
        return convertToResponseList(appraisals);
    }

    @Override
    public AppraisalResponse getAppraisalById(long id) throws DataNotFoundException {
        Appraisal appraisal = entityFinder.findAppraisalById(id);
        return AppraisalResponse.fromAppraisal(appraisal);
    }

    @Override
    public List<AppraisalResponse> getAppraisalByUserId(long userId) {
        List<Appraisal> appraisals = appraisalRepository.findByUserId(userId);
        return convertToResponseList(appraisals);
    }

    @Override
    public List<AppraisalResponse> getAppraisalByWatchId(long watchId) {
        List<Appraisal> appraisals = appraisalRepository.findByWatchId(watchId);
        return convertToResponseList(appraisals);
    }

    @Override
    public AppraisalResponse updateAppraisal(long id, AppraisalDTO appraisalDTO, Long userId) throws DataNotFoundException {
        Appraisal existingAppraisal = entityFinder.findAppraisalById(id);
        entityFinder.findUserById(userId);

        existingAppraisal.setAppraisalValue(appraisalDTO.getAppraisalValue());
        existingAppraisal.setAuthenticity(appraisalDTO.isAuthenticity());
        existingAppraisal.setWatchCondition(appraisalDTO.getWatchCondition());
        appraisalRepository.save(existingAppraisal);
        return AppraisalResponse.fromAppraisal(existingAppraisal);
    }

    @Override
    public void deleteAppraisal(long id) throws DataNotFoundException {
        entityFinder.findAppraisalById(id);
        appraisalRepository.deleteById(id);
    }

    @Override
    public Appraisal findById(Long id) throws DataNotFoundException {
        return entityFinder.findAppraisalById(id);
    }

    private List<AppraisalResponse> convertToResponseList(List<Appraisal> appraisals) {
        return appraisals.stream()
                .map(AppraisalResponse::fromAppraisal)
                .collect(Collectors.toList());
    }

}
