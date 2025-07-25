package com.onetouch.delinight.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestDTO {

    private Long id;

    private String phone;

    private String email;

    private String reservationNum;

    private String certId;

    private String password;



}
