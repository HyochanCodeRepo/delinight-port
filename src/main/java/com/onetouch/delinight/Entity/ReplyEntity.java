package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Entity.base.BaseEntity;
import com.onetouch.delinight.Entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReplyEntity extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String replyText;

    private String replyer;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime responseTime; //답변시간


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquireEntity_id")
    private InquireEntity inquireEntity;



}
