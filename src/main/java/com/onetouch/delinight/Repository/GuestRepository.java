package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<GuestEntity, Long> {


    Optional<GuestEntity> findByPhone(String phone);

}
