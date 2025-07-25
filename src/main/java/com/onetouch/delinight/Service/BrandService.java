/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.BrandDTO;

import java.util.List;

public interface BrandService {

    void create(BrandDTO brandDTO);

    List<BrandDTO> list();

    BrandDTO read(Long id);


    void del(Long num);


}
