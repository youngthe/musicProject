package com.example.spring.utils;


import com.example.spring.dao.UserTb;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
//    private String secretKey = "85F8331397E3585C47B029F168E669D579558FECC1F13E42372D96596E3389F5";
    private String secretKey = "myproject";


    private long tokenValidTime = 1440 * 60 * 1000L; //맨앞에 꺼 수정(분 단위)


    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createToken(UserTb user) {
//        Claims role = Jwts.claims().setSubject(user); // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.
        Date now = new Date();

        return Jwts.builder()
                .setIssuer(Integer.toString(user.getUserNo()))
//                .setSubject(user.getRole())
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
                .compact();
    }

    //
    // JWT 토큰에서 user의 id 파싱
    public int getUserId(String token) {
        try{
            return Integer.parseInt(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getIssuer());
        }catch(Exception e){
            return 0;
        }
    }


    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
