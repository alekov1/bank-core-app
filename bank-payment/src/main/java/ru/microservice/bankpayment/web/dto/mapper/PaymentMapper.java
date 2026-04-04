package ru.microservice.bankpayment.web.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.microservice.bankpayment.domain.Payment;
import ru.microservice.bankpayment.web.dto.PaymentRequest;
import ru.microservice.bankpayment.web.dto.PaymentResponse;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "createdAt",
            dateFormat = "yyyy-MM-dd HH:mm:ss")
    PaymentResponse toResponse(Payment payment);

    PaymentRequest toRequest(Payment payment);
}
