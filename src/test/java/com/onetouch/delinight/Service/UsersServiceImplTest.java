package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.UsersRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class UsersServiceImplTest {
    @Autowired
    UsersService usersService;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    @Commit
    public void insertTest(){

        String userPassword ="akqjqtk12!";
        String email1="user";
        String email2="@test.com";
        String name = "유저님";
        String address = "경기 부천시 부천로 29번길 7, 12층(심곡동)";
int phone = 1011110000;


        for(int i=0; i<1000; i++){
            UsersEntity usersEntity = new UsersEntity();
            usersEntity.setPassword(passwordEncoder.encode(userPassword));
            usersEntity.setEmail(email1+i+email2);
            usersEntity.setName(name+i);
            usersEntity.setAddress(address);
            phone+=1;
            usersEntity.setPhone("0"+phone);
            usersRepository.save(usersEntity);
        }



    }

}