package org.example.domain.user.pensionManager.strategy;

import org.example.domain.user.pensionManager.PensionManager;
import java.util.ArrayList;
import java.util.List;

public class EmptyPensionManagerListStrategy implements PensionManagerInitStrategy {
    @Override
    public List<PensionManager> initializeList() {
        return new ArrayList<>();
    }
}
