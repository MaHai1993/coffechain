package com.kuro.coffechain.service;

import com.kuro.coffechain.dto.UserDTO;

import java.util.List;

public interface AdminService {
    List<UserDTO> getAllUsers();
}
