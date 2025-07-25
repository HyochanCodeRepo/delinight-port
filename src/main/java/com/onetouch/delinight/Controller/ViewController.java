package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.MembersRepository;
import com.onetouch.delinight.Service.ImageService;
import com.onetouch.delinight.Service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@Log4j2

public class ViewController {

    private final MenuService menuService;
    private final ImageService imageService;
    private final MembersRepository membersRepository;


    @ModelAttribute("membersDTO")
    public MembersDTO setUserInfo(Principal principal) {
        if (principal == null) return null;

        String email = principal.getName();
        MembersEntity membersEntity = membersRepository.findByEmail(email);
        return new MembersDTO(membersEntity);
    }

    @GetMapping("/")
    public String index() {

        return "redirect:/members/account/login";
    }
    //
    //@GetMapping("/register")
    //public String register() {
    //    return "/admin/menu/register";
    //}
    //
    //@GetMapping("/registerIndex")
    //public String registerView() {
    //    return "/admin/menu/registerIndex";
    //}
    //@GetMapping("/listIndex")
    //public String listView(Pageable pageable, Model model, Principal principal){
    //    Page<MenuDTO> menuList = menuService.menuList(pageable, principal.getName());
    //    model.addAttribute("menuDTOList",menuList);
    //
    //
    //
    //    return "/admin/menu/listIndex";
    //}
    //@GetMapping("/readIndex")
    //public String readView(@RequestParam Long id, Model model, Principal principal, RedirectAttributes redirectAttributes) {
    //    MenuDTO menuDTO =
    //            menuService.read(id);
    //    model.addAttribute("menuDTO", menuDTO);
    //    return "/admin/menu/readIndex";
    //}
    //@GetMapping("/updateIndex")
    //public String updateView(@PathVariable(name = "id") Long id, Model model) {
    //    log.info("수정" + id);
    //    MenuDTO menuDTO = menuService.read(id);
    //    model.addAttribute("menuDTO", menuDTO);
    //    log.info(menuDTO.getId());
    //    String imgUrl = imageService.read(id);
    //    model.addAttribute("imgUrl",imgUrl);
    //    return "/admin/menu/updateIndex";
    //}

}