package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerBatch;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Notifications;

import java.util.List;
import java.util.Optional;

public interface INoticationService {
//    public List<FlowerBatch> getAllFlowerBatch();
//    public FlowerBatch insertFlowerBatch(FlowerBatch flowerBatch);
//    public FlowerBatch updateFlowerBatch(int flowerID, FlowerBatch flowerBatch);
//    public void deleteFlowerBatch(int flowerID);
//    public Optional<FlowerBatch> getFlowerBatchById(int flowerID);
        public List<Notifications> getAllNotifications();
        public Notifications insertNotifications(Notifications notifications);
        public void deleteNotifications(int notificationID);
        public Optional<Notifications> getNotificationsById(int notificationID);

        public Notifications updateNoti(int notiId, Notifications notifications);
        
}
