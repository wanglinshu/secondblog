package com.secondblog.secondblog.service;

import com.secondblog.secondblog.po.User;


public interface UserService {

    User checkUser(String username, String password);
}
