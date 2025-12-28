package com.dirijable.springstarter.financetracker.mapper;

import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.user.UserCreateDto;
import com.dirijable.springstarter.financetracker.dto.user.UserResponseDto;
import com.dirijable.springstarter.financetracker.dto.user.UserUpdateDto;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    User toEntity(UserCreateDto createDto);

    void updateEntity(UserUpdateDto updateDto, @MappingTarget User user);

    UserResponseDto toResponse(User user);
}




//
//public User toEntity(UserCreateDto createDto) {
//    return User.builder()
//            .email(createDto.email())
//            .password(createDto.password())
//            .username(createDto.username())
//            .build();
//}
//
//public void updateEntity(UserUpdateDto updateDto, User user) {
//    if (updateDto.email() != null)
//        user.setEmail(updateDto.email());
//    if (updateDto.username() != null)
//        user.setUsername(updateDto.username());
//    if (updateDto.password() != null)
//        user.setPassword(updateDto.password());
//}
//
//public UserResponseDto toResponse(User user) {
//    return new UserResponseDto(
//            user.getId(),
//            user.getEmail(),
//            user.getUsername()
//    );
//}