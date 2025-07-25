package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.ImageEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    Optional<ImageEntity> findByHotelEntity_Id(Long id);
    void deleteByRegTimeIsLessThanEqualAndMenuEntityIsNullAndHotelEntityIsNullAndStoreEntityIsNullAndAndRoomCareMenuEntityIsNull(LocalDateTime time);
    Optional<ImageEntity> findByMenuEntity_Id(Long menuEntityId);
    Optional<ImageEntity> findByStoreEntity_Id(Long storeEntityId);
    Optional<ImageEntity> findByRoomCareMenuEntity_Id(Long menuId);

    void deleteByMenuEntity_Id(Long menuEntityId);
    void deleteByStoreEntity_Id(Long storeEntityId);
    void deleteByRoomCareMenuEntity_Id(Long storeEntityId);
    void deleteByHotelEntity_Id(Long hotelEntityId);

    boolean existsByStoreEntity_Id(Long id);
    boolean existsByHotelEntity_Id(Long id);

}
