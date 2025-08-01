package com.project.futabuslines.services;

import com.project.futabuslines.dtos.BrandDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.ResourceAlreadyExistsException;
import com.project.futabuslines.models.Brand;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IBrandService {
    Brand createBrand(BrandDTO brandDTO) throws ResourceAlreadyExistsException;
    List<Brand> getAllBrand();
    Brand getBrandById(long id) throws DataNotFoundException;
    Brand updateBrand(long id, BrandDTO brandDTO) throws ResourceAlreadyExistsException, DataNotFoundException;
    void deleteBrand(long id);
}
