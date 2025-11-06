package org.example.domain.user.pensionManager.strategy;

import org.example.domain.user.pensionManager.PensionManager;
import java.util.List;

public interface PensionManagerInitStrategy {
    List<PensionManager> initializeList();
}
