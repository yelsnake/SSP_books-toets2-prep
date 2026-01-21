package be.thomasmore.bookserver.controllers.members;

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
public class MemberControllerMemberNumberValidationTest extends AbstractIntegrationTest {

    @Test
    @WithMockUser
    public void createMember_validMemberNumber_returnsCreated() throws Exception {
        MemberDetailedDTO dto = MemberDetailedDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .address("Some Street 1")
                .city("Antwerpen")
                .memberNumber("M-ANT24-101-8")
                .build();

        mockMvc.perform(getMockRequestPost("/api/members", dto))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    public void createMember_invalidDashes_returns500_andDoesNotSave() throws Exception {
        MemberDetailedDTO dto = MemberDetailedDTO.builder()
                .firstName("A")
                .lastName("B")
                .address("X")
                .city("Antwerpen")
                .memberNumber("M-ANT-24-101-8")
                .build();

        mockMvc.perform(getMockRequestPost("/api/members", dto))
                .andExpect(status().isInternalServerError());

        // 저장이 안 됐는지: 다음 유효 생성이 id=1이어야 함
        MemberDetailedDTO valid = MemberDetailedDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .address("Some Street 1")
                .city("Antwerpen")
                .memberNumber("M-ANT24-101-8")
                .build();

        mockMvc.perform(getMockRequestPost("/api/members", valid))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    public void createMember_invalidFourDigits_returns500() throws Exception {
        MemberDetailedDTO dto = MemberDetailedDTO.builder()
                .firstName("A")
                .lastName("B")
                .address("X")
                .city("Antwerpen")
                .memberNumber("M-ANT24-1012-1")
                .build();

        mockMvc.perform(getMockRequestPost("/api/members", dto))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    public void createMember_wrongCityCode_returns500() throws Exception {
        MemberDetailedDTO dto = MemberDetailedDTO.builder()
                .firstName("A")
                .lastName("B")
                .address("X")
                .city("Brussel")
                .memberNumber("M-ANT24-101-8")
                .build();

        mockMvc.perform(getMockRequestPost("/api/members", dto))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    public void createMember_wrongChecksum_returns500() throws Exception {
        MemberDetailedDTO dto = MemberDetailedDTO.builder()
                .firstName("A")
                .lastName("B")
                .address("X")
                .city("Antwerpen")
                .memberNumber("M-ANT24-101-9")
                .build();

        mockMvc.perform(getMockRequestPost("/api/members", dto))
                .andExpect(status().isInternalServerError());
    }
}
