package ru.practicum.ewmService.user;

import ru.practicum.ewmService.user.dto.UserDto;
import ru.practicum.ewmService.user.dto.UserNewDto;
import ru.practicum.ewmService.user.dto.UserShortDto;
import ru.practicum.ewmService.user.model.User;

public class UserMapper {

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserNewDto userNewDto) {
        return User.builder()
                .name(userNewDto.getName())
                .email(userNewDto.getEmail())
                .build();
    }
}
