package com.example.toutiao.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {//存储一次访问的用户是谁
    private static ThreadLocal<User> users = new ThreadLocal<User>();//线程本地变量

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}

