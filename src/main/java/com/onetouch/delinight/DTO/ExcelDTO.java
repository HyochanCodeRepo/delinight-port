package com.onetouch.delinight.DTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExcelDTO {

    private LocalDate date;
    private String menuName;
    private int unitPrice;
    private int quantity;
    private int totalPrice;
    private String hotelName;
    private String storeName;


}
