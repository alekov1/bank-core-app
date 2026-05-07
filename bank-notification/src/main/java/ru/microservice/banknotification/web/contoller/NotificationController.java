package ru.microservice.banknotification.web.contoller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.microservice.banknotification.domain.EmailType;
import ru.microservice.banknotification.service.NotificationService;

@Controller
@RequestMapping("/api/v1/notify")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @PostMapping
    public ResponseEntity<String> notify(@RequestBody EmailType emailType, String accountNumber) {
        notificationService.notify(emailType, accountNumber);
        return ResponseEntity.ok().build();
    }

}
