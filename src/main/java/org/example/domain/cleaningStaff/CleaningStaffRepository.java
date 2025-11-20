package org.example.domain.cleaningStaff;

import org.example.domain.cleaningStaff.strategy.CleaningStaffInitStrategy;
import org.example.domain.cleaningStaff.strategy.EmptyListStrategy;
import java.util.List;
import java.util.Optional;

public class CleaningStaffRepository {
    private static CleaningStaffRepository instance;
    private List<CleaningStaff> cleaningStaffList;
    private final CleaningStaffInitStrategy initStrategy;
    private int nextId = 1;

    // 생성자
    private CleaningStaffRepository(CleaningStaffInitStrategy initStrategy) {
        this.initStrategy = initStrategy;
        this.cleaningStaffList = initStrategy.initializeList();
        updateNextId();
    }

    // 다음 ID 업데이트 메서드
    private void updateNextId() {
        int maxId = cleaningStaffList.stream()
                .mapToInt(CleaningStaff::getId)
                .max()
                .orElse(0);
        nextId = maxId + 1;
    }

    public static void initialize(CleaningStaffInitStrategy initStrategy) {
        if (instance == null) {
            instance = new CleaningStaffRepository(initStrategy);
        }
    }

    public static CleaningStaffRepository getInstance() {
        if (instance == null) {
            throw new IllegalStateException("CleaningStaffRepository가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    // 여기부터 CRUD 메서드
    public List<CleaningStaff> findAll() {
        return cleaningStaffList;
    }

    public Optional<CleaningStaff> findById(int id) {
        return cleaningStaffList.stream()
                .filter(staff -> staff.getId() == id)
                .findFirst();
    }

    public CleaningStaff save(CleaningStaff cleaningStaff) {
        cleaningStaff.setId(nextId++);
        cleaningStaffList.add(cleaningStaff);
        return cleaningStaff;
    }

    public void deleteById(int id) {
        cleaningStaffList.removeIf(staff -> staff.getId() == id);
    }

    public void returnToDefaultData() {
        this.cleaningStaffList = initStrategy.initializeList();
        updateNextId();
    }

}