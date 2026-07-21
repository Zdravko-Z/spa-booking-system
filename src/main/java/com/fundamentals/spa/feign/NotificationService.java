package com.fundamentals.spa.feign;

import com.fundamentals.spa.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "${services.notification.url}")
public interface NotificationService {
    @PostMapping("/api/v1/notifications/confirmation")
    void sendConfirmation(@RequestBody NotificationRequest request);
}
