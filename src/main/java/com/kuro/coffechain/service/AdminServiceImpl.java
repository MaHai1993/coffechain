package com.kuro.coffechain.service;

import com.kuro.coffechain.dto.UserDTO;
import com.kuro.coffechain.repository.UserRepository;
import com.kuro.coffechain.utils.DTOConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    private UserRepository userRepository;
    private DTOConverter dtoConverter;

    public AdminServiceImpl(UserRepository userRepository, DTOConverter dtoConverter) {
        this.userRepository = userRepository;
        this.dtoConverter = dtoConverter;
    }

    // Get All Users and Convert to DTOs
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> dtoConverter.convertToDTO(user, UserDTO.class))
                .collect(Collectors.toList());
    }
}
