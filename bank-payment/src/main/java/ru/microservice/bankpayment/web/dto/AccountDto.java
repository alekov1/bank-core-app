package ru.microservice.bankpayment.web.dto;



import lombok.Data;

import java.math.BigDecimal;



@Data
public class AccountDto {
        Long id;

        String accountNumber;

        BigDecimal balance;



}
