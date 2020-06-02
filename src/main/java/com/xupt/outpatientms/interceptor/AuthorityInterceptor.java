package com.xupt.outpatientms.interceptor;


import com.xupt.outpatientms.common.CurrentUserData;
import com.xupt.outpatientms.service.JwtService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class AuthorityInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthorityInterceptor.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtService jwtService;

    public AuthorityInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger.info("in authority");

        if (HttpMethod.OPTIONS.name().equals(request.getMethod())){
            return true;
        }
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotEmpty(header)){
            if (header.startsWith("Bearer ")){
                String token = header.substring(7);
                Optional<Claims> claimsOptional = jwtService.parseToken(token);
                //合法 token
                if (claimsOptional.isPresent()){
                    String id = claimsOptional.get().getSubject();
                    String key = String.format(JwtService.USER_JWT_KEY,id);
                    if (BooleanUtils.isTrue(redisTemplate.hasKey(key))){
                        redisTemplate.expire(key, 3600, TimeUnit.SECONDS);
                        CurrentUserData currentUserData = new CurrentUserData();
                        currentUserData.setId(id);
                        request.setAttribute("currentUser", currentUserData);
                        return true;
                    }
                }
            }

        }

        logger.warn("Wrong authorization: {}", header);
        response.sendRedirect("/xuptcd/unauthorized");
        return true;
    }

}
