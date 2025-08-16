package com.project.futabuslines.services;

import com.project.futabuslines.dtos.AppraisalDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.ResourceAlreadyExistsException;
import com.project.futabuslines.models.Appraisal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAppraisalService {
    Appraisal createAppraisal(AppraisalDTO appraisalDTO) throws DataNotFoundException, ResourceAlreadyExistsException;
    List<Appraisal> getAllAppraisal();
    Appraisal getAppraisalById(long id) throws DataNotFoundException;
    List<Appraisal> getAppraisalByUserId(long userId);
    List<Appraisal> getAppraisalByWatchId(long watchId);
    Appraisal updateAppraisal(long id, AppraisalDTO appraisalDTO) throws DataNotFoundException;
    void deleteAppraisal(long id);
}
