package com.commi.chu.domain.user.service;

import com.commi.chu.domain.user.dto.response.TestResponse;
import com.commi.chu.domain.user.dto.response.UserResponse;
import com.commi.chu.domain.user.entity.User;
import com.commi.chu.domain.user.repository.UserRepository;
import com.commi.chu.global.exception.CustomException;
import com.commi.chu.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public TestResponse getGoSuTest(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "userId", userId));

        return TestResponse.builder()
                .userId(userId)
                .comment("고-수")
                .build();
    }

    public UserResponse getUserInfo(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "userId", userId));

        return UserResponse.builder()
                .userName(user.getGithubUsername())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
