package lt.teamProject.smartCarCosts.service;

import lt.teamProject.smartCarCosts.entity.ExpenseCategory;
import lt.teamProject.smartCarCosts.entity.UserCar;
import lt.teamProject.smartCarCosts.repository.ExpenseCategoryRepository;
import lt.teamProject.smartCarCosts.repository.ExpenseRepository;
import lt.teamProject.smartCarCosts.repository.UserCarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private ExpenseCategoryRepository expenseCategoryRepository;
    @Mock
    private UserCarRepository userCarRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void getAllTimeTotal_WhenUserHasNoCars_ShouldReturnZero() {
        Long userId = 1L;
        when(userCarRepository.findByUserId(userId)).thenReturn(List.of());

        BigDecimal result = expenseService.getAllTimeTotal(userId);

        assertEquals(BigDecimal.ZERO, result);
        verify(expenseRepository, never()).getAllTimeTotalByUserCars(any());
    }

    @Test
    void getAllTimeTotal_WhenUserHasCars_ShouldReturnSum() {
        Long userId = 1L;
        UserCar car1 = new UserCar(); ReflectionTestUtils.setField(car1, "id", 10L);
        UserCar car2 = new UserCar(); ReflectionTestUtils.setField(car2, "id", 20L);

        when(userCarRepository.findByUserId(userId)).thenReturn(List.of(car1, car2));
        when(expenseRepository.getAllTimeTotalByUserCars(List.of(10L, 20L)))
                .thenReturn(new BigDecimal("1500.50"));

        BigDecimal result = expenseService.getAllTimeTotal(userId);

        assertEquals(new BigDecimal("1500.50"), result);
    }

    @Test
    void getTotalByPeriod_WhenDatesAreNull_ShouldReturnZero() {
        Long userId = 1L;

        BigDecimal result = expenseService.getTotalByPeriod(userId, null, LocalDate.now());

        assertEquals(BigDecimal.ZERO, result);
        verify(userCarRepository, never()).findByUserId(anyLong());
    }

    @Test
    void formatSelectedPeriod_ShouldHandleNullAndValidDates() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 12, 31);

        String validResult = expenseService.formatSelectedPeriod(start, end);
        assertEquals("2026-01-01 - 2026-12-31", validResult);

        String nullResult = expenseService.formatSelectedPeriod(null, end);
        assertEquals("XXXX-XX-XX - XXXX-XX-XX", nullResult);
    }

    @Test
    void getExpenseCategories_ShouldFilterNullsAndEmptyNames() {
        ExpenseCategory validCat = new ExpenseCategory();
        ReflectionTestUtils.setField(validCat, "name", "Fuel");

        ExpenseCategory nullNameCat = new ExpenseCategory();

        when(expenseCategoryRepository.findAll()).thenReturn(List.of(validCat, nullNameCat));

        List<ExpenseCategory> result = expenseService.getExpenseCategories();

        assertEquals(1, result.size());
        assertEquals("Fuel", ReflectionTestUtils.getField(result.get(0), "name"));
    }
}