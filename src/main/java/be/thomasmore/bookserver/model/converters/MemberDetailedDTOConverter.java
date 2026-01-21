package be.thomasmore.bookserver.model.converters;

import be.thomasmore.bookserver.model.Member;
import be.thomasmore.bookserver.model.dto.MemberDetailedDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberDetailedDTOConverter {

    private final ModelMapper modelMapper;

    public MemberDetailedDTOConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    private ModelMapper modelMapper;

    public MemberDetailedDTO convertToDto(Member member) {
        return modelMapper.map(member, MemberDetailedDTO.class);
    }

    public Member convertToEntity(MemberDetailedDTO dto) {
        return modelMapper.map(dto, Member.class);
    }

    public Member convertToEntity(MemberDetailedDTO dto, Member member) {
        modelMapper.map(dto, member);
        return member;
    }
}
