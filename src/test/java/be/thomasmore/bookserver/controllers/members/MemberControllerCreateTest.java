package be.thomasmore.bookserver.controllers.member;

import be.thomasmore.bookserver.AbstractIntegrationTest;
import be.thomasmore.bookserver.model.dto.MemberDetailedDTO;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(scripts = "/sql/members/clean_members.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/members/clean_members.sql", executionPhase = AFTER_TEST_METHOD)
public class MemberControllerCreateTest extends AbstractIntegrationTest {

    @Test
    @WithMockUser
    public void createMember() throws Exception {
        MemberDetailedDTO dto = MemberDetailedDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .address("Some Street 1")
                .city("Antwerpen")
                .memberNumber("M-ANT24-101-8")
                .build();

        mockMvc.perform(getMockRequestPost("/api/members", dto))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.address").value("Some Street 1"))
                .andExpect(jsonPath("$.city").value("Antwerpen"))
                .andExpect(jsonPath("$.memberNumber").value("M-ANT24-101-8"));
    }
}
