package com.onetouch.delinight.Constant;

public enum OrdersStatus {

    PENDING, // 결제 방법 및 결제 전 대기 상태
    AWAITING, // 스토어의 오더 승인 대기중
    PREPARING, // 음식 준비 단계
    DELIVERING, // 배달 중 상태
    DELIVERED // 배달 완료 상태(최종 단계)

}
