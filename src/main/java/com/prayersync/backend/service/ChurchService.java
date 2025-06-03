package com.prayersync.backend.service;

import com.prayersync.backend.dto.ChurchDto;
import com.prayersync.backend.dto.CreateChurchDto;
import com.prayersync.backend.entity.Church;
import com.prayersync.backend.repository.ChurchRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChurchService {

    @Autowired
    private ChurchRepository churchRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ChurchDto createChurch(CreateChurchDto createChurchDto) {
        Church church = modelMapper.map(createChurchDto, Church.class);
        Church savedChurch = churchRepository.save(church);
        return convertToDto(savedChurch);
    }

    public ChurchDto getChurchById(String id) {
        Church church = churchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Church not found"));
        return convertToDto(church);
    }

    public List<ChurchDto> getAllChurches() {
        List<Church> churches = churchRepository.findAllOrderByCreatedAtDesc();
        return churches.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ChurchDto> searchChurches(String searchTerm) {
        List<Church> churches = churchRepository.searchByName(searchTerm);
        return churches.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ChurchDto> getChurchesNearLocation(String location) {
        List<Church> churches = churchRepository.findByLocationContaining(location);
        return churches.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ChurchDto updateChurch(String id, CreateChurchDto updateDto) {
        Church church = churchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Church not found"));

        church.setName(updateDto.getName());
        church.setDescription(updateDto.getDescription());
        church.setAddress(updateDto.getAddress());
        church.setPhone(updateDto.getPhone());
        church.setEmail(updateDto.getEmail());
        church.setWebsite(updateDto.getWebsite());

        Church updatedChurch = churchRepository.save(church);
        return convertToDto(updatedChurch);
    }

    public void deleteChurch(String id) {
        Church church = churchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Church not found"));
        churchRepository.delete(church);
    }

    private ChurchDto convertToDto(Church church) {
        ChurchDto dto = modelMapper.map(church, ChurchDto.class);
        dto.setMemberCount(church.getMembers().size());
        return dto;
    }
}