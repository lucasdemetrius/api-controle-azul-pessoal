package com.controleazulpessoal.finance_api.usecase.user.mapper;

import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.usecase.user.output.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto entityToDto(User user);

}
