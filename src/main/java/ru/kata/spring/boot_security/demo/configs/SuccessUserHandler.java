package ru.kata.spring.boot_security.demo.configs;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(SuccessUserHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        logger.info("Успешная аутентификация. Роли: {}", roles);

        if (roles.contains("ROLE_ADMIN")) {
            logger.info("Редирект на /admin");
            httpServletResponse.sendRedirect("/admin");
        } else if (roles.contains("ROLE_USER")) {
            logger.info("Редирект на /user");
            httpServletResponse.sendRedirect("/user");
        } else {
            logger.info("Редирект на /");
            httpServletResponse.sendRedirect("/");
        }
    }
}