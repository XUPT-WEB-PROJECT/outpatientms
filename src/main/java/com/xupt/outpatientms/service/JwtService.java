package com.xupt.outpatientms.service;

import com.xupt.outpatientms.common.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public static final String USER_JWT_KEY = "user_jwt_%s";

    /** 秘钥 */
    @Value("${jwt.secret-key}")
    private String secret;

    /** 过期时间(秒) */
    @Value("${jwt.expire-time}")
    private long expire;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     *  生成token
     * */
    private String generateToken(String subject, Map<String,Object> claim) {

        // 将密钥加密
        byte[] encodeKey = Base64.getEncoder().encode(secret.getBytes());
        //加密后的密钥
        SecretKey secretKey = Keys.hmacShaKeyFor(encodeKey);
        // 用加密的密钥进行签名
        return Jwts.builder().addClaims(claim).setSubject(subject).signWith(secretKey).compact();
    }

    /**
     * 解析token
     * */
    public Optional<Claims> parseToken(String token){
        byte[] encode = Base64.getEncoder().encode(secret.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(encode);
        Claims claims = null;
        //用签名对token进行解密
        try{
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (JwtException e){
            logger.error("JwtException :{}",e.getMessage());
        }
        //为null就返回一个空的optional
        //不为null返回optional
        return Optional.ofNullable(claims);
    }

    /**
     * 重新刷新token
     * */
    public Token refreshToken(String userId){
        Map<String, Object> claim = new HashMap<>();
        claim.put("userId",userId);
        claim.put("createDate",System.currentTimeMillis());
        Token token = new Token(generateToken(userId, claim),expire);
        redisTemplate.opsForValue().set(String.format(USER_JWT_KEY,userId),userId,token.getExpire(), TimeUnit.SECONDS);
        return token;
    }

    /**
     * 使token失效
     * */
    public void removeToken(String token){
        Optional<Claims> claimsOptional = parseToken(token);
        claimsOptional.ifPresent(claim -> redisTemplate.delete(String.format(USER_JWT_KEY,claim.get("userId"))));
    }

}
