package com.onetouch.delinight.DTO;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdMailDTO {

    private String title;
    private String period;
    private String content;

    private int templeteId;
    private String benefit;
    private String  mainContent;

    private MultipartFile image;
}
