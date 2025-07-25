package com.onetouch.delinight.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RoomCareMenuDTO {

    private Long id;
    private String name;
    private String content;
    @ToString.Exclude
    private HotelDTO hotelDTO;
    private String imgUrl;
    private Long imgNum;
    private Long hotelId;
    public RoomCareMenuDTO setHotelDTO(HotelDTO hotelDTO){
        this.hotelDTO = hotelDTO;
        return this;

    }
    public RoomCareMenuDTO setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
        return this;
    }
}
