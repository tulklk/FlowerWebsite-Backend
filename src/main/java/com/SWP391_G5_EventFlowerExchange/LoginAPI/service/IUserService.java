package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.UserCreationRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.UserUpdateRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.UserResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.User;

import java.util.List;

public interface IUserService {
    User createUser(UserCreationRequest request);
    UserResponse getUser(int userID);
    User updateUser(int userID, UserUpdateRequest request);
    User UpdateUserIntoSeller(int userID);
    List<User> getUsers();
    void deleteUser(int userID);
    User findById(int userID);
}
