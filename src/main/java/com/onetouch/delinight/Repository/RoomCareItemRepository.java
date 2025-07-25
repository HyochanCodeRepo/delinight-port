/*********************************************************************
 * 클래스명 : RoomCareItemRepository
 * 기능 : 룸케어 메뉴 항목들
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.RoomCareItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomCareItemRepository extends JpaRepository<RoomCareItemEntity, Long> {
}
