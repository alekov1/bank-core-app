package ru.microservice.banknotification.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.microservice.banknotification.client.AccountClient;
import ru.microservice.banknotification.domain.EmailType;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final AccountClient accountClient;
    private final NotificationStrategyFactory factory;

    public ResponseEntity<String> notify(EmailType emailType, String accountNumber) {
        String email = accountClient.getEmailByAccountNumber(accountNumber);
        factory.getStrategy(emailType).sendEmail(email);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
