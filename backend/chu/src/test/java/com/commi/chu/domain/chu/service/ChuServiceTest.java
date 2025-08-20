package com.commi.chu.domain.chu.service;

import com.commi.chu.domain.chu.dto.ChuNicknameRequestDto;
import com.commi.chu.domain.chu.dto.UpdateResponseDto;
import com.commi.chu.domain.chu.entity.Chu;
import com.commi.chu.domain.chu.repository.ChuRepository;
import com.commi.chu.domain.user.entity.User;
import com.commi.chu.domain.user.repository.UserRepository;
import com.commi.chu.global.exception.CustomException;
import com.commi.chu.global.exception.code.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class ChuServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    ChuRepository chuRepository;

    @InjectMocks
    ChuService chuService;
    
    @Test
    @DisplayName("닉네임 변경 성공 - chu 엔티티 상태가 변경된다.")
    public void updateNickname_success() {
        //given
        Integer userId = 1;
        User user = User.builder().build();
        Chu chu = Chu.builder().build();
        chu.updateName("before");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(chuRepository.findByUser(user)).willReturn(Optional.of(chu));

        ChuNicknameRequestDto dto = ChuNicknameRequestDto.builder()
                .nickname("after")
                .build();

        //when
        UpdateResponseDto res = chuService.updateNickname(userId, dto);

        //then
        assertEquals("after", chu.getName()); // 메서드 호출로 상태 변경되었는지
        assertEquals("닉네임이 변경 되었습니다.", res.getMessage());

        //테스트하려는 비즈니스 로직이 리포지토리를 불필요하게 더 호출하지 않는지 확인
        then(userRepository).should().findById(userId);
        then(chuRepository).should().findByUser(user);
        then(userRepository).shouldHaveNoMoreInteractions(); //findById말고 다른 호출은 없어야함.
        then(chuRepository).shouldHaveNoMoreInteractions();
    }


    @Test
    @DisplayName("존재하지 않은 사용자 -> USER_NOT_FOUND")
    public void updateNickname_userNotFound() {
        //given
        Integer userId = 1;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        //when&then
        CustomException ex = assertThrows(CustomException.class, () -> chuService.updateNickname(userId, ChuNicknameRequestDto.builder().nickname("new").build()));
        assertEquals(ErrorCode.USER_NOT_FOUND, ex.getErrorCode());

        then(userRepository).should().findById(userId);
        then(chuRepository).shouldHaveNoMoreInteractions();
    }


    @Test
    @DisplayName("사용자는 있으나 chu 없음 -> CHU_NOT_FOUND")
    public void updateNickname_chuNotFound(){
        //given
        Integer userId = 1;
        User user = User.builder().build();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(chuRepository.findByUser(user)).willReturn(Optional.empty());

        //when&then
        CustomException ex = assertThrows(CustomException.class, () -> chuService.updateNickname(userId, ChuNicknameRequestDto.builder().nickname("new").build()));
        assertEquals(ErrorCode.CHU_NOT_FOUND, ex.getErrorCode());

        then(userRepository).should().findById(userId);
        then(chuRepository).should().findByUser(user);
        then(userRepository).shouldHaveNoMoreInteractions();
        then(chuRepository).shouldHaveNoMoreInteractions();


    }
}
