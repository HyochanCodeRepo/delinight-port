package com.onetouch.delinight.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pointWallet")
public class PointWalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pointWallet_id")
    private Long id;

    private Long cPoint; //CurrentPoint 현재포인트

    @OneToOne
    @JoinColumn(name = "users_id")
    private UsersEntity users;
}
