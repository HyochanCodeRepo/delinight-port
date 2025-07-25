package com.onetouch.delinight.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReplyDTO {
    private Long id;

    private String replyText;


    private String replyer;

    private InquireDTO inquireDTO;

    private Long inquireId;
    
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    public ReplyDTO setInquireDTO(InquireDTO inquireDTO){
        this.inquireDTO = inquireDTO;
        return this;
    }

}
