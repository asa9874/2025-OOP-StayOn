package org.example.domain.cleaningStaff;

import org.example.domain.cleaningStaff.dto.CleaningStaffRequestDTO;
import java.util.Optional;
import java.util.List;

public class CleaningStaffService {
    private static CleaningStaffService instance;
    private final CleaningStaffRepository cleaningStaffRepository;

    private CleaningStaffService(CleaningStaffRepository cleaningStaffRepository) {
        this.cleaningStaffRepository = cleaningStaffRepository;
    }

    public static void initialize(CleaningStaffRepository cleaningStaffRepository) {
        if (instance == null) {
            instance = new CleaningStaffService(cleaningStaffRepository);
        }
    }

    public static CleaningStaffService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("CleaningStaffService가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    public Optional<CleaningStaff> findById(int id) {
        return cleaningStaffRepository.findById(id);
    }

    public List<CleaningStaff> findAll() {
        return cleaningStaffRepository.findAll();
    }

    public CleaningStaff save(CleaningStaffRequestDTO requestDTO) {
        CleaningStaff newStaff = new CleaningStaff(requestDTO.name(), requestDTO.phoneNumber());
        return cleaningStaffRepository.save(newStaff);
    }

    public void deleteById(int id) {
        cleaningStaffRepository.deleteById(id);
    }
}