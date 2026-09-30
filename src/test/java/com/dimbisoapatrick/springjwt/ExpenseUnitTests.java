package com.dimbisoapatrick.springjwt;
import com.dimbisoapatrick.springjwt.dto.*;
import com.dimbisoapatrick.springjwt.entity.Expense;
import com.dimbisoapatrick.springjwt.mapper.ExpenseConverter;
import com.dimbisoapatrick.springjwt.repository.ExpenseRepository;
import com.dimbisoapatrick.springjwt.service.ExpenseService;
import com.dimbisoapatrick.springjwt.util.DateTimeUtil;
import com.dimbisoapatrick.springjwt.validator.ExpenseValidator;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
class ExpenseUnitTests {
    private final ExpenseRepository repository = mock(ExpenseRepository.class);
    private final ExpenseConverter converter = new ExpenseConverter();
    private final ExpenseService service = new ExpenseService(repository, converter);
    private ExpenseDTO dto(String amount) { var dto = new ExpenseDTO(); dto.setAmount(new BigDecimal(amount)); return dto; }
    @Test void mapperPreservesExternalIdentifier() { var dto=dto("12.34"); dto.setId(1L); dto.setExpenseId("external-id"); dto.setName("Travel"); dto.setDate(Date.valueOf("2026-01-01")); assertThat(converter.convertModelToDTO(converter.convertDtoToModel(dto))).usingRecursiveComparison().isEqualTo(dto); }
    @Test void totalKeepsDecimalPrecisionAndZero() { assertThat(service.totalExpenses(List.of(dto("0.10"),dto("0.20")))).isEqualTo("0.30"); assertThat(service.totalExpenses(List.of())).isEqualTo("0.00"); }
    @Test void dateRoundTrip() throws Exception { assertThat(DateTimeUtil.convertDateToString(DateTimeUtil.convertStringToDate("31/01/2026"))).isEqualTo("31/01/2026"); }
    @Test void impossibleDateIsRejected() { assertThatThrownBy(() -> DateTimeUtil.convertStringToDate("31/02/2026")).isInstanceOf(java.text.ParseException.class); }
    @Test void emptyDateValidatorDoesNotThrow() { var dto=new ExpenseDTO(); var errors=new BeanPropertyBindingResult(dto,"expense"); new ExpenseValidator().validate(dto,errors);assertThat(errors.hasFieldErrors("dateString")).isTrue(); }
    @Test void filterDefaultsAreNullSafe() throws Exception { when(repository.findByNameContainingAndDateBetween(anyString(),any(),any())).thenReturn(List.of()); assertThat(service.getFilteredExpenses(new ExpenseFilterDTO())).isEmpty(); }
    @Test void filtersSortByAmount() throws Exception { var first=new Expense();first.setAmount(new BigDecimal("5")); first.setDate(Date.valueOf("2026-01-01"));var second=new Expense();second.setAmount(new BigDecimal("20"));second.setDate(Date.valueOf("2026-01-02"));when(repository.findByNameContainingAndDateBetween(anyString(),any(),any())).thenReturn(List.of(first,second));var filter=new ExpenseFilterDTO("","amount","","");assertThat(service.getFilteredExpenses(filter)).extracting(ExpenseDTO::getAmount).containsExactly(new BigDecimal("20"),new BigDecimal("5")); }
    @Test void saveGeneratesIdAndParsesDate() throws Exception { var dto=dto("12.34");dto.setName("Travel");dto.setDateString("01/01/2026");when(repository.save(any())).thenAnswer(call -> call.getArgument(0));var saved=service.saveExpenseDetails(dto);assertThat(saved.getExpenseId()).isNotBlank();assertThat(saved.getDate()).isEqualTo(Date.valueOf("2026-01-01")); }
    @Test void missingExpenseIsReported() { when(repository.findByExpenseId("missing")).thenReturn(java.util.Optional.empty());assertThatThrownBy(() -> service.getExpense("missing")).isInstanceOf(RuntimeException.class); }
}
