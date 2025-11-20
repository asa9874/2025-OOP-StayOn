package org.example.domain.user.pensionManager;

import org.example.domain.user.pensionManager.dto.PensionManagerRequestDTO;
import java.util.List;
import java.util.NoSuchElementException;

public class PensionManagerService {
    private static PensionManagerService instance;
    private final PensionManagerRepository pensionManagerRepository;

    private PensionManagerService(PensionManagerRepository pensionManagerRepository) {
        this.pensionManagerRepository = pensionManagerRepository;
    }

    public static void initialize(PensionManagerRepository pensionManagerRepository) {
        if (instance == null) {
            instance = new PensionManagerService(pensionManagerRepository);
        }
    }

    public static PensionManagerService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PensionManagerService가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    public PensionManager findById(int id) {
        return pensionManagerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ID가 " + id + "인 펜션 관리자를 찾을 수 없습니다."));
    }

    public PensionManager findByAccountId(String accountId) {
        return pensionManagerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NoSuchElementException("계정 ID가 " + accountId + "인 펜션 관리자를 찾을 수 없습니다."));
    }

    public List<PensionManager> findAll() {
        return pensionManagerRepository.findAll();
    }

    public PensionManager save(PensionManagerRequestDTO requestDTO) {
        PensionManager newManager = new PensionManager(
                requestDTO.name(),
                requestDTO.accountId(),
                requestDTO.password(),
                requestDTO.phone(),
                requestDTO.email()
        );
        return pensionManagerRepository.save(newManager);
    }

    public void deleteById(int id) {
        findById(id);
        pensionManagerRepository.deleteById(id);
    }

    public PensionManager login(String accountId, String password) {
        PensionManager manager = pensionManagerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NoSuchElementException("계정 ID가 " + accountId + "인 펜션 관리자를 찾을 수 없습니다."));
        
        if (!manager.login(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        
        return manager;
    }
}
