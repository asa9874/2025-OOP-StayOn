package org.example.domain.pension.strategy;

import org.example.domain.pension.Pension;
import java.util.ArrayList;
import java.util.List;

public class EmptyPensionListStrategy implements PensionInitStrategy {
    @Override
    public List<Pension> initializeList() {
        return new ArrayList<>();
    }
}
