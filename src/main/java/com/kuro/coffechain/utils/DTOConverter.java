package com.kuro.coffechain.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DTOConverter {

    private final ModelMapper modelMapper;

    public DTOConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Generic Method to Convert Entity to DTO
    public <D, T> D convertToDTO(T entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }


    // Generic Method to Convert DTO to Entity
    public <T, D> T convertToEntity(D dto, Class<T> entityClass) {
        return modelMapper.map(dto, entityClass);
    }
}
