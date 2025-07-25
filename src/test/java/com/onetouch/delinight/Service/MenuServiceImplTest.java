package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.Menu;
import com.onetouch.delinight.Constant.MenuStatus;
import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.Entity.MenuEntity;
import com.onetouch.delinight.Entity.StoreEntity;
import com.onetouch.delinight.Repository.MenuRepository;
import com.onetouch.delinight.Repository.StoreRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class MenuServiceImplTest {

    @Autowired
    MenuService menuService;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    StoreRepository storeRepository;


//
//
//
//    @Test
//    public void register() {
//
//        MenuDTO menuDTO = new MenuDTO();
//        menuDTO.setName("계란말이");
//        menuDTO.setContent("계란은 단백질이니까");
//        menuDTO.setPrice(4000);
//        menuService.register(menuDTO, "dlwlgh908@naver.com");
//    }


    @Test
    @Commit
    public void insertTest() {
        List<String> menuName = new ArrayList<>();
        List<String> menuContent = new ArrayList<>();
        menuContent.add("매콤한 국물떡볶이");
        menuName.add("엽기떡볶이");

        menuName.add("치즈떡볶이");
        menuContent.add("모짜렐라 치즈 듬뿍");

        menuName.add("주먹밥");
        menuContent.add("참치마요 소스로 버무림");

        menuName.add("오뎅탕");
        menuContent.add("따뜻한 국물과 어묵");

        menuName.add("닭발");
        menuContent.add("불맛 가득 매운 닭발");

        menuName.add("튀김세트");
        menuContent.add("김말이·오징어·야채");

        menuName.add("라면사리");
        menuContent.add("국물에 말아먹는 라면");

        menuName.add("계란찜");
        menuContent.add("부드럽고 폭신한 계란");

        menuName.add("엽기닭볶음탕");
        menuContent.add("칼칼한 국물 닭요리");

        menuName.add("콘치즈");
        menuContent.add("고소한 콘+치즈 조합");
        menuName.add("김밥");
        menuContent.add("기본 재료 가득");

        menuName.add("참치김밥");
        menuContent.add("참치+마요네즈 환상조합");

        menuName.add("돈까스김밥");
        menuContent.add("돈까스와 소스의 조화");

        menuName.add("떡라면");
        menuContent.add("떡이 들어간 얼큰 라면");

        menuName.add("라볶이");
        menuContent.add("라면+떡볶이 퓨전");

        menuName.add("치즈라면");
        menuContent.add("치즈 올린 라면");

        menuName.add("비빔밥");
        menuContent.add("고추장과 나물 비빔");

        menuName.add("제육덮밥");
        menuContent.add("매콤한 제육볶음");

        menuName.add("오므라이스");
        menuContent.add("케찹 소스 계란밥");

        menuName.add("우동");
        menuContent.add("따뜻한 국물의 우동");
        menuName.add("이탈리안비엠티");
        menuContent.add("햄·페퍼로니 조화");

        menuName.add("에그마요");
        menuContent.add("부드러운 계란 샐러드");

        menuName.add("치킨데리야끼");
        menuContent.add("달달한 닭고기 소스");

        menuName.add("스파이시이탈리안");
        menuContent.add("매콤한 소시지 샌드위치");

        menuName.add("터키베이컨");
        menuContent.add("담백한 칠면조 베이컨");

        menuName.add("참치샌드위치");
        menuContent.add("고소한 참치 마요");

        menuName.add("풀드포크");
        menuContent.add("바비큐 스타일 돼지고기");

        menuName.add("스테이크앤치즈");
        menuContent.add("부드러운 스테이크 조합");

        menuName.add("로티세리치킨");
        menuContent.add("촉촉한 닭고기 샐러드");

        menuName.add("야채만샌드");
        menuContent.add("야채만 가득 담은 메뉴");
        menuName.add("순대국밥");
        menuContent.add("진한 국물의 순대국");

        menuName.add("얼큰순대국");
        menuContent.add("매콤하게 끓인 순대국");

        menuName.add("모듬순대");
        menuContent.add("순대·간·허파 구성");

        menuName.add("수육");
        menuContent.add("야들야들한 돼지 수육");

        menuName.add("머릿고기");
        menuContent.add("쫄깃한 머리고기");

        menuName.add("순대볶음");
        menuContent.add("야채와 함께 매콤하게");

        menuName.add("깍두기");
        menuContent.add("아삭한 무김치");

        menuName.add("국수사리");
        menuContent.add("국밥에 넣는 국수");

        menuName.add("공기밥");
        menuContent.add("고슬고슬한 쌀밥");

        menuName.add("막걸리");
        menuContent.add("시원한 전통주");
        menuName.add("바지락칼국수");
        menuContent.add("바지락 시원한 국물");

        menuName.add("들깨칼국수");
        menuContent.add("고소한 들깨 국물");

        menuName.add("김치칼국수");
        menuContent.add("매콤한 김치맛 국물");

        menuName.add("수제비");
        menuContent.add("쫄깃한 반죽 수제비");

        menuName.add("비빔국수");
        menuContent.add("매콤한 양념 비빔");

        menuName.add("왕만두");
        menuContent.add("속이 꽉 찬 만두");

        menuName.add("보쌈");
        menuContent.add("김치와 함께 즐기는");

        menuName.add("콩국수");
        menuContent.add("여름철 별미 콩국수");

        menuName.add("칼제비");
        menuContent.add("칼국수+수제비 조합");

        menuName.add("오이무침");
        menuContent.add("새콤한 입가심 반찬");
        menuName.add("투움바파스타");
        menuContent.add("크림+매운 소스 조화");

        menuName.add("베이비백립");
        menuContent.add("달콤한 바비큐 폭립");

        menuName.add("스테이크세트");
        menuContent.add("고급 소고기 스테이크");

        menuName.add("치킨텐더");
        menuContent.add("바삭한 닭가슴살 튀김");

        menuName.add("감바스");
        menuContent.add("마늘향 새우 요리");

        menuName.add("시저샐러드");
        menuContent.add("신선한 야채와 드레싱");

        menuName.add("머쉬룸수프");
        menuContent.add("진한 버섯 향 가득");

        menuName.add("갈릭스테이크");
        menuContent.add("마늘향 나는 스테이크");

        menuName.add("청키스프라이스");
        menuContent.add("버터향 볶음밥");

        menuName.add("브라우니");
        menuContent.add("달콤한 디저트 케이크");
        menuName.add("그릴스테이크");
        menuContent.add("불향 가득 스테이크");

        menuName.add("왕새우구이");
        menuContent.add("탱글한 새우 그릴");

        menuName.add("콘치즈피자");
        menuContent.add("달콤한 옥수수 피자");

        menuName.add("수프바");
        menuContent.add("다양한 스프 무제한");

        menuName.add("샐러드바");
        menuContent.add("신선한 야채 무제한");

        menuName.add("로스트치킨");
        menuContent.add("겉바속촉 닭고기");

        menuName.add("크림파스타");
        menuContent.add("부드러운 크림소스");

        menuName.add("핫윙");
        menuContent.add("매콤한 닭날개");

        menuName.add("감자튀김");
        menuContent.add("바삭한 감자 사이드");

        menuName.add("젤리바");
        menuContent.add("다양한 젤리 디저트");
        menuName.add("불고기피자");
        menuContent.add("불고기 토핑 듬뿍");

        menuName.add("치킨텐더");
        menuContent.add("바삭한 치킨 사이드");

        menuName.add("로제파스타");
        menuContent.add("크림+토마토 조화");

        menuName.add("떡볶이");
        menuContent.add("달달한 고추장 양념");

        menuName.add("크림수프");
        menuContent.add("부드럽고 고소한 맛");

        menuName.add("콘샐러드");
        menuContent.add("옥수수와 마요 샐러드");

        menuName.add("훈제오리");
        menuContent.add("짭조름한 훈제향 오리");

        menuName.add("볶음밥");
        menuContent.add("불향 가득 볶은 밥");

        menuName.add("냉모밀");
        menuContent.add("시원한 모밀면");

        menuName.add("푸딩");
        menuContent.add("쫀득한 달콤 디저트");
        menuName.add("숯불닭갈비");
        menuContent.add("숯불향 가득 닭갈비");

        menuName.add("양념닭구이");
        menuContent.add("달콤한 양념맛");

        menuName.add("소금닭구이");
        menuContent.add("담백한 소금구이");

        menuName.add("불닭발");
        menuContent.add("매운 불닭발");

        menuName.add("닭날개구이");
        menuContent.add("바삭한 날개 부위");

        menuName.add("닭똥집볶음");
        menuContent.add("쫄깃한 식감의 별미");

        menuName.add("치즈닭갈비");
        menuContent.add("치즈 올린 닭갈비");

        menuName.add("묵사발");
        menuContent.add("시원한 여름 별미");

        menuName.add("감자전");
        menuContent.add("겉은 바삭, 속은 촉촉");

        menuName.add("막걸리");
        menuContent.add("닭요리와 어울리는 술");
        int count = 0;
        int price = 10000;
        Long storeId = 12L;
        for (int k = 0; k < 2; k++) {
            for (int i = 1; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    price += 1000;
                    if (price > 30000) {
                        price = 10000;
                    }
                    MenuEntity menuEntity = new MenuEntity();
                    menuEntity.setStoreEntity(storeRepository.findById(storeId).get());
                    menuEntity.setMenuStatus(MenuStatus.SELL);
                    menuEntity.setName(menuName.get(count));
                    menuEntity.setContent(menuContent.get(count));
                    menuEntity.setPrice(price);
                    menuEntity.setMenu(Menu.KOREAN);
                    count++;
                    System.out.println(storeId);
                    menuRepository.save(menuEntity);

                }
                storeId++;
            }
            count = 0;
        }

        System.out.println(menuName.size());

    }


    @Test
    public void read() {
        Long id = 1L;
        MenuDTO menuDTO = menuService.read(id);
        log.info(menuDTO);
    }

    @Test
    public void update() {
        MenuEntity menuEntity = menuRepository.findById(1L).get();
        menuEntity.setName("비빔밥");
        menuEntity.setContent("비빔 비비빔");
        menuEntity.setPrice(52000);

        MenuEntity result = menuRepository.save(menuEntity);

    }

}