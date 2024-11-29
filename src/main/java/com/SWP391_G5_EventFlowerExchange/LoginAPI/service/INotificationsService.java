package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Notifications;

import java.util.List;
import java.util.Optional;

public interface INotificationsService {
    public List<Notifications> getAllNotifications();
    public Notifications insertNotifications(Notifications notifications);
    public void deleteNotifications(int notificationID);
    public Optional<Notifications> getNotificationsById(int notificationID);
    public Notifications updateNoti(int notiId, Notifications notifications);

}
