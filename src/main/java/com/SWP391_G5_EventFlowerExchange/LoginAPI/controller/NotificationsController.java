package com.SWP391_G5_EventFlowerExchange.LoginAPI.controller;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerBatch;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Notifications;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/noti")
public class NotificationsController {
    @Autowired
    private NotificationsService notificationsService;
    @GetMapping("/")
    public ResponseEntity<List<Notifications>> fetchAll() {

        return ResponseEntity.ok(notificationsService.getAllNotifications()) ;
    }
    @PutMapping("/{id}")
    public ResponseEntity<Notifications> updateNotiId(@PathVariable int id, @RequestBody Notifications noti) {
        Notifications updatedNoId = notificationsService.updateNoti(id, noti);
        return ResponseEntity.ok(updatedNoId);
    }
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Notifications saveNoti(@RequestBody Notifications notiId) {
        return notificationsService.insertNotifications(notiId);//201 CREATED
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNoti(@PathVariable int id) {
        notificationsService.deleteNotifications(id);
        return ResponseEntity.ok("Deleted!");
    }
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Notifications>> getNotiById(@PathVariable int id) {
        Optional<Notifications> noti= notificationsService.getNotificationsById(id);
        return ResponseEntity.ok(noti);
    }

    @GetMapping("/user/{userID}")
    public ResponseEntity<List<Notifications>> getNotiByUserID(@PathVariable int userID) {
        List<Notifications> notifications = notificationsService.getNotificationsByUserId(userID);
        return ResponseEntity.ok(notifications);
    }
}
