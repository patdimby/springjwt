package com.dimbisoapatrick.springjwt;
import com.dimbisoapatrick.springjwt.entity.User;
import com.dimbisoapatrick.springjwt.repository.*;
import com.dimbisoapatrick.springjwt.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.containsString;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WebIntegrationTests {
    @Autowired MockMvc mvc;
    @Autowired UserRepository users;
    @Autowired ExpenseRepository expenses;
    @Autowired PasswordEncoder encoder;
    @MockBean EmailService email;
    @BeforeEach void reset() { expenses.deleteAll();users.deleteAll(); }
    @Test void publicFormsRenderAndExpensePagesRequireLogin() throws Exception { mvc.perform(get("/req/login")).andExpect(status().isOk());mvc.perform(get("/req/register")).andExpect(status().isOk());mvc.perform(get("/req/expenses")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("**/req/login")); }
    @Test void formRegistrationHashesPasswordAndSupportsLogin() throws Exception {
        mvc.perform(post("/req/register").with(csrf()).param("username","ada").param("email","ada@example.com").param("password","secret123")).andExpect(status().isOk()).andExpect(view().name("req/login"));
        var account=users.findByEmail("ada@example.com");assertThat(encoder.matches("secret123",account.getPassword())).isTrue();assertThat(account.getPassword()).isNotEqualTo("secret123");
        mvc.perform(formLogin("/login").user("ada").password("secret123")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/req/expenses"));
    }
    @Test void invalidFormShowsErrorsWithoutPersisting() throws Exception { mvc.perform(post("/req/register").with(csrf()).param("username","").param("email","bad").param("password","short")).andExpect(status().isOk()).andExpect(view().name("req/register"));assertThat(users.count()).isZero(); }
    @Test void jsonSignupVerifiesEmailAndRejectsReplay() throws Exception {
        mvc.perform(post("/req/signup").contentType("application/json").content("{\"username\":\"ada\",\"email\":\"ada@example.com\",\"password\":\"secret123\",\"verified\":true}")).andExpect(status().isOk());
        var account=users.findByEmail("ada@example.com");assertThat(account.isVerified()).isFalse();assertThat(encoder.matches("secret123",account.getPassword())).isTrue();verify(email).sendVerificationEmail(eq("ada@example.com"),anyString());
        var token=account.getVerificationToken();mvc.perform(get("/req/signup/verify").param("token",token)).andExpect(status().isCreated());assertThat(users.findByEmail("ada@example.com").isVerified()).isTrue();
        mvc.perform(get("/req/signup/verify").param("token",token)).andExpect(status().isForbidden());
    }
    @Test void invalidVerificationReturns403() throws Exception { mvc.perform(get("/req/signup/verify").param("token","garbage")).andExpect(status().isForbidden()); }
    @Test @WithMockUser void expensesPersistRenderUpdateFilterAndDelete() throws Exception {
        mvc.perform(post("/req/saveOrUpdateExpense").with(csrf()).param("name","Travel").param("description","Taxi").param("amount","12.34").param("dateString","01/01/2026")).andExpect(redirectedUrl("/req/expenses"));
        var expense=expenses.findAll().get(0);assertThat(expense.getExpenseId()).isNotBlank();
        mvc.perform(get("/req/expenses")).andExpect(status().isOk()).andExpect(content().string(containsString("Travel")));
        mvc.perform(get("/req/updateExpense").param("id",expense.getExpenseId())).andExpect(status().isOk());
        mvc.perform(post("/req/saveOrUpdateExpense").with(csrf()).param("id",expense.getId().toString()).param("expenseId",expense.getExpenseId()).param("name","Updated").param("amount","20.00").param("dateString","01/01/2026")).andExpect(redirectedUrl("/req/expenses"));
        assertThat(expenses.findByExpenseId(expense.getExpenseId())).get().extracting("name").isEqualTo("Updated");
        mvc.perform(get("/req/filterExpenses").param("keyword","Updated").param("sortBy","amount").param("startDate","").param("endDate","")).andExpect(status().isOk()).andExpect(view().name("req/expenses-list"));
        mvc.perform(post("/req/deleteExpense").with(csrf()).param("id",expense.getExpenseId())).andExpect(redirectedUrl("/req/expenses"));assertThat(expenses.count()).isZero();
    }
    @Test @WithMockUser void mutationWithoutCsrfIsDenied() throws Exception { mvc.perform(post("/req/deleteExpense").param("id","any")).andExpect(status().isForbidden()); }
    @Test void staticResourcesArePublic() throws Exception { mvc.perform(get("/req/js/jquery.js")).andExpect(status().isOk()); }
}
