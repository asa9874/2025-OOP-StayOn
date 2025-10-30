package org.example.domain.cleaningStaff.strategy;

import org.example.domain.cleaningStaff.CleaningStaff;
import java.util.List;

public interface CleaningStaffInitStrategy {
    List<CleaningStaff> initializeList();
}