package com.onetouch.delinight.DTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PerformanceMailDTO {

    private String email;
    private String name;
    private String targetName;
    private String aiResponse;
    private LocalDate date;

}
