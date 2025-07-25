/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이효찬
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이효찬
 *********************************************************************/
package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.Constant.Status;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.MembersRepository;
import com.onetouch.delinight.Service.CenterService;
import com.onetouch.delinight.Service.HotelService;
import com.onetouch.delinight.Service.MembersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/account")
@Log4j2
public class AccountController {

    private final MembersService membersService;
    private final MembersRepository membersRepository;
    private final CenterService centerService;
    private final HotelService hotelService;
    private final ModelMapper modelMapper;

    @GetMapping("/makeSysA")
    public ResponseEntity<String> makeSysA(){
        membersService.makeSysA();
        return ResponseEntity.ok("완료");
    }

    @GetMapping("/dashboard")
    @ResponseBody
    public ResponseEntity<List<MembersDTO>> dashboard(Principal principal){
        MembersDTO membersDTO = membersService.findByEmail(principal.getName());
        List<MembersDTO> result = membersService.findPendingMembersListByCenterMembers(membersDTO);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/redirectPage")
    public String redirectPage() {

        return "members/account/common/redirectPage";
    }

    @GetMapping("/waitRedirectPage")
    public String waitRedirectPage() {

        return "members/account/common/waitRedirectPage";
    }

    @GetMapping("/accountRequest")
    public String accountRequest(String role, Long id, Model model) {

        if (role.equals("super")) {
            model.addAttribute("role", "본사 관리자");
            model.addAttribute("parentId", id);
        } else if (role.equals("hotel")) {
            model.addAttribute("role", "호텔 관리자");
            model.addAttribute("parentId", id);
        } else {
            model.addAttribute("role", "가맹점 관리자");
            model.addAttribute("parentId", id);
        }

        return "members/account/common/requestForm";
    }

    @PostMapping("/accountRequest")
    public String accountRequestPost(MembersDTO membersDTO) {
        String roleStr = membersDTO.getRoleStr();
        log.info(membersDTO);

        if (roleStr.equals("super")) {
            membersDTO.setRole(Role.SUPERADMIN);
            Long membersId = membersService.create(membersDTO);
            centerService.settingAdmin(membersId, membersDTO.getParentId());
        } else if (roleStr.equals("hotel")) {
            membersDTO.setRole(Role.ADMIN);
            membersService.create(membersDTO);
        } else {
            membersDTO.setRole(Role.STOREADMIN);
            membersService.create(membersDTO);
        }



        return "redirect:/members/account/redirectPage";

    }

    @GetMapping("/dashHub")
    public String dashHub(Principal principal, Model model) {
        Role role = membersService.findOnlyRoleByEmail(principal.getName());
        model.addAttribute("role", role);
        log.info("허브진입"+role);

        //로그인한 사람 롤 찾기
        //수퍼 어드민
        if (role.equals(Role.SUPERADMIN)) {
            if (membersService.assignCheck(principal.getName(), 1)) {
                return "redirect:/members/account/superAdHome";
            } else {
                return "redirect:/members/account/waitRedirectPage";
            }
        }
        //호텔 어드민
        else if (role.equals(Role.ADMIN)) {
            if (membersService.assignCheck(principal.getName(), 2)) {
                return "redirect:/members/account/hotelAdHome";
            } else {
                return "redirect:/members/account/waitRedirectPage";
            }
        }
        //스토어 어드민
        else if (role.equals(Role.STOREADMIN)) {
            if (membersService.assignCheck(principal.getName(), 3)) {
                return "redirect:/members/account/storeAdHome";
            } else {
                return "redirect:/members/account/waitRedirectPage";
            }
        } else { // 시스템 어드민일 경우
            return "redirect:/members/account/superAdmin/list";

        }
    }

    @GetMapping("/accountHub")
    public String accountHub(Principal principal, Model model) {
        Role role = membersService.findOnlyRoleByEmail(principal.getName());
        log.info("여기니????");
        model.addAttribute("role", role);

        //시스템 어드민
        if (role.equals(Role.SYSTEMADMIN)) {
            return "redirect:/members/account/superAdmin/list";
        }
        //수퍼 어드민
        else if (role.equals(Role.SUPERADMIN)){
            return "redirect:/members/account/hotelAdmin/list";
        }
        //호텔 어드민
        else {
            return "redirect:/members/account/storeAdmin/list";
        }
    }

    @GetMapping("/centerhub")
    public String centerhub(Principal principal, Model model) {
        Role role = membersService.findOnlyRoleByEmail(principal.getName());
        model.addAttribute("role", role);

        //수퍼 어드민
        if (role.equals(Role.SUPERADMIN)) {
            return "redirect:/members/center/read";
        } else { // 시스템 어드민일 경우
            return "redirect:/members/center/list";
        }
    }

    @GetMapping("/paymenthub")
    public String paymenthub(Principal principal) {
        Role role = membersService.findOnlyRoleByEmail(principal.getName());

        //수퍼 어드민
        if (role.equals(Role.SUPERADMIN)) {
            return "redirect:/members/account/";
        }
        //호텔 어드민
        else if (role.equals(Role.ADMIN)) {
            return "redirect:/members/account/";
        }
        //스토어 어드민
        else if (role.equals(Role.STOREADMIN)) {
            return "redirect:/members/account/";
        } else { // 시스템 어드민일 경우
            return "redirect:/members/account/";
        }
    }

    @GetMapping("/superAdHome")
    public String superAdHome() {
        return "members/account/common/superAdHome";
    }

    @GetMapping("/hotelAdHome")
    public String hotelAdHome() {
        return "members/account/common/hotelAdHome";
    }

    @GetMapping("/storeAdHome")
    public String storeAdHome() {
        return "members/account/common/storeAdHome";
    }

    @GetMapping("/mypage")
    public String adminMyPage(Principal principal, Model model) {

        return "members/account/common/mypage";
    }

    @GetMapping("/update")

    public String adminUpdate(Principal principal, Model model){
        MembersEntity members = membersRepository.findByEmail(principal.getName());
        model.addAttribute("member", members);
        return "members/account/common/update";
    }

    @PostMapping("/update")
    public String updateProc(Principal principal,
                             @ModelAttribute MembersDTO membersDTO, Model model,
                             @RequestParam(value = "currentPassword", required = false) String currentPassword,
                             @RequestParam(value = "newPassword", required = false) String newPassword,
                             @RequestParam(value = "newPhone", required = false) String newPhone,
                             @RequestParam(value = "confirmPassword", required = false) String confirmPassword) {

        MembersEntity membersEntity = membersRepository.findByEmail(principal.getName());
        boolean hasError = false;

        ////유효성검사
        ////전화번호
        //if (membersDTO.getPhone() == null || membersDTO.getPhone().trim().isEmpty()){
        //    model.addAttribute("phoneError", "전화번호를 입력해주세요.");
        //    hasError = true;
        //}
        ////비밀번호
        ////현재 비밀번호에 입력된 값이 있을 때 실행됨
        //if (currentPassword != null && !currentPassword.trim().isEmpty()){
        //
        //    //현재 비밀번호 검증
        //    if (!passwordEncoder.matches(currentPassword, membersEntity.getPassword())){
        //        model.addAttribute("currentPasswordError", "비밀번호가 일치하지 않습니다.");
        //        hasError = true;
        //    }
        //
        //    //새 비밀번호, 새 비밀번호 확인 검증
        //    if (!newPassword.equals(confirmPassword)){
        //        model.addAttribute("confirmPasswordError", "새 비밀번호 확인이 일치하지 않습니다.");
        //        hasError = true;
        //    }
        //}


        membersDTO =
                modelMapper.map(membersEntity, MembersDTO.class);

        membersService.update(membersDTO, newPhone ,newPassword);
        log.info(membersDTO);
        log.info(membersDTO);
        log.info(newPassword);
        log.info(newPassword);
        log.info(newPhone);
        log.info(newPhone);

        MembersEntity members = membersRepository.findByEmail(principal.getName());
        model.addAttribute("member", members);

        return "members/account/common/mypage";
    }


    @GetMapping("/superAdmin/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "status", required = false) String status, Principal principal) {
        log.info("list페이지 진입");

        Status status1;


        Page<MembersEntity> paging = null;

        if (status != null && !status.isEmpty()) {
            if (status.equals("WAIT")) {
                status1 = Status.WAIT;
                paging = membersService.getListBystatus(status1, page);
            } else if (status.equals("VALID")) {
                status1 = Status.VALID;
                paging = membersService.getListBystatus(status1, page);
            } else if (status.equals("NOTVALID")) {
                status1 = Status.NOTVALID;
                paging = membersService.getListBystatus(status1, page);
            }
        } else {
            paging = membersService.getList(page);

        }
        model.addAttribute("paging", paging);
        model.addAttribute("status", status);

        return "members/account/superAdmin/list";
    }

    @GetMapping("/hotelAdmin/list")
    public String hoteladlist(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "status", required = false) String status, Principal principal) {
        log.info("list페이지 진입");

        Status status1;
        Long parentId = centerService.findCenter(principal.getName());


        Page<MembersEntity> paging = null;

        if (status != null && !status.isEmpty()) {
            if (status.equals("WAIT")) {
                status1 = Status.WAIT;
                paging = membersService.findAccount(status1, page, principal.getName(), "hotel");
            } else if (status.equals("VALID")) {

                status1 = Status.VALID;
                paging = membersService.findAccount(status1, page, principal.getName(), "hotel");
            } else if (status.equals("NOTVALID")) {
                status1 = Status.NOTVALID;
                paging = membersService.findAccount(status1, page, principal.getName(), "hotel");
            }
        } else {
            paging = membersService.getListHotel(page, principal.getName());

        }
        model.addAttribute("parentId", parentId);
        model.addAttribute("paging", paging);
        model.addAttribute("status", status);


        return "members/account/hotelAdmin/list";
    }

