package be.thomasmore.bookserver.services;

import be.thomasmore.bookserver.model.Member;
import be.thomasmore.bookserver.model.converters.MemberDetailedDTOConverter;
import be.thomasmore.bookserver.model.dto.MemberDetailedDTO;
import be.thomasmore.bookserver.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
}
