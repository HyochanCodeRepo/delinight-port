/*********************************************************************
 * 클래스명 : MembersDTO
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CenterDTO {

    private Long id;
    
    @NotBlank(message = "이름은 필수로 입력해주세요.")
    @Size(min = 3, max = 20, message = "3~20자 이내로 작성")
    private String name;
    @NotBlank(message = "내용은 필수로 입력해주세요.")
    @Size(min = 3, max = 100, message = "3~100자 이내로 작성")
    private String content;


    private MembersDTO membersDTO;
    private Long membersId;

}
