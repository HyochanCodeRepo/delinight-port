package com.onetouch.delinight.Service;

import com.onetouch.delinight.Entity.StoreEntity;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.StoreRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Log4j2
@Transactional
class StoreServiceTest {
    @Autowired
    HotelRepository hotelRepository;
    @Autowired
    StoreRepository storeRepository;
    @Test
    @Commit
    public void createTest(){
        List<String> storeName= new ArrayList<>();
        storeName.add("엽기 떡볶이 ");
        storeName.add("김밥 천국 ");
        storeName.add("서브 웨이 ");
        storeName.add("대치 순대국 ");
        storeName.add("홍두깨 칼국수 ");
        storeName.add("아웃백 ");
        storeName.add("빕스 ");
        storeName.add("애슐리 ");
        storeName.add("숯닭 ");
        storeName.add("고기 나라 ");
        List<String> storeContent= new ArrayList<>();
        storeContent.add("매운 맛의 절정");
        storeContent.add("분식의 대가");
        storeContent.add("간단하고 맛있게 먹고 싶을땐?");
        storeContent.add("순대국의 최고봉");
        storeContent.add("칼국수와 수제비의 만남 칼제비");
        storeContent.add("명불허전 최고의 패밀리 레스토랑");
        storeContent.add("고기와 샐러드 뷔페 모두 최고");
        storeContent.add("MZ들의 최고 뷔페");
        storeContent.add("숯불에 잘 익힌 닭고기 한점");
        storeContent.add("인간은 원래 육식 동물이었다.");
        for(int j=1; j<12; j++) {
            if(j==8){
                return;
            }
            for (int i = 0; i < 10; i++) {
                StoreEntity storeEntity = new StoreEntity();
                storeEntity.setName(storeName.get(i) + hotelRepository.findById((long) j).get().getName());
                storeEntity.setContent(storeContent.get(i));
                storeEntity.setHotelEntity(hotelRepository.findById((long) j).get());
                storeRepository.save(storeEntity);
            }
        }

    }

}