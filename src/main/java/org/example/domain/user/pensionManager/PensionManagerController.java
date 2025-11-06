package org.example.domain.user.pensionManager;

import org.example.domain.user.pensionManager.dto.PensionManagerRequestDTO;
import java.util.List;

public class PensionManagerController {
    private static PensionManagerController instance;
    private final PensionManagerService pensionManagerService;
    
    private PensionManagerController(PensionManagerService pensionManagerService) {
        this.pensionManagerService = pensionManagerService;
    }
    
    public static void initialize(PensionManagerService pensionManagerService) {
        if (instance == null) {
            instance = new PensionManagerController(pensionManagerService);
        }
    }

    public static PensionManagerController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PensionManagerController가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    public PensionManager findById(int id) {
        return pensionManagerService.findById(id);
    }

    public PensionManager findByAccountId(String accountId) {
        return pensionManagerService.findByAccountId(accountId);
    }

    public PensionManager save(PensionManagerRequestDTO requestDTO) {
        return pensionManagerService.save(requestDTO);
    }

    public List<PensionManager> findAll() {
        return pensionManagerService.findAll();
    }

    public void deleteById(int id) {
        pensionManagerService.deleteById(id);
    }

    public PensionManager login(String accountId, String password) {
        return pensionManagerService.login(accountId, password);
    }
}
