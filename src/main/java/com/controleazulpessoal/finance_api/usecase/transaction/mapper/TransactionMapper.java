package com.controleazulpessoal.finance_api.usecase.transaction.mapper;

import com.controleazulpessoal.finance_api.persistence.entity.Transaction;
import com.controleazulpessoal.finance_api.usecase.transaction.output.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {

    TransactionDto entityToDto(Transaction transaction);

}
