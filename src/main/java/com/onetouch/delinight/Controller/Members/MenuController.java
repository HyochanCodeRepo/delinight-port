/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.Repository.MenuRepository;
import com.onetouch.delinight.Service.ImageService;
import com.onetouch.delinight.Service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/members/menu")
public class MenuController {
    private final MenuService menuService;
    private final ImageService imageService;

    //등록get
    @GetMapping("/create")
    public String createView(Principal principal, Model model){
        boolean storeImgExistence = imageService.ExistStoreImgByEmail(principal.getName());
        if(storeImgExistence) {
            model.addAttribute("menuDTO", new MenuDTO());
            return "members/menu/create";
        }
        else {
            model.addAttribute("sep", "store");
            return "members/account/common/imgRedirect";
        }
    }

    @PostMapping("/create")
    public String createProc(MenuDTO menuDTO, Principal principal){

        log.info("등록 포스트 진입 : " + menuDTO);
        log.info("등록 포스트 진입 : " + menuDTO);

        log.info("imgNum 값: " + menuDTO.getImgNum());

        String email = principal.getName();

        menuService.register(menuDTO, email);
        log.info("저장된 데이터 : " + menuDTO);
        return "redirect:/members/menu/list";
    }

    @GetMapping("/list")
    public String listView(@RequestParam(value = "id", required = false) Long id, Model model,
                           @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC)
                           Pageable pageable, Principal principal){

        Page<MenuDTO> menuDTOList = menuService.menuList(pageable, principal.getName());
        log.info(menuDTOList.getPageable().getPageNumber());
        model.addAttribute("menuDTOList",menuDTOList);
        log.info(menuDTOList.getContent());

        if (id != null){
            MenuDTO selectedMenu = menuService.read(id);
            model.addAttribute("selectedMenu", selectedMenu);
            log.info("선택된 메뉴 :: ", selectedMenu);
        }

        return "members/menu/list";
    }

    @GetMapping("/update/{id}")
    public String updateView(@PathVariable(name = "id") Long id,Model model){
        log.info("수정" + id);
        MenuDTO menuDTO = menuService.read(id);
        model.addAttribute("menuDTO", menuDTO);
        log.info(menuDTO.getId());
        String imgUrl = imageService.read(id);
        model.addAttribute("imgUrl",imgUrl);
        return "members/menu/update";

    }
    @PostMapping("/update")
    public String updateProc(MenuDTO menuDTO){
        log.info(menuDTO);

        menuService.update(menuDTO);

        return "redirect:/members/menu/list?id="+menuDTO.getId();
    }
}
