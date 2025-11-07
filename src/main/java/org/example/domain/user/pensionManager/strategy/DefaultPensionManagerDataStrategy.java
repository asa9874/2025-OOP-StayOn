package org.example.domain.user.pensionManager.strategy;

import org.example.domain.user.pensionManager.PensionManager;
import java.util.ArrayList;
import java.util.List;

public class DefaultPensionManagerDataStrategy implements PensionManagerInitStrategy {
    @Override
    public List<PensionManager> initializeList() {
        List<PensionManager> list = new ArrayList<>();
        
        PensionManager manager1 = new PensionManager("이사장", "manager1", "admin123", "010-5555-1111", "manager1@pension.com");
        manager1.setId(1);
        
        PensionManager manager2 = new PensionManager("김관리", "manager2", "admin456", "010-5555-2222", "manager2@pension.com");
        manager2.setId(2);
        
        list.add(manager1);
        list.add(manager2);
        
        return list;
    }
}
