/*****************************
 * 작성자 : 이동건
 * 공용모듈
 * 필요할 때마다 사용가능한 메소드
 * ***************************/
package com.onetouch.delinight.Util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class PasswordUtil {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    // 랜덤할 임시 비밀번호 생성
    public static String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 7);
    }

    // 비밀번호 암호화(해싱)
    public static String encodePassword(String newPassword) {
        return encoder.encode(newPassword);
    }

}
