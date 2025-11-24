package org.example.domain.cleaningStaff;

import org.example.domain.cleaningStaff.dto.CleaningStaffRequestDTO;
import java.util.List;
import java.util.NoSuchElementException;

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

    public CleaningStaff findById(int id) {
        return cleaningStaffRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ID가 " + id + "인 청소 스태프를 찾을 수 없습니다."));
    }

    public List<CleaningStaff> findAll() {
        return cleaningStaffRepository.findAll();
    }

    public CleaningStaff save(CleaningStaffRequestDTO requestDTO) {
        CleaningStaff newStaff = new CleaningStaff(requestDTO.name(), requestDTO.phoneNumber());
        return cleaningStaffRepository.save(newStaff);
    }

    public void deleteById(int id) {
        findById(id);
        cleaningStaffRepository.deleteById(id);
    }
}