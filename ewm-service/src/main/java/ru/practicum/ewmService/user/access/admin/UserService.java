package ru.practicum.ewmService.user.access.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.common.PageRequestModified;
import ru.practicum.ewmService.error.exception.NotFoundException;
import ru.practicum.ewmService.user.UserMapper;
import ru.practicum.ewmService.user.UserRepository;
import ru.practicum.ewmService.user.dto.UserDto;
import ru.practicum.ewmService.user.dto.UserNewDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    final UserRepository userRepository;

    public List<UserDto> getEventsByAdmin(List<Long> ids,Integer from,Integer size) {
        PageRequest pageRequest = new PageRequestModified(from, size, Sort.by("id"));
        return userRepository.findByIdIn(ids, pageRequest)
                .stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Transactional
    public UserDto createUserByAdmin(UserNewDto userNewDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userNewDto)));
    }

    @Transactional
    public void deleteUserByAdmin(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с индексом " + userId + " не найден в базе."));
        userRepository.deleteById(userId);
    }
}
