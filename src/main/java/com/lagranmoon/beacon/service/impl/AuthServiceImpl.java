package com.lagranmoon.beacon.service.impl;

import com.lagranmoon.beacon.exception.UnAuthorizedException;
import com.lagranmoon.beacon.mapper.AuthMapper;
import com.lagranmoon.beacon.model.AuthDto;
import com.lagranmoon.beacon.model.AuthRequestDto;
import com.lagranmoon.beacon.model.WechatAuthResp;
import com.lagranmoon.beacon.model.domain.UserAuth;
import com.lagranmoon.beacon.service.AuthService;
import com.lagranmoon.beacon.service.WechatService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;

/**
 * @author Lagranmoon
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {


    @Resource
    private AuthMapper authMapper;

    @Resource
    private ValueOperations redisTemplate;

    @Resource
    private WechatService wechatService;

    @Override
    @SuppressWarnings("unchecked")
    public AuthDto auth(AuthRequestDto requestDto) {

        log.debug(requestDto.getCode());

//        WechatAuthResp resp = wechatService.code2Session(requestDto.getCode());

        WechatAuthResp resp =  WechatAuthResp.builder()
                .openId("hsdjfhsfhiw")
                .sessionKey("reiunjsdbnw")
                .build();

        log.debug(resp.toString());

        if (StringUtils.isEmpty(resp.getSessionKey())) {
            throw new UnAuthorizedException(resp.getErrMsg(), resp.getErrCode());
        }

        UserAuth userAuth = UserAuth
                .builder()
                .userName(requestDto.getNickName())
                .openId(resp.getOpenId())
                .sessionKey(resp.getSessionKey())
                .build();

        authMapper.saveUser(userAuth);


        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        log.info(String.valueOf(userAuth.getId()));

        String token = Jwts.builder()
                .signWith(key)
                .setSubject(String.valueOf(userAuth.getId()))
                .setExpiration(new Date(System.currentTimeMillis() + 3 * 24 * 3600 * 1000))
                .compact();

        redisTemplate.set(token,
                Base64Utils.encodeToString(key.getEncoded()), Duration.ofDays(3));


        return new AuthDto(userAuth.getId(), token);
    }

    @Override
    public String verify(String token) {
        try {

            String key = (String) redisTemplate.get(token);

            log.info(key);

            Objects.requireNonNull(key);

            String uid = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            log.info(uid);

            return authMapper.getOpenIdById(Long.valueOf(uid));
        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
            log.info("{} is invalid", token);
            return "";
        }
    }


}
