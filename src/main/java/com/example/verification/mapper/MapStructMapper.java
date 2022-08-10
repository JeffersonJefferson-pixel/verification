package com.example.verification.mapper;

import com.example.verification.dto.OtpDto;
import com.example.verification.dto.UserDto;
import com.example.verification.model.Otp;
import com.example.verification.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
  UserDto userToUserDto(User user);

  User userDtoToUser(UserDto userDto);

  OtpDto otpToOtpDto(Otp otp);

  Otp otpDtoToOtp(OtpDto otpDto);
}
