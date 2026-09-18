package com.santiagomac.auth.application.ports.out;

public interface PasswordPort {

    String encryptPassword(String password);
}
