package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.NetPromoterScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NetPromoterScoreRepository extends JpaRepository<NetPromoterScoreEntity , Long> {

    List<NetPromoterScoreEntity> findAll();


    List<NetPromoterScoreEntity> findByHotelEntity_MembersEntity_EmailAndRegTimeBetween(String email, LocalDateTime startDate, LocalDateTime endDate);
    List<NetPromoterScoreEntity> findByStoreEntity_MembersEntity_EmailAndRegTimeBetween(String email, LocalDateTime startDate, LocalDateTime endDate);
    List<NetPromoterScoreEntity> findByHotelEntity_BranchEntity_CenterEntity_MembersEntity_EmailAndRegTimeBetween(String email, LocalDateTime startDate, LocalDateTime endDate);
    List<NetPromoterScoreEntity> findByStoreEntity_MembersEntity_Id(Long membersId);
    List<NetPromoterScoreEntity> findByStoreEntity_HotelEntity_MembersEntity_IdOrHotelEntity_MembersEntity_Id(Long membersId, Long membersId2);
    List<NetPromoterScoreEntity> findByStoreEntity_HotelEntity_BranchEntity_CenterEntity_MembersEntity_IdOrHotelEntity_BranchEntity_CenterEntity_MembersEntity_Id(Long membersId, Long membersId2);


}
