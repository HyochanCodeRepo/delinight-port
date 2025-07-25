package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.BrandDTO;
import com.onetouch.delinight.Repository.BrandRepository;
import com.onetouch.delinight.Service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/brand")
@Log4j2
public class BrandController {
    private final BrandRepository brandRepository;
    private final BrandService brandService;
    private final ModelMapper modelMapper;

    @GetMapping("/list")
    public String listView(Model model) {

        List<BrandDTO> brandDTOList =
            brandService.list();

        log.info(brandDTOList);
        log.info(brandDTOList);
        model.addAttribute("brandDTOList", brandDTOList);

        return "members/brand/list";
    }


}
