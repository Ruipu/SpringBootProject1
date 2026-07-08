package net.javaguides.ems.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private static final Set<String> ADMIN_EMAILS = Set.of(
            "rgao4@tulane.edu"
    );

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return buildOAuth2User(oAuth2User);
    }

    OAuth2User buildOAuth2User(OAuth2User oAuth2User) {
        String role = determineRole(oAuth2User);
        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority(role)),
                oAuth2User.getAttributes(),
                "email"
        );
    }

    String determineRole(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email != null && ADMIN_EMAILS.contains(email)) {
            return "ROLE_ADMIN";
        }
        return "ROLE_USER";
    }
}