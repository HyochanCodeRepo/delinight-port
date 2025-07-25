package com.onetouch.delinight.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class ImageDTO {

    private Long imgNum;
    private String fullUrl;
    private String fileName;
    private String originName;
    private MenuDTO menuDTO;
    private HotelDTO hotelDTO;
    private StoreDTO storeDTO;

    private String imgType;
    public ImageDTO setMenuDTO(MenuDTO menuDTO){
        this.menuDTO = menuDTO;
        return this;
    }

    public ImageDTO setStoreDTO(StoreDTO storeDTO){
        this.storeDTO = storeDTO;
        return this;
    }

}
