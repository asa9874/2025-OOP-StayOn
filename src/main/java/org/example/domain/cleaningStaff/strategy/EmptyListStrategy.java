package org.example.domain.cleaningStaff.strategy;

import org.example.domain.cleaningStaff.CleaningStaff;
import java.util.ArrayList;
import java.util.List;

public class EmptyListStrategy implements CleaningStaffInitStrategy {
    @Override
    public List<CleaningStaff> initializeList() {
        return new ArrayList<>();
    }
}