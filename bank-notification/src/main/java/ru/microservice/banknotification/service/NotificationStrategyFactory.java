package ru.microservice.banknotification.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.microservice.banknotification.domain.EmailType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NotificationStrategyFactory {

    private final Map<EmailType, NotificationStrategy> strategies;

    public NotificationStrategyFactory(List<NotificationStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(
                        NotificationStrategy::getEmailType,
                        Function.identity()
                ));
    }

    public NotificationStrategy getStrategy(EmailType emailType) {
        NotificationStrategy strategy = strategies.get(emailType);
        if(strategy == null) {
            throw new IllegalArgumentException("No strategy found for type " + emailType);
        }
        return strategy;
    }

}