    @GetMapping("/storeAdmin/list")
    public String storeadlist(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "status", required = false) String status, Principal principal) {


        Status status1;

        Long parentId = hotelService.findHotelByEmail(principal.getName());


        Page<MembersEntity> paging = null;

        if (status != null && !status.isEmpty()) {
            if (status.equals("WAIT")) {
                status1 = Status.WAIT;
                paging = membersService.findAccount(status1, page, principal.getName(), "store");
            } else if (status.equals("VALID")) {
                status1 = Status.VALID;
                paging = membersService.findAccount(status1, page, principal.getName(), "store");
            } else if (status.equals("NOTVALID")) {
                status1 = Status.NOTVALID;
                paging = membersService.findAccount(status1, page, principal.getName(), "store");
            }
        } else {
            paging = membersService.getListStore(page, principal.getName());

        }
        model.addAttribute("paging", paging);
        model.addAttribute("status", status);
        model.addAttribute("parentId", parentId);

        return "members/account/storeAdmin/list";
    }


    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {

        if(membersRepository.findAll().isEmpty()){
            model.addAttribute("reset", "on");
        }
        if ("bad_credentials".equals(error)) {
            model.addAttribute("passwordError", "비밀번호가 틀립니다.");
        } else if ("unknown".equals(error)) {
            model.addAttribute("emailError", "이메일이 틀립니다.");
        }

        return "members/account/common/login";
    }

    @GetMapping("/logout-success")
    public String logout() {
        return "members/account/common/logout";
    }

}