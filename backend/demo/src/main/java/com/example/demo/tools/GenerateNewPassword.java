package com.example.demo.tools;

public class GenerateNewPassword {
    public static void main(String[] args) {
        String newPassword = PasswordEncoder.encode("123456");
        System.out.println("新生成的密码：");
        System.out.println(newPassword);
    }
}
