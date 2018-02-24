package com.xiaomo.funny.home.model;

/**
 * Created by qiao on 17/01/2018.
 */

public class UserModel {
    //用户id
    String userId;
    String userNickname;
    // 用户权重
    int weight;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public UserModel(String userId, String userNickname, int weight) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.weight = weight;
    }
}
