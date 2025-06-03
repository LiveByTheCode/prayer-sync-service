package com.prayersync.backend.service;

import com.prayersync.backend.dto.*;
import com.prayersync.backend.entity.PrayerList;
import com.prayersync.backend.entity.PrayerRequest;
import com.prayersync.backend.entity.User;
import com.prayersync.backend.repository.PrayerListRepository;
import com.prayersync.backend.repository.PrayerRequestRepository;
import com.prayersync.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SyncService {

    @Autowired
    private PrayerRequestRepository prayerRequestRepository;

    @Autowired
    private PrayerListRepository prayerListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public SyncResponseDto performBidirectionalSync(SyncRequestDto request, String userId) {
        // First upload client changes
        SyncResponseDto uploadResponse = uploadChanges(request, userId);
        
        // Then get server changes
        SyncResponseDto downloadResponse = getChangesSince(request.getLastSyncTime(), userId);
        
        // Merge responses
        List<ConflictDto> allConflicts = new ArrayList<>();
        if (uploadResponse.getConflicts() != null) {
            allConflicts.addAll(uploadResponse.getConflicts());
        }
        if (downloadResponse.getConflicts() != null) {
            allConflicts.addAll(downloadResponse.getConflicts());
        }
        
        return new SyncResponseDto(
            LocalDateTime.now(),
            downloadResponse.getUpdatedPrayerRequests(),
            downloadResponse.getUpdatedPrayerLists(),
            downloadResponse.getDeletedPrayerRequestIds(),
            downloadResponse.getDeletedPrayerListIds(),
            allConflicts
        );
    }

    public SyncResponseDto uploadChanges(SyncRequestDto request, String userId) {
        List<ConflictDto> conflicts = new ArrayList<>();
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Process prayer request updates
        if (request.getUpdatedPrayerRequests() != null) {
            for (PrayerRequestDto dto : request.getUpdatedPrayerRequests()) {
                processRequestUpdate(dto, user, conflicts);
            }
        }

        // Process prayer list updates
        if (request.getUpdatedPrayerLists() != null) {
            for (PrayerListDto dto : request.getUpdatedPrayerLists()) {
                processListUpdate(dto, user, conflicts);
            }
        }

        // Process deletions
        if (request.getDeletedPrayerRequestIds() != null) {
            for (String id : request.getDeletedPrayerRequestIds()) {
                prayerRequestRepository.deleteById(id);
            }
        }

        if (request.getDeletedPrayerListIds() != null) {
            for (String id : request.getDeletedPrayerListIds()) {
                prayerListRepository.deleteById(id);
            }
        }

        return new SyncResponseDto(
            LocalDateTime.now(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            conflicts
        );
    }

    public SyncResponseDto getChangesSince(LocalDateTime since, String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Get updated prayer requests
        List<PrayerRequest> updatedRequests = prayerRequestRepository
            .findAccessiblePrayerRequests(userId, user.getChurch() != null ? user.getChurch().getId() : null)
            .stream()
            .filter(pr -> pr.getUpdatedAt().isAfter(since))
            .collect(Collectors.toList());

        // Get updated prayer lists
        List<PrayerList> updatedLists = prayerListRepository
            .findAccessiblePrayerLists(userId, user.getChurch() != null ? user.getChurch().getId() : null)
            .stream()
            .filter(pl -> pl.getUpdatedAt().isAfter(since))
            .collect(Collectors.toList());

        // Convert to DTOs
        List<PrayerRequestDto> requestDtos = updatedRequests.stream()
            .map(this::convertToRequestDto)
            .collect(Collectors.toList());

        List<PrayerListDto> listDtos = updatedLists.stream()
            .map(this::convertToListDto)
            .collect(Collectors.toList());

        return new SyncResponseDto(
            LocalDateTime.now(),
            requestDtos,
            listDtos,
            new ArrayList<>(), // TODO: Track deletions
            new ArrayList<>(), // TODO: Track deletions
            new ArrayList<>()
        );
    }

    private void processRequestUpdate(PrayerRequestDto dto, User user, List<ConflictDto> conflicts) {
        Optional<PrayerRequest> existingOpt = prayerRequestRepository.findById(dto.getId());
        
        if (existingOpt.isPresent()) {
            PrayerRequest existing = existingOpt.get();
            
            // Check for conflicts (simple last-write-wins for now)
            if (existing.getUpdatedAt().isAfter(dto.getUpdatedAt())) {
                conflicts.add(new ConflictDto(
                    dto.getId(),
                    ConflictDto.ConflictType.PRAYER_REQUEST,
                    ConflictDto.ConflictResolution.SERVER_WINS,
                    dto.getUpdatedAt().toString(),
                    existing.getUpdatedAt().toString(),
                    dto.getUpdatedAt(),
                    existing.getUpdatedAt(),
                    "Server version is newer"
                ));
                return;
            }
            
            // Update existing
            existing.setTitle(dto.getTitle());
            existing.setDescription(dto.getDescription());
            existing.setCategory(dto.getCategory());
            existing.setPriority(dto.getPriority());
            existing.setPrivacyLevel(dto.getPrivacyLevel());
            existing.setIsAnonymous(dto.getIsAnonymous());
            prayerRequestRepository.save(existing);
        } else {
            // Create new
            PrayerRequest newRequest = new PrayerRequest();
            newRequest.setId(dto.getId());
            newRequest.setTitle(dto.getTitle());
            newRequest.setDescription(dto.getDescription());
            newRequest.setCategory(dto.getCategory());
            newRequest.setPriority(dto.getPriority());
            newRequest.setPrivacyLevel(dto.getPrivacyLevel());
            newRequest.setIsAnonymous(dto.getIsAnonymous());
            newRequest.setCreator(user);
            newRequest.setCreatedAt(dto.getCreatedAt());
            newRequest.setUpdatedAt(dto.getUpdatedAt());
            
            if (dto.getPrayerListId() != null) {
                prayerListRepository.findById(dto.getPrayerListId())
                    .ifPresent(newRequest::setPrayerList);
            }
            
            prayerRequestRepository.save(newRequest);
        }
    }

    private void processListUpdate(PrayerListDto dto, User user, List<ConflictDto> conflicts) {
        Optional<PrayerList> existingOpt = prayerListRepository.findById(dto.getId());
        
        if (existingOpt.isPresent()) {
            PrayerList existing = existingOpt.get();
            
            // Check for conflicts (simple last-write-wins for now)
            if (existing.getUpdatedAt().isAfter(dto.getUpdatedAt())) {
                conflicts.add(new ConflictDto(
                    dto.getId(),
                    ConflictDto.ConflictType.PRAYER_LIST,
                    ConflictDto.ConflictResolution.SERVER_WINS,
                    dto.getUpdatedAt().toString(),
                    existing.getUpdatedAt().toString(),
                    dto.getUpdatedAt(),
                    existing.getUpdatedAt(),
                    "Server version is newer"
                ));
                return;
            }
            
            // Update existing
            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setPrivacyLevel(dto.getPrivacyLevel());
            existing.setIsActive(dto.getIsActive());
            prayerListRepository.save(existing);
        } else {
            // Create new
            PrayerList newList = new PrayerList();
            newList.setId(dto.getId());
            newList.setName(dto.getName());
            newList.setDescription(dto.getDescription());
            newList.setPrivacyLevel(dto.getPrivacyLevel());
            newList.setIsActive(dto.getIsActive());
            newList.setCreator(user);
            newList.setCreatedAt(dto.getCreatedAt());
            newList.setUpdatedAt(dto.getUpdatedAt());
            
            if (dto.getChurchId() != null) {
                userRepository.findById(user.getId())
                    .ifPresent(u -> newList.setChurch(u.getChurch()));
            }
            
            prayerListRepository.save(newList);
        }
    }

    private PrayerRequestDto convertToRequestDto(PrayerRequest request) {
        PrayerRequestDto dto = modelMapper.map(request, PrayerRequestDto.class);
        dto.setCreatorId(request.getCreator().getId());
        dto.setCreatorName(request.getCreator().getFullName());
        if (request.getPrayerList() != null) {
            dto.setPrayerListId(request.getPrayerList().getId());
            dto.setPrayerListName(request.getPrayerList().getName());
        }
        return dto;
    }

    private PrayerListDto convertToListDto(PrayerList list) {
        PrayerListDto dto = modelMapper.map(list, PrayerListDto.class);
        dto.setCreatorId(list.getCreator().getId());
        dto.setCreatorName(list.getCreator().getFullName());
        if (list.getChurch() != null) {
            dto.setChurchId(list.getChurch().getId());
            dto.setChurchName(list.getChurch().getName());
        }
        dto.setRequestCount(list.getPrayerRequests().size());
        return dto;
    }
}