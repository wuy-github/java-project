package com.project.futabuslines.controllers;

import com.project.futabuslines.components.JwtTokenUtil;
import com.project.futabuslines.dtos.UpdateWatchDetailDTO;
import com.project.futabuslines.dtos.WatchDetailDTO;
import com.project.futabuslines.models.WatchDetail;
import com.project.futabuslines.responses.WatchDetailViewResponse;
import com.project.futabuslines.responses.WatchDetailUserViewResponse;
import com.project.futabuslines.service.IWatchDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/watch-detail")
@RequiredArgsConstructor
public class WatchDetailController {
    private final IWatchDetailService watchDetailService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("create-watch-detail")
    public ResponseEntity<?> createWatchDetail(
            @Valid @RequestBody WatchDetailDTO watchDetailDTO,
            BindingResult result
    ){
        if (result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        try {
            WatchDetail watchDetail = watchDetailService.createWatchDetail(watchDetailDTO);
            return ResponseEntity.ok(watchDetail);
        }
        catch (DataIntegrityViolationException e) {
            String errorMessage = "This watch already has detail information. Please edit instead of creating a new one.";
            return ResponseEntity.badRequest().body(errorMessage);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get-all-details")
    public ResponseEntity<List<WatchDetail>> getAllWatchDetail(){
        List<WatchDetail> watchDetails = watchDetailService.getAllWatchDetails();
        return ResponseEntity.ok((watchDetails));
    }

    @GetMapping("get-detail/{id}")
    public ResponseEntity<?> getWatchDetailById(@PathVariable long id){
        try {
            WatchDetail watch = watchDetailService.getWatchDetailById(id);
            return ResponseEntity.ok(watch);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get-watch-detail/{watchId}")
    public ResponseEntity<?> getWatchDetailByWatchId(@PathVariable long watchId){
        try {
            WatchDetail watchDetail = watchDetailService.getWatchDetailByWatchId(watchId);
            return ResponseEntity.ok(watchDetail);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get-watch-detail-view/{watchId}")
    public ResponseEntity<?> getWatchDetailView(@PathVariable long watchId){
        try {
            WatchDetailViewResponse watchDetail = watchDetailService.getWatchDetailView(watchId);
            return ResponseEntity.ok(watchDetail);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get-watch-detail-user-view/{watchId}")
    public ResponseEntity<?> getWatchDetailUserView(
            @PathVariable("watchId") Long watchId,
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            Long userId = null;

            if (token != null && token.startsWith("Bearer ")) {
                String jwt = token.substring(7);
                userId = jwtTokenUtil.extractUserId(jwt);
            }

            WatchDetailUserViewResponse watchDetail = watchDetailService.getWatchDetailForUser(watchId, userId);
            return ResponseEntity.ok(watchDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @PutMapping("update-watch-detail/{id}")
    public ResponseEntity<?> updateWatchDetail(
            @PathVariable long id,
            @Valid @RequestBody UpdateWatchDetailDTO watchDetailDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        try {
            WatchDetail watchDetail = watchDetailService.updateWatchDetail(id, watchDetailDTO);
            return ResponseEntity.ok(watchDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("update-watch-detail/{id}")
    public ResponseEntity<?> updatePartialWatchDetail(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            WatchDetail updated = watchDetailService.updatePartial(id, updates);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("delete-watch-detail/{id}")
    public ResponseEntity<String> deteteDetailWatch(
            @PathVariable long id
    ){
        try {
            watchDetailService.deleteWatchDetail(id);
            return ResponseEntity.ok("Delete Watch Detail Successfully");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
