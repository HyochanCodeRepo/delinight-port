package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    Optional<CartItemEntity> findByCartEntity_IdAndMenuEntity_Id(Long cartEntityNum, Long menuEntityNum);
    List<CartItemEntity> findByCartEntity_Id(Long CartEntityId);

    void deleteByCartEntity_Id(Long cartNum);


}
