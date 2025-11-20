package org.example.domain.user.pensionManager;

import org.example.domain.user.pensionManager.strategy.PensionManagerInitStrategy;
import java.util.List;
import java.util.Optional;

public class PensionManagerRepository {
    private static PensionManagerRepository instance;
    private List<PensionManager> pensionManagerList;
    private final PensionManagerInitStrategy initStrategy;
    private int nextId = 1;

    // 생성자
    private PensionManagerRepository(PensionManagerInitStrategy initStrategy) {
        this.initStrategy = initStrategy;
        this.pensionManagerList = initStrategy.initializeList();
        updateNextId();
    }

    // 다음 ID 업데이트 메서드
    private void updateNextId() {
        int maxId = pensionManagerList.stream()
                .mapToInt(PensionManager::getId)
                .max()
                .orElse(0);
        nextId = maxId + 1;
    }

    public static void initialize(PensionManagerInitStrategy initStrategy) {
        if (instance == null) {
            instance = new PensionManagerRepository(initStrategy);
        }
    }

    public static PensionManagerRepository getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PensionManagerRepository가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    // 여기부터 CRUD 메서드
    public List<PensionManager> findAll() {
        return pensionManagerList;
    }

    public Optional<PensionManager> findById(int id) {
        return pensionManagerList.stream()
                .filter(manager -> manager.getId() == id)
                .findFirst();
    }

    public Optional<PensionManager> findByAccountId(String accountId) {
        return pensionManagerList.stream()
                .filter(manager -> manager.getAccountId().equals(accountId))
                .findFirst();
    }

    public PensionManager save(PensionManager pensionManager) {
        pensionManager.setId(nextId++);
        pensionManagerList.add(pensionManager);
        return pensionManager;
    }

    public void deleteById(int id) {
        pensionManagerList.removeIf(manager -> manager.getId() == id);
    }

    public void returnToDefaultData() {
        this.pensionManagerList = initStrategy.initializeList();
        updateNextId();
    }
}
