package com.prayersync.backend.service;

import com.prayersync.backend.entity.*;
import com.prayersync.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    private SyncLogRepository syncLogRepository;

    public SyncResult downloadChanges(String userId, LocalDateTime lastSyncTime) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SyncLog syncLog = new SyncLog(userId, "DOWNLOAD");
        syncLogRepository.save(syncLog);

        try {
            // Get changes since last sync
            List<PrayerRequest> updatedRequests = prayerRequestRepository
                    .findUpdatedSinceLastSync(userId, lastSyncTime);
            List<PrayerList> updatedLists = prayerListRepository
                    .findUpdatedSinceLastSync(userId, lastSyncTime);

            // Get deleted items
            List<PrayerRequest> deletedRequests = prayerRequestRepository.findDeletedByUser(userId);
            List<PrayerList> deletedLists = prayerListRepository.findDeletedByUser(userId);

            // Update user's last sync time
            user.updateLastSyncTime();
            userRepository.save(user);

            // Complete sync log
            syncLog.setItemsDownloaded(updatedRequests.size() + updatedLists.size());
            syncLog.markAsCompleted();
            syncLogRepository.save(syncLog);

            return new SyncResult(updatedRequests, updatedLists, deletedRequests, deletedLists);

        } catch (Exception e) {
            syncLog.markAsFailed(e.getMessage());
            syncLogRepository.save(syncLog);
            throw e;
        }
    }

    public void uploadChanges(String userId, List<PrayerRequest> requests, List<PrayerList> lists) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SyncLog syncLog = new SyncLog(userId, "UPLOAD");
        syncLogRepository.save(syncLog);

        try {
            int uploaded = 0;
            int conflicts = 0;

            // Process prayer requests
            for (PrayerRequest request : requests) {
                // Ensure creator_id is set to the authenticated user for new requests
                if (request.getCreatorId() == null || request.getCreatorId().isEmpty()) {
                    request.setCreatorId(userId);
                }
                
                if (handlePrayerRequestConflict(request)) {
                    conflicts++;
                }
                prayerRequestRepository.save(request);
                uploaded++;
            }

            // Process prayer lists
            for (PrayerList list : lists) {
                // Ensure creator_id is set to the authenticated user for new lists
                if (list.getCreatorId() == null || list.getCreatorId().isEmpty()) {
                    list.setCreatorId(userId);
                }
                
                if (handlePrayerListConflict(list)) {
                    conflicts++;
                }
                prayerListRepository.save(list);
                uploaded++;
            }

            // Update user's last sync time
            user.updateLastSyncTime();
            userRepository.save(user);

            // Complete sync log
            syncLog.setItemsUploaded(uploaded);
            syncLog.setConflictsResolved(conflicts);
            syncLog.markAsCompleted();
            syncLogRepository.save(syncLog);

        } catch (Exception e) {
            syncLog.markAsFailed(e.getMessage());
            syncLogRepository.save(syncLog);
            throw e;
        }
    }

    private boolean handlePrayerRequestConflict(PrayerRequest incomingRequest) {
        if (incomingRequest.getId() == null) {
            // New item, no conflict
            incomingRequest.setSyncId(UUID.randomUUID().toString());
            return false;
        }

        PrayerRequest existingRequest = prayerRequestRepository.findById(incomingRequest.getId()).orElse(null);
        if (existingRequest == null) {
            // Item doesn't exist on server, treat as new
            incomingRequest.setSyncId(UUID.randomUUID().toString());
            return false;
        }

        // Check for conflicts (last-write-wins strategy)
        if (existingRequest.getUpdatedAt().isAfter(incomingRequest.getUpdatedAt())) {
            // Server version is newer, keep server version (conflict resolved)
            return true;
        }

        // Client version is newer or same, accept client version
        incomingRequest.setSyncId(existingRequest.getSyncId());
        return false;
    }

    private boolean handlePrayerListConflict(PrayerList incomingList) {
        if (incomingList.getId() == null) {
            // New item, no conflict
            incomingList.setSyncId(UUID.randomUUID().toString());
            return false;
        }

        PrayerList existingList = prayerListRepository.findById(incomingList.getId()).orElse(null);
        if (existingList == null) {
            // Item doesn't exist on server, treat as new
            incomingList.setSyncId(UUID.randomUUID().toString());
            return false;
        }

        // Check for conflicts (last-write-wins strategy)
        if (existingList.getUpdatedAt().isAfter(incomingList.getUpdatedAt())) {
            // Server version is newer, keep server version (conflict resolved)
            return true;
        }

        // Client version is newer or same, accept client version
        incomingList.setSyncId(existingList.getSyncId());
        return false;
    }

    public static class SyncResult {
        private final List<PrayerRequest> updatedRequests;
        private final List<PrayerList> updatedLists;
        private final List<PrayerRequest> deletedRequests;
        private final List<PrayerList> deletedLists;

        public SyncResult(List<PrayerRequest> updatedRequests, List<PrayerList> updatedLists,
                         List<PrayerRequest> deletedRequests, List<PrayerList> deletedLists) {
            this.updatedRequests = updatedRequests;
            this.updatedLists = updatedLists;
            this.deletedRequests = deletedRequests;
            this.deletedLists = deletedLists;
        }

        public List<PrayerRequest> getUpdatedRequests() { return updatedRequests; }
        public List<PrayerList> getUpdatedLists() { return updatedLists; }
        public List<PrayerRequest> getDeletedRequests() { return deletedRequests; }
        public List<PrayerList> getDeletedLists() { return deletedLists; }
    }
}