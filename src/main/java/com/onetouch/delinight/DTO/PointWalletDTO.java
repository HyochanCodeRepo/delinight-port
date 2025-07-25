package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Entity.UsersEntity;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointWalletDTO {

    private Long id;

    private Long cPoint; //CurrentPoint 현재포인트

    private UsersEntity users;
}
