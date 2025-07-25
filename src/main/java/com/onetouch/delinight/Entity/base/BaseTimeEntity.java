package com.onetouch.delinight.Entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
public class BaseTimeEntity{

    @CreatedDate
    @Column(updatable = false)
    //등록날짜
    private LocalDateTime regTime;
    //수정날짜
    @LastModifiedDate
    private LocalDateTime updateTime;




}
