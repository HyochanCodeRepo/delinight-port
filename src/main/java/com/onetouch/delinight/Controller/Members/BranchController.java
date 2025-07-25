/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이효찬
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이효찬
 *********************************************************************/
package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.BranchDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Service.BranchService;
import com.onetouch.delinight.Service.CenterService;
import com.onetouch.delinight.Service.MembersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/branch")
public class BranchController {

    private final MembersService membersService;
    private final BranchService branchService;
    private final CenterService centerService;


    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> create(BranchDTO branchDTO) {
        branchService.create(branchDTO);
        return ResponseEntity.ok("생성 완료");
    }

    @GetMapping("/list")
    public String listView(Model model, Principal principal) {
        MembersDTO membersDTO = membersService.findByEmail(principal.getName());
        List<BranchDTO> branchDTOList =
            branchService.list(membersDTO.getEmail());
        Long centerId = centerService.findCenter(membersDTO.getEmail());
        model.addAttribute("branchDTOList", branchDTOList);
        model.addAttribute("centerId", centerId);

        return "members/branch/listA";
    }
}
