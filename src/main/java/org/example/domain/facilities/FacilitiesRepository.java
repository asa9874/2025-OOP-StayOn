package org.example.domain.facilities;

import org.example.domain.facilities.strategy.FacilitiesInitStrategy;
import java.util.List;
import java.util.Optional;

public class FacilitiesRepository {
    private static FacilitiesRepository instance;
    private List<Facilities> facilitiesList;
    private final FacilitiesInitStrategy initStrategy;
    private int nextId = 1;

    // 생성자
    private FacilitiesRepository(FacilitiesInitStrategy initStrategy) {
        this.initStrategy = initStrategy;
        this.facilitiesList = initStrategy.initializeList();
        updateNextId();
    }

    // 다음 ID 업데이트 메서드
    private void updateNextId() {
        int maxId = facilitiesList.stream()
                .mapToInt(Facilities::getId)
                .max()
                .orElse(0);
        nextId = maxId + 1;
    }

    public static void initialize(FacilitiesInitStrategy initStrategy) {
        if (instance == null) {
            instance = new FacilitiesRepository(initStrategy);
        }
    }

    public static FacilitiesRepository getInstance() {
        if (instance == null) {
            throw new IllegalStateException("FacilitiesRepository가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    // 여기부터 CRUD 메서드
    public List<Facilities> findAll() {
        return facilitiesList;
    }

    public Optional<Facilities> findById(int id) {
        return facilitiesList.stream()
                .filter(facility -> facility.getId() == id)
                .findFirst();
    }

    public List<Facilities> findByPensionId(int pensionId) {
        return facilitiesList.stream()
                .filter(facility -> facility.getPension().getId() == pensionId)
                .toList();
    }

    public Facilities save(Facilities facilities) {
        facilities.setId(nextId++);
        facilitiesList.add(facilities);
        return facilities;
    }

    public void deleteById(int id) {
        facilitiesList.removeIf(facility -> facility.getId() == id);
    }

    public void delete(Facilities facilities) {
        deleteById(facilities.getId());
    }

    public void returnToDefaultData() {
        this.facilitiesList = initStrategy.initializeList();
        updateNextId();
    }
}
