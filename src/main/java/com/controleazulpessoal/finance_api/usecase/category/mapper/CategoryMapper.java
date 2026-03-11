package com.controleazulpessoal.finance_api.usecase.category.mapper;

import com.controleazulpessoal.finance_api.persistence.entity.Category;
import com.controleazulpessoal.finance_api.usecase.category.output.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    CategoryDto entityToDto(Category category);
}