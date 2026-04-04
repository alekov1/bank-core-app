package ru.microservice.bankaccount.web.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.microservice.bankaccount.domain.Account;
import ru.microservice.bankaccount.web.dto.AccountResponse;



@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "createdAt",
            dateFormat = "yyyy-MM-dd HH:mm:ss")
    AccountResponse toResponse(Account account);
}
