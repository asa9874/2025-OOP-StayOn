package org.example.domain.pension;

import org.example.domain.pension.strategy.PensionInitStrategy;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Comparator;

public class PensionRepository {
    private static PensionRepository instance;
    private List<Pension> pensionList;
    private final PensionInitStrategy initStrategy;
    private int nextId = 1;

    private PensionRepository(PensionInitStrategy initStrategy) {
        this.initStrategy = initStrategy;
        this.pensionList = initStrategy.initializeList();
        Collections.sort(pensionList, Comparator.comparingInt(Pension::getId));
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
        int low = 0;
        int high = pensionList.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            Pension midPension = pensionList.get(mid);
            if (midPension.getId() == id) {
                return Optional.of(midPension);
            } else if (midPension.getId() < id) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return Optional.empty();
    }

    public List<Pension> findByPensionManagerId(int pensionManagerId) {
        return pensionList.stream()
            .filter(pension -> pension.getPensionManagerId() == pensionManagerId)
            .collect(Collectors.toList());
    }

    public Pension save(Pension pension) {
        pension.setId(nextId++);
        pensionList.add(pension);
        Collections.sort(pensionList, Comparator.comparingInt(Pension::getId));
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
            existingPension.setImage(pension.getImage());
            return existingPension;
        }
        return null;
    }

    public void deleteById(int id) {
        pensionList.removeIf(pension -> pension.getId() == id);
        Collections.sort(pensionList, Comparator.comparingInt(Pension::getId));
    }

    public void returnToDefaultData() {
        this.pensionList = initStrategy.initializeList();
        Collections.sort(pensionList, Comparator.comparingInt(Pension::getId));
        updateNextId();
    }
}
