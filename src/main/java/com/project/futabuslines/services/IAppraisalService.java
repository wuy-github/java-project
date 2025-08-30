package com.project.futabuslines.services;

import com.project.futabuslines.dtos.AppraisalDTO;
import com.project.futabuslines.dtos.AppraisalReportDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.ResourceAlreadyExistsException;
import com.project.futabuslines.models.Appraisal;
import com.project.futabuslines.responses.AppraisalResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAppraisalService {
    AppraisalResponse createAppraisal(AppraisalDTO appraisalDTO, Long userId) throws DataNotFoundException, ResourceAlreadyExistsException;
    List<AppraisalResponse> getAllAppraisal();
    AppraisalResponse getAppraisalById(long id) throws DataNotFoundException;
    List<AppraisalResponse> getAppraisalByUserId(long userId);
    List<AppraisalResponse> getAppraisalByWatchId(long watchId);
    AppraisalResponse updateAppraisal(long id, AppraisalDTO appraisalDTO, Long userId) throws DataNotFoundException;
    void deleteAppraisal(long id) throws DataNotFoundException;
    Appraisal uploadAppraisalReport(long appraisalId, AppraisalReportDTO appraisalReportDTO) throws Exception;
    Appraisal findById(Long id) throws DataNotFoundException;
}
