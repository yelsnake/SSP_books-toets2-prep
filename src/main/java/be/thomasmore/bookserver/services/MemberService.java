package be.thomasmore.bookserver.services;

import be.thomasmore.bookserver.model.Member;
import be.thomasmore.bookserver.model.converters.MemberDetailedDTOConverter;
import be.thomasmore.bookserver.model.dto.MemberDetailedDTO;
import be.thomasmore.bookserver.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberDetailedDTOConverter memberDetailedDTOConverter;

    public MemberDetailedDTO findOne(int id) {
        Optional<Member> member = memberRepository.findById(id);
        if (member.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Member with id %d does not exist.", id));

        return memberDetailedDTOConverter.convertToDto(member.get());
    }

    public MemberDetailedDTO create(MemberDetailedDTO memberDto) {
        memberDto.setId(0);

        if (!isValidMemberNumber(memberDto.getMemberNumber(), memberDto.getCity())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Invalid member number");
        }

        Member member = memberDetailedDTOConverter.convertToEntity(memberDto);

        Member saved = memberRepository.save(member);

        return memberDetailedDTOConverter.convertToDto(saved);
    }

    private boolean isValidMemberNumber(String memberNumber, String city) {
        if (memberNumber == null || city == null) return false;

        //1. 길이 13자
        if (memberNumber.length() != 13) return false;

        //2. regex로 기본 형식 체크: M-XXXMN-NNN-N
        if (!memberNumber.matches("^M-[A-Z]{3}(24|25)-\\d{3}-\\d$")) return false;

        //3. city 일치
        String code = memberNumber.substring(2, 5); // xxx
        if (!code.equals(getCityCode(city))) return false;

        //4. checksum 검증
        // 숫자 year YY, 중간 3자리 합을 mod 9
        int yearTens = Character.getNumericValue(memberNumber.charAt(5));
        int yearOnes = Character.getNumericValue(memberNumber.charAt(6));
        int n1 = Character.getNumericValue(memberNumber.charAt(8));
        int n2 = Character.getNumericValue(memberNumber.charAt(9));
        int n3 = Character.getNumericValue(memberNumber.charAt(10));
        int checksum = Character.getNumericValue(memberNumber.charAt(12));

        int sum = yearTens + yearOnes + n1 + n2 + n3;
        int expected = sum % 9;

        return checksum == expected;
    }

    private String getCityCode(String city) {
        if (city == null) return "";
        String trimmed = city.trim();
        if (trimmed.length() < 3) return "";
        return trimmed.substring(0, 3).toUpperCase();
    }

//    매핑으로도 가능
//    private static final Map<String, String> CITY_CODES = Map.of(
//            "antwerpen", "ANT",
//            "brussel", "BRU",
//            "gent", "GEN",
//            "mechelen", "MEC",
//            "hasselt", "HAS",
//            "leuven", "LEU"
//    );
//
//    private String getCityCode(String city) {
//        if (city == null) return "";
//        return CITY_CODES.getOrDefault(city.trim().toLowerCase(), "");
//    }

}
