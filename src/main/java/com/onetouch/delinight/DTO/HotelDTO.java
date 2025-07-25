/*********************************************************************
 * 클래스명 : MembersDTO
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelDTO {

    private Long id;

    private String name;

    private String content;

    private BranchDTO branchDTO;
    private MembersDTO membersDTO;
    private BrandDTO brandDTO;
    private Long imgNum;
    private String imgUrl;
    private Long branchId;
    private Long brandId;


    public HotelDTO setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
        return this;
    }
    public HotelDTO setMembersDTO(MembersDTO membersDTO){
        this.membersDTO = membersDTO;
        return this;
    }

    public HotelDTO setBrandDTO(BrandDTO brandDTO) {
        this.brandDTO = brandDTO;
        return this;
    }

    public HotelDTO setImgNum(Long imgNum){
        this.imgNum = imgNum;
        return this;
    }



    public HotelDTO setBranchDTO(BranchDTO branchDTO){
        this.branchDTO = branchDTO;
        return this;
    }

}
