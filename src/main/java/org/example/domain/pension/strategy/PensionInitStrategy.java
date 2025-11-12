package org.example.domain.pension.strategy;

import org.example.domain.pension.Pension;
import java.util.List;

public interface PensionInitStrategy {
    List<Pension> initializeList();
}
