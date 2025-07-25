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
public class BranchDTO {

    private Long id;

    private String name;

    private String content;

    private CenterDTO centerDTO;
    private Long centerId;

    public BranchDTO setCenterDTO(CenterDTO  centerDTO){
        this.centerDTO = centerDTO;
        return this;
    }

}
