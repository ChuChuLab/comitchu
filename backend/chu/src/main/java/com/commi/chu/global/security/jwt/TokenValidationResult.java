package com.commi.chu.global.security.jwt;

public record TokenValidationResult(boolean isValid, TokenError error) {
}
