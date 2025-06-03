package com.prayersync.backend.service;

import com.prayersync.backend.dto.CreatePrayerRequestDto;
import com.prayersync.backend.dto.PrayerRequestDto;
import com.prayersync.backend.entity.PrayerList;
import com.prayersync.backend.entity.PrayerRequest;
import com.prayersync.backend.entity.User;
import com.prayersync.backend.enums.RequestStatus;
import com.prayersync.backend.repository.PrayerListRepository;
import com.prayersync.backend.repository.PrayerRequestRepository;
import com.prayersync.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrayerRequestService {

    @Autowired
    private PrayerRequestRepository prayerRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrayerListRepository prayerListRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PrayerRequestDto createPrayerRequest(CreatePrayerRequestDto createDto, String creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PrayerRequest prayerRequest = new PrayerRequest();
        prayerRequest.setTitle(createDto.getTitle());
        prayerRequest.setDescription(createDto.getDescription());
        prayerRequest.setCategory(createDto.getCategory());
        prayerRequest.setPriority(createDto.getPriority());
        prayerRequest.setPrivacyLevel(createDto.getPrivacyLevel());
        prayerRequest.setIsAnonymous(createDto.getIsAnonymous());
        prayerRequest.setCreator(creator);

        if (createDto.getPrayerListId() != null) {
            PrayerList prayerList = prayerListRepository.findById(createDto.getPrayerListId())
                    .orElseThrow(() -> new RuntimeException("Prayer list not found"));
            prayerRequest.setPrayerList(prayerList);
        }

        PrayerRequest savedPrayerRequest = prayerRequestRepository.save(prayerRequest);
        return convertToDto(savedPrayerRequest);
    }

    public PrayerRequestDto getPrayerRequestById(String id) {
        PrayerRequest prayerRequest = prayerRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prayer request not found"));
        return convertToDto(prayerRequest);
    }

    public List<PrayerRequestDto> getAllActivePrayerRequests() {
        List<PrayerRequest> prayerRequests = prayerRequestRepository.findActivePrayerRequests();
        return prayerRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PrayerRequestDto> getPrayerRequestsByCreator(String creatorId) {
        List<PrayerRequest> prayerRequests = prayerRequestRepository.findActiveByCreator(creatorId);
        return prayerRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PrayerRequestDto> getPrayerRequestsByPrayerList(String prayerListId) {
        List<PrayerRequest> prayerRequests = prayerRequestRepository.findByPrayerListId(prayerListId);
        return prayerRequests.stream()
                .filter(pr -> pr.getStatus() == RequestStatus.ACTIVE)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PrayerRequestDto> getPrayerRequestsByChurch(String churchId) {
        List<PrayerRequest> prayerRequests = prayerRequestRepository.findActiveByChurch(churchId);
        return prayerRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PrayerRequestDto> getPublicPrayerRequests() {
        List<PrayerRequest> prayerRequests = prayerRequestRepository.findPublicPrayerRequests();
        return prayerRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PrayerRequestDto> getAccessiblePrayerRequests(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String churchId = user.getChurch() != null ? user.getChurch().getId() : null;
        List<PrayerRequest> prayerRequests = prayerRequestRepository.findAccessiblePrayerRequests(userId, churchId);
        
        return prayerRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PrayerRequestDto> getUrgentPrayerRequests() {
        List<PrayerRequest> prayerRequests = prayerRequestRepository.findUrgentPrayerRequests();
        return prayerRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PrayerRequestDto> searchPrayerRequests(String searchTerm) {
        List<PrayerRequest> prayerRequests = prayerRequestRepository.searchByTitleOrDescription(searchTerm);
        return prayerRequests.stream()
                .filter(pr -> pr.getStatus() == RequestStatus.ACTIVE)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PrayerRequestDto> getAnsweredPrayerRequests(LocalDateTime fromDate) {
        List<PrayerRequest> prayerRequests = prayerRequestRepository.findAnsweredSince(fromDate);
        return prayerRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PrayerRequestDto updatePrayerRequest(String id, CreatePrayerRequestDto updateDto, String userId) {
        PrayerRequest prayerRequest = prayerRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prayer request not found"));

        // Check if user is the creator
        if (!prayerRequest.getCreator().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to update this prayer request");
        }

        prayerRequest.setTitle(updateDto.getTitle());
        prayerRequest.setDescription(updateDto.getDescription());
        prayerRequest.setCategory(updateDto.getCategory());
        prayerRequest.setPriority(updateDto.getPriority());
        prayerRequest.setPrivacyLevel(updateDto.getPrivacyLevel());
        prayerRequest.setIsAnonymous(updateDto.getIsAnonymous());

        if (updateDto.getPrayerListId() != null) {
            PrayerList prayerList = prayerListRepository.findById(updateDto.getPrayerListId())
                    .orElseThrow(() -> new RuntimeException("Prayer list not found"));
            prayerRequest.setPrayerList(prayerList);
        }

        PrayerRequest updatedPrayerRequest = prayerRequestRepository.save(prayerRequest);
        return convertToDto(updatedPrayerRequest);
    }

    public PrayerRequestDto markAsAnswered(String id, String answeredDescription, String userId) {
        PrayerRequest prayerRequest = prayerRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prayer request not found"));

        // Check if user is the creator
        if (!prayerRequest.getCreator().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to mark this prayer request as answered");
        }

        prayerRequest.markAsAnswered(answeredDescription);
        PrayerRequest updatedPrayerRequest = prayerRequestRepository.save(prayerRequest);
        return convertToDto(updatedPrayerRequest);
    }

    public PrayerRequestDto incrementPrayerCount(String id) {
        PrayerRequest prayerRequest = prayerRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prayer request not found"));

        prayerRequest.incrementPrayerCount();
        PrayerRequest updatedPrayerRequest = prayerRequestRepository.save(prayerRequest);
        return convertToDto(updatedPrayerRequest);
    }

    public void deletePrayerRequest(String id, String userId) {
        PrayerRequest prayerRequest = prayerRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prayer request not found"));

        // Check if user is the creator or church admin
        if (!prayerRequest.getCreator().getId().equals(userId) && 
            !isUserChurchAdmin(userId, prayerRequest)) {
            throw new RuntimeException("You don't have permission to delete this prayer request");
        }

        prayerRequest.setStatus(RequestStatus.NO_LONGER_NEEDED);
        prayerRequestRepository.save(prayerRequest);
    }

    private boolean isUserChurchAdmin(String userId, PrayerRequest prayerRequest) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || !user.getIsChurchAdmin()) {
            return false;
        }
        
        if (prayerRequest.getPrayerList() != null && 
            prayerRequest.getPrayerList().getChurch() != null) {
            return user.getChurch() != null && 
                   user.getChurch().getId().equals(prayerRequest.getPrayerList().getChurch().getId());
        }
        
        return false;
    }

    private PrayerRequestDto convertToDto(PrayerRequest prayerRequest) {
        PrayerRequestDto dto = modelMapper.map(prayerRequest, PrayerRequestDto.class);
        dto.setCreatorId(prayerRequest.getCreator().getId());
        
        if (prayerRequest.getIsAnonymous()) {
            dto.setCreatorName("Anonymous");
        } else {
            dto.setCreatorName(prayerRequest.getCreator().getFullName());
        }
        
        if (prayerRequest.getPrayerList() != null) {
            dto.setPrayerListId(prayerRequest.getPrayerList().getId());
            dto.setPrayerListName(prayerRequest.getPrayerList().getName());
        }
        
        return dto;
    }
}