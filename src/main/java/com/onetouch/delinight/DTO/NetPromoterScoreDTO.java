package com.onetouch.delinight.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NetPromoterScoreDTO {

    private Long id;
    private Long checkOutId;
    private int questionOne;
    private int questionTwo;
    private int questionThree;
    private int questionFour;
    private int questionFive;
    private String etcQuestion;
    private int totalScore;
    private String hotelOrStore;
    private Long hotelId;
    private Long storeId;
    private String storeName;
    private String hotelName;
    private LocalDateTime insertTime;

    private HotelDTO hotelDTO;
    private StoreDTO storeDTO;

    public NetPromoterScoreDTO setHotelDTO(HotelDTO hotelDTO) {

        this.hotelDTO = hotelDTO;
        return this;
    }
    public NetPromoterScoreDTO setStoreDTO(StoreDTO storeDTO) {

        this.storeDTO = storeDTO;
        return this;
    }




}
