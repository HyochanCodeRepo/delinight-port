/*****************************
 * 작성자 : 이동건
 * 공용모듈
 * 필요할 때마다 사용가능한 메소드
 * ***************************/
package com.onetouch.delinight.Util;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// List : 변수명 , 데이터형 , 변수 값이 다를 때
// Map : 변수명은 다르고, 데이터형은 동일하게 , 변수값을 다를 때
@Component // 공용모듈 클래스
// HTML에 사용할 페이지정보를 가공
public class PagenationUtil {
    // 가공할 페이지정보(데이터베이스)를 전달 받아서 각 페이지 번호의 정보를 저장해서 전달
    public static Map<String, Integer> Pagination(Page<?> page) {
        // 첫페이지(1) 이전(현재-1) 현재페이지 이후(현재+1) 마지막페이지(페이지마지막)
        // 데이터베이스 페이지번호(0) -> 화면 페이지번호(1)
        int currentPage = page.getNumber()+1; // 화면에 출력할 현재 페이지번호(중앙)
        int totalPage = page.getTotalPages(); // 전체 페이지번호(마지막번호)
        int blockLimit = 10; // 화면에 출력할 페이지 번호의 갯수 처음 이전 1,2,3,4,5,6,7,8,9,10 다음 끝

        Map<String , Integer> pageInfo = new HashMap<>();
        // 시작 페이지가 1 2 3(기준) 4 5
        // 앞에 시작번호가 0보다 작으면 1값으로 설정
        int startPage = Math.max(1, currentPage-blockLimit/2);// 화면에 출력되는 시작페이지번호
        // 1페이지+5 = 6 => -1 => 5 (1 , 2 , 3 , 4 , 5)
        int endPage = Math.min(totalPage, startPage+blockLimit-1);  // 화면에 출력되는 끝나는 페이지번호
        // 1과 현재페이지번호 중 큰수를 저장 1,0 ==> 1
        int prevPage = Math.max(1 ,currentPage-1); // 이전 페이지번호(0이면 존재불가능)
        // 마지막페이지번호와 다음번호 중 작은수를 저장 10, 11(존재X)==?10
        int nextPage = Math.min(totalPage , currentPage+1); // 다음 페이지번호(마지막페이지보다 크면 존재불가능)
        int lastPage = totalPage; // 마지막 페이지번호

        // 계산한 페이지정보를 저장해서 전달
        // Map 추가할 때 put("변수명" , 데이터값)
        pageInfo.put("startPage", startPage);
        pageInfo.put("endPage", endPage);
        pageInfo.put("prevPage", prevPage);
        pageInfo.put("currentpage", currentPage);
        pageInfo.put("nextPage", nextPage);
        pageInfo.put("lastPage", lastPage);

        return pageInfo;
    }

}
