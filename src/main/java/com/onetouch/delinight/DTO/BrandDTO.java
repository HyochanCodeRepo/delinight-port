/*********************************************************************
 * 클래스명 : MembersDTO
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandDTO {

    private Long id;

    @NotBlank(message = "제목을 작성해주세요.")
    @Size(min = 3,max = 50,message = "3~50글자로 작성해주세요.")
    private String name;

    @NotBlank(message = "제목을 작성해주세요.")
    @Size(min = 3,max = 50,message = "3~50글자로 작성해주세요.")
    private String content;








}
