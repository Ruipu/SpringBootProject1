package net.javaguides.ems.security;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomOAuth2UserServiceTest {

    @Test
    void determineRole_returnsRoleUser() {
        CustomOAuth2UserService service = new CustomOAuth2UserService();
        OAuth2User mockUser = Mockito.mock(OAuth2User.class);

        String role = service.determineRole(mockUser);

        assertEquals("ROLE_USER", role);
    }

    @Test
    void buildOAuth2User_assignsRoleUserAuthority() {
        CustomOAuth2UserService service = new CustomOAuth2UserService();
        OAuth2User mockUser = Mockito.mock(OAuth2User.class);
        Mockito.when(mockUser.getAttributes()).thenReturn(Map.of("email", "test@example.com"));

        OAuth2User result = service.buildOAuth2User(mockUser);

        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }
}