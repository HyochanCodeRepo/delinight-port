package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.Service.RoomCareItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/roomCareItem")
public class RoomCareItemRestController {
    private final RoomCareItemService roomCareItemService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRoomCareItem(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();

        roomCareItemService.del(id, email);

        return ResponseEntity.noContent().build();
    }
}
