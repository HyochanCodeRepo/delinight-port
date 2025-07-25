/*********************************************************************
 * 클래스명 : UsersController
 * 기능 : 회원가입 , 로그인 , 로그아웃 , 마이페이지 , 정보수정 , 비번변경 , 비번찾기
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.UsersRepository;
import com.onetouch.delinight.Service.MenuService;
import com.onetouch.delinight.Service.OpenAIService;
import com.onetouch.delinight.Service.StoreService;
import com.onetouch.delinight.Service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Log4j2
public class UserController {

        private final UsersService usersService;
        private final UsersRepository usersRepository;
        private final StoreService storeService;
        private final MenuService menuService;
        private final OpenAIService openAIService;

        @GetMapping("/mobile")
        public String mobileGET(){
                return "users/mobile";
        }

        @PostMapping("/chatGPT/recommend/menu")
        @ResponseBody
        public String recommendMenu(@RequestParam("prompt") String prompt){

                String result = openAIService.makeRecommendPrompt(prompt);
                return result;
        }


        @GetMapping("/home")
        public String usershome(Principal principal , Model model) {

                if (principal == null) {
                        return "redirect:/users/login";
                }

                String email = principal.getName();

                // DB 사용자 이름 조회
                UsersEntity usersEntity = usersRepository.selectEmail(email);
                String name = usersEntity != null ? usersEntity.getName() : "고객";

                List<StoreDTO> storeDTOList = storeService.list(email);

                model.addAttribute("storeDTOList",storeDTOList);



                return "users/home";
        }

        @GetMapping("/store/read")
        public String storeRead(Long storeId, Model model){

                List<MenuDTO> menuDTOList = menuService.menuList(storeId);
                StoreDTO storeDTO = storeService.read(storeId);
                model.addAttribute("menuDTOList", menuDTOList);
                model.addAttribute("storeDTO", storeDTO);
                return "users/store/read";
        }

        @GetMapping("/signUp")
        public String singUp() {
                return "users/account/signUp";
        }

        @PostMapping("/signUp")
        public String singUpPost(@Valid UsersDTO usersDTO , BindingResult bindingResult , Model model) {
                if(bindingResult.hasErrors()) {
                        // 오류 메시지를 모델에 담기
                        model.addAttribute("error" , bindingResult.getAllErrors());
                        return "redirect:/users/account/signUp";
                }

                try {
                        usersService.singUpUser(usersDTO);
                }catch (Exception e) {
                        model.addAttribute("errorMessage" , "회원가입 처리 중 오류가 발생했습니다.");
                        return "users/account/signUp";
                }

                return "users/account/login";
        }

        @GetMapping("/login")
        public String login(Integer sep, Model model) {

                model.addAttribute("sep", sep);

                return "users/account/login";
        }


        // 비번 변경
        @GetMapping("/passwordChange")
        public String passwordChangeGET() {
                log.info("passwordChange");
                return "users/account/passwordChange";
        }

        @PostMapping("/passwordChange")
        public String passwordChangePost(UsersDTO usersDTO , RedirectAttributes redirectAttributes) {

                boolean result = usersService.passwordChange(usersDTO);

                if(result) {
                        return "redirect:/users/login";
                }else{
                        redirectAttributes.addFlashAttribute("msg" , "비밀번호를 변경할 수 없습니다.");
                        return "redirect:/users/passwordChange";
                }

        }


        // 임시 비번발급
        @GetMapping("/sendPassword")
        public String sendPasswordGET() {
                return "users/account/sendPassword";
        }

        @PostMapping("/sendPassword")
        public String sendPasswordPost(UsersDTO usersDTO , Model model) {
                try {
                        // 임시 비밀번호 발급 서비스 호출
                        String resultMessage = usersService.sendTemporaryPassword(usersDTO);
                        model.addAttribute("message" , resultMessage); // 성공 메시지
                        return "redirect:/users/login";                                     // 실패 시 이동할 URL
                }catch (IllegalStateException e){
                        model.addAttribute("error" , e.getMessage());  // 오류 메시지
                        return "redirect:/users/login";                                     // 실패 시 이동할 URL
                }
        }

        @GetMapping("/update")
        public String updateGET(Principal principal, Model model) {
                if (principal != null) {
                        String email = principal.getName(); // 현재 로그인한 사용자의 이메일 (username)
                        model.addAttribute("email", email); // 뷰에서 사용하기 위해 추가
                } else {
                        return "redirect:/users/login"; // 비로그인 사용자는 로그인 페이지로 리다이렉트
                }
                return "users/account/update";
        }

        @PostMapping("/update")
        public String updatePost(Principal principal,UsersDTO usersDTO ,RedirectAttributes redirectAttributes) {

                if (principal == null) {
                        redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
                        return "redirect:/users/login"; // 로그인 페이지로 리다이렉트
                }

                String email = principal.getName(); // 현재 로그인한 사용자의 이메일 가져오기

                try {
                        usersService.userUpdate(email, usersDTO);
                        redirectAttributes.addFlashAttribute("successMessage", "회원정보가 성공적으로 수정되었습니다.");
                        return "redirect:/users/home";
                } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("errorMessage", "회원정보 수정 중 오류가 발생했습니다.");
                        return "redirect:/users/update";
                }
        }

        @GetMapping("/delete")
        public String deleteUser(Principal principal) {
                principal.getName();
                log.info("탈퇴처리 응답하러 오긴함???");
               try {
                       String email = principal.getName();
                       usersService.userDelete(email);
               }catch (Exception e){

               }
               return "redirect:/users/home";
        }

        @GetMapping("/userCheck")
        public String userCheckGET() {
                return "users/userCheck";
        }

        // 사용자 주문 View Mapping →→→→→→→→ RestFullController
        @GetMapping("welcome")
        public String WelcomeUserGET() {
                return "users/account/welcome";
        }



}









