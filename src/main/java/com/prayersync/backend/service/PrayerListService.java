package com.prayersync.backend.service;

import com.prayersync.backend.dto.CreatePrayerListDto;
import com.prayersync.backend.dto.PrayerListDto;
import com.prayersync.backend.entity.Church;
import com.prayersync.backend.entity.PrayerList;
import com.prayersync.backend.entity.User;
import com.prayersync.backend.repository.ChurchRepository;
import com.prayersync.backend.repository.PrayerListRepository;
import com.prayersync.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrayerListService {

    @Autowired
    private PrayerListRepository prayerListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChurchRepository churchRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PrayerListDto createPrayerList(CreatePrayerListDto createDto, String creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PrayerList prayerList = new PrayerList();
        prayerList.setName(createDto.getName());
        prayerList.setDescription(createDto.getDescription());
        prayerList.setPrivacyLevel(createDto.getPrivacyLevel());
        prayerList.setCreator(creator);

        if (createDto.getChurchId() != null) {
            Church church = churchRepository.findById(createDto.getChurchId())
                    .orElseThrow(() -> new RuntimeException("Church not found"));
            prayerList.setChurch(church);
        }

        PrayerList savedPrayerList = prayerListRepository.save(prayerList);
        return convertToDto(savedPrayerList);
    }

    public PrayerListDto getPrayerListById(String id) {
        PrayerList prayerList = prayerListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prayer list not found"));
        return convertToDto(prayerList);
    }

    public List<PrayerListDto> getAllPrayerLists() {
        List<PrayerList> prayerLists = prayerListRepository.findByIsActiveTrue();
        return prayerLists.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PrayerListDto> getPrayerListsByCreator(String creatorId) {
        List<PrayerList> prayerLists = prayerListRepository.findActiveByCreator(creatorId);
        return prayerLists.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PrayerListDto> getPrayerListsByChurch(String churchId) {
        List<PrayerList> prayerLists = prayerListRepository.findActiveByChurch(churchId);
        return prayerLists.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PrayerListDto> getPublicPrayerLists() {
        List<PrayerList> prayerLists = prayerListRepository.findPublicPrayerLists();
        return prayerLists.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PrayerListDto> getAccessiblePrayerLists(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String churchId = user.getChurch() != null ? user.getChurch().getId() : null;
        List<PrayerList> prayerLists = prayerListRepository.findAccessiblePrayerLists(userId, churchId);
        
        return prayerLists.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PrayerListDto> searchPrayerLists(String searchTerm) {
        List<PrayerList> prayerLists = prayerListRepository.searchByName(searchTerm);
        return prayerLists.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PrayerListDto updatePrayerList(String id, CreatePrayerListDto updateDto, String userId) {
        PrayerList prayerList = prayerListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prayer list not found"));

        // Check if user is the creator or church admin
        if (!prayerList.getCreator().getId().equals(userId) && 
            !isUserChurchAdmin(userId, prayerList.getChurch().getId())) {
            throw new RuntimeException("You don't have permission to update this prayer list");
        }

        prayerList.setName(updateDto.getName());
        prayerList.setDescription(updateDto.getDescription());
        prayerList.setPrivacyLevel(updateDto.getPrivacyLevel());

        if (updateDto.getChurchId() != null) {
            Church church = churchRepository.findById(updateDto.getChurchId())
                    .orElseThrow(() -> new RuntimeException("Church not found"));
            prayerList.setChurch(church);
        }

        PrayerList updatedPrayerList = prayerListRepository.save(prayerList);
        return convertToDto(updatedPrayerList);
    }

    public void deletePrayerList(String id, String userId) {
        PrayerList prayerList = prayerListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prayer list not found"));

        // Check if user is the creator or church admin
        if (!prayerList.getCreator().getId().equals(userId) && 
            !isUserChurchAdmin(userId, prayerList.getChurch().getId())) {
            throw new RuntimeException("You don't have permission to delete this prayer list");
        }

        prayerList.setIsActive(false);
        prayerListRepository.save(prayerList);
    }

    private boolean isUserChurchAdmin(String userId, String churchId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null && user.getIsChurchAdmin() && 
               user.getChurch() != null && user.getChurch().getId().equals(churchId);
    }

    private PrayerListDto convertToDto(PrayerList prayerList) {
        PrayerListDto dto = modelMapper.map(prayerList, PrayerListDto.class);
        dto.setCreatorId(prayerList.getCreator().getId());
        dto.setCreatorName(prayerList.getCreator().getFullName());
        
        if (prayerList.getChurch() != null) {
            dto.setChurchId(prayerList.getChurch().getId());
            dto.setChurchName(prayerList.getChurch().getName());
        }
        
        dto.setRequestCount(prayerList.getPrayerRequests().size());
        return dto;
    }
}