package com.book.password;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordManager {
    // 비밀번호를 해싱하는 메소드
    public static String hashPassword(String plainTextPassword) {
        // BCrypt.gensalt()를 호출하여 자동으로 솔트를 생성
        String salt = BCrypt.gensalt(12);
        // 솔트를 사용하여 비밀번호를 해싱
        return BCrypt.hashpw(plainTextPassword, salt);
    }

    // 저장된 해시와 사용자가 입력한 비밀번호가 일치하는지 검증하는 메소드
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        // BCrypt.checkpw()를 사용하여 비밀번호가 일치하는지 확인
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
