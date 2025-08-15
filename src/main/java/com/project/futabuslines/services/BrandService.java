package com.project.futabuslines.services;

import com.project.futabuslines.dtos.BrandDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.ResourceAlreadyExistsException;
import com.project.futabuslines.models.Brand;
import com.project.futabuslines.repositories.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService implements IBrandService{
    private final BrandRepository brandRepository;

    @Override
    // Ham tao moi brand
    // Viec tao moi brand co the them o hai vai tro admin hoac seller
    // Seller se gui yeu cau len admin de co the tao moi brand
    // Admin co the duyet hoac tham khao Appraiser
    public Brand createBrand(BrandDTO brandDTO) throws ResourceAlreadyExistsException {
        boolean exists = brandRepository.existsByNameIgnoreCase(brandDTO.getName());
        if (exists) {
            throw new ResourceAlreadyExistsException("Category with name '" + brandDTO.getName() + "' already exists.");
        }
        Brand newBrand = Brand.builder()
                .name(brandDTO.getName())
                .build();
        return brandRepository.save(newBrand);
    }

    @Override
    // Lay toan bo danh sach brand
    public List<Brand> getAllBrand() {
        return brandRepository.findAll();
    }

    @Override
    // Lay brand theo id
    public Brand getBrandById(long id) throws DataNotFoundException {
        return brandRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Brand's Id cannot found or not empty"));
    }

    @Override
    // Cap nhat brand
    public Brand updateBrand(long id, BrandDTO brandDTO) throws ResourceAlreadyExistsException, DataNotFoundException {
        boolean exists = brandRepository.existsByNameIgnoreCase(brandDTO.getName());
        if (exists) {
            throw new ResourceAlreadyExistsException("Category with name '" + brandDTO.getName() + "' already exists.");
        }
        Brand existingBrand = getBrandById(id);
        existingBrand.setName(brandDTO.getName());
        return brandRepository.save(existingBrand);
    }

    @Override
    // Xoa brand
    public void deleteBrand(long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Brand's Id cannot found or not empty"));
        brandRepository.deleteById(id);
    }
}
