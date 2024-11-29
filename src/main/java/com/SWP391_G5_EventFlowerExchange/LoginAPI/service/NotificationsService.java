package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Notifications;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.INotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationsService implements INoticationService {
    @Autowired
    private INotificationsRepository inotificationsRepository;

    @Override
    public List<Notifications> getAllNotifications() {
        return inotificationsRepository.findAll();
    }

    @Override
    public Notifications insertNotifications(Notifications notifications) {
        return inotificationsRepository.save(notifications);
    }

    @Override
    public void deleteNotifications(int notificationID) {
            inotificationsRepository.deleteById(notificationID);
    }

    @Override
    public Optional<Notifications> getNotificationsById(int notificationID) {
        return inotificationsRepository.findById(notificationID);
    }
    @Override
    public Notifications updateNoti(int notiId, Notifications notifications) {
        Notifications noti=inotificationsRepository.getById(notiId);
        if(noti != null){
            if (notifications.getContent() != null) {
                noti.setContent(notifications.getContent());
            }
            if (notifications.getNotificationType() != null) {
                noti.setNotificationType(notifications.getNotificationType());
            }
            if (notifications.getCreatedAt() != null) {
                noti.setCreatedAt(notifications.getCreatedAt());
            }
            if (notifications.getUser() != null) {
                noti.setUser(notifications.getUser());
            }
            inotificationsRepository.save(noti);

        }
        return null;
    }
    public List<Notifications> getNotificationsByUserId(int userID) {
        return inotificationsRepository.findByUser_userID(userID);
    }
}
