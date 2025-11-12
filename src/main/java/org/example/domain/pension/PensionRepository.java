package org.example.domain.pension;

import org.example.domain.pension.strategy.PensionInitStrategy;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PensionRepository {
    private static PensionRepository instance;
    private List<Pension> pensionList;
    private final PensionInitStrategy initStrategy;
    private int nextId = 1;

    private PensionRepository(PensionInitStrategy initStrategy) {
        this.initStrategy = initStrategy;
        this.pensionList = initStrategy.initializeList();
        updateNextId();
    }

    private void updateNextId() {
        int maxId = pensionList.stream()
                .mapToInt(Pension::getId)
                .max()
                .orElse(0);
        nextId = maxId + 1;
    }

    public static void initialize(PensionInitStrategy initStrategy) {
        if (instance == null) {
            instance = new PensionRepository(initStrategy);
        }
    }

    public static PensionRepository getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PensionRepository가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    public List<Pension> findAll() {
        return pensionList;
    }

    public Optional<Pension> findById(int id) {
        return pensionList.stream()
                .filter(pension -> pension.getId() == id)
                .findFirst();
    }

    public List<Pension> findByPensionManagerId(int pensionManagerId) {
        return pensionList.stream()
                .filter(pension -> pension.getPensionManagerId() == pensionManagerId)
                .collect(Collectors.toList());
    }

    public Pension save(Pension pension) {
        pension.setId(nextId++);
        pensionList.add(pension);
        return pension;
    }

    public Pension update(Pension pension) {
        Optional<Pension> existing = findById(pension.getId());
        if (existing.isPresent()) {
            Pension existingPension = existing.get();
            existingPension.setName(pension.getName());
            existingPension.setAddress(pension.getAddress());
            existingPension.setPhoneNumber(pension.getPhoneNumber());
            existingPension.setDescription(pension.getDescription());
            return existingPension;
        }
        return null;
    }

    public void deleteById(int id) {
        pensionList.removeIf(pension -> pension.getId() == id);
    }

    public void delete(Pension pension) {
        deleteById(pension.getId());
    }

    public void returnToDefaultData() {
        this.pensionList = initStrategy.initializeList();
        updateNextId();
    }
}
