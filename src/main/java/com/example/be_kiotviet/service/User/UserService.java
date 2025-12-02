package com.example.be_kiotviet.service.User;

import com.example.be_kiotviet.dto.User.UserCreateRequest;
import com.example.be_kiotviet.dto.User.UserDto;
import com.example.be_kiotviet.dto.User.UserUpdateRequest;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();  // Lấy tất cả người dùng
    List<UserDto> getAllActiveUsers();  // Lấy tất cả người dùng đang hoạt động
    UserDto getById(Long id);  // Lấy người dùng theo id
    UserDto create(UserCreateRequest req);  // Tạo người dùng mới
    UserDto update(Long id, UserUpdateRequest req);  // Cập nhật người dùng
    void delete(Long id);  // Xóa người dùng
}
