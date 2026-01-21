package be.thomasmore.bookserver.controllers;

import be.thomasmore.bookserver.model.dto.MemberDetailedDTO;
import be.thomasmore.bookserver.services.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Operation(summary = "get 1 member from the database.",
    description = "Member with id is fetched from database - returns detailed info.")
    @GetMapping("{id}")
    public MemberDetailedDTO findOne(@PathVariable int id) {
        log.info(String.format("##### findOne member %d", id));
        return memberService.findOne(id);
    }
}
