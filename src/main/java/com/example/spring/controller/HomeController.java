package com.example.spring.controller;

import com.example.spring.dao.*;
import com.example.spring.repository.*;
import com.example.spring.utils.JwtTokenProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class HomeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MusicLikeRepository musicLikeRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private PlayListMusicRepository playListMusicRepository;

    @Autowired
    private PlayListCategoryRepository playListCategoryRepository;


    @Operation(summary = "서버 동작 확인용", description = "ello 메세지만 뿌림")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public HashMap test() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("message", "hello");

        return result;
    }

    @Operation(summary = "로그인", description = "ID, PW를 통한 로그인")
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "아이디", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pw", value = "비밀번호", required = true, dataType = "string"),
    })
    public HashMap loginCheck(@RequestBody HashMap<String, Object> data) {

        HashMap<String, Object> result = new HashMap<>();

        if (ObjectUtils.isEmpty(data.get("account"))) {
            result.put("message", "account is null");
            result.put("resultCode", false);
            return result;
        }
        if (ObjectUtils.isEmpty(data.get("pw"))) {
            result.put("message", "pw is null");
            result.put("resultCode", false);
            return result;
        }
        String account = data.get("account").toString();
        String pw = data.get("pw").toString();

        log.info("account : {}", account);

        if(ObjectUtils.isEmpty(userRepository.getUserTbByAccount(account))){
            result.put("message", "not exist");
            result.put("resultCode", false);
            return result;
        }

        try {

            UserTb user = userRepository.getUserTbByAccount(account);

            String get_pw = user.getPw();

            if (passwordEncoder.matches(pw, get_pw)) {

                result.put("resultCode", "true");
                result.put("jwt", jwtTokenProvider.createToken(user));
                return result;

            } else {
                result.put("message", "no match");
                result.put("resultCode", false);
                return result;
            }
        } catch (Exception e) {
            log.info("{}", e);
            result.put("message", "db error");
            result.put("resultCode", false);
            return result;
        }
    }

    @Operation(summary = "일반 회원가입", description = "아이디 비밀번호 별명을 통한 회원가입")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "아이디", required = true),
            @ApiImplicitParam(name = "pw", value = "비밀번호", required = true),
            @ApiImplicitParam(name = "nickname", value = "별명", required = true)
    })
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public HashMap Register(@RequestBody HashMap<String, Object> data) {

        HashMap<String, Object> result = new HashMap<>();
        log.info("/user/register");

        if (ObjectUtils.isEmpty(data.get("account"))) {
            result.put("message", "account is null");
            result.put("resultCode", false);
            return result;
        }
        if (ObjectUtils.isEmpty(data.get("pw"))) {
            result.put("message", "pw is null");
            result.put("resultCode", false);
            return result;
        }
        if (ObjectUtils.isEmpty(data.get("nickname"))) {
            result.put("message", "nickname is null");
            result.put("resultCode", false);
            return result;
        }

        String account = data.get("account").toString();
        String pw = data.get("pw").toString();
        String nickname = data.get("nickname").toString();

        if (ObjectUtils.isEmpty(userRepository.getUserTbByAccount(account))){
            try {
                UserTb userTb = new UserTb();
                userTb.setAccount(account);
                userTb.setPw(passwordEncoder.encode(pw));
                userTb.setNickName(nickname);
                userTb.setLoginType("LOCAL");
                userRepository.save(userTb);
                result.put("resultCode", true);
                return result;
            } catch (Exception e) {
                result.put("resultCode", false);
                return result;
            }
        } else {
            result.put("message", "already");
            result.put("resultCode", false);
            return result;
        }
    }


    @ApiOperation(value = "내 정보 확인", notes = "현재 로그인 되어 있는 사용자의 정보")
    @RequestMapping(value = "/user/my-info", method = RequestMethod.GET)
    public HashMap getMyInfo(@RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if (!jwtTokenProvider.validateToken(tokenHeader)) {
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        int user_id = jwtTokenProvider.getUserId(tokenHeader);

        try {
            UserTb userTb = userRepository.getReferenceById(user_id);
            result.put("user", userTb);
            result.put("resultCode", true);
            return result;

        } catch (Exception e) {
            log.info("{}", e);
            result.put("message", "error");
            result.put("resultCode", false);
            return result;
        }
    }

    @ApiOperation(value = "간편 로그인", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name="email", value = "회원 아이디"),
    }
    )
    @RequestMapping(value = "/user/login/simple", method = RequestMethod.POST)
    public Map<String, Object> simpleLogin(@RequestBody HashMap<String, Object> data) {

        log.info("/api/user/simple/login");
        Map<String, Object> result = new HashMap<>();

        if (ObjectUtils.isEmpty(data.get("EMAIL"))) {
            result.put("message", "EMAIL is null");
            result.put("resultCode", false);
            return result;
        }

        try {

            UserTb userTb = userRepository.getUserTbByAccount(data.get("EMAIL").toString());
            if(ObjectUtils.isEmpty(userTb)){
                result.put("message", "empty");
                result.put("resultCode", false);
                return result;
            } else{
                String token = jwtTokenProvider.createToken(userTb);
                result.put("resultCode", true);
                result.put("jwt", token);
                return result;
            }

        } catch (Exception e) {
            log.info("{}", e);
            result.put("message", "db error");
            result.put("resultCode", false);
            return result;
        }
    }

    @Operation(summary = "간편 회원가입", description = "아이디 비밀번호 별명을 통한 회원가입")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "", required = true),
    })
    @RequestMapping(value = "/user/simple/register", method = RequestMethod.POST)
    public HashMap simpleRegister(@RequestBody HashMap<String, Object> data) {

        HashMap<String, Object> result = new HashMap<>();

        if (ObjectUtils.isEmpty(data.get("email"))) {
            result.put("message", "account is null");
            result.put("resultCode", false);
            return result;
        }

        String account = data.get("email").toString();

        if (ObjectUtils.isEmpty(userRepository.getUserTbByAccount(account))){
            try {

                UserTb userTb = new UserTb();
                userTb.setAccount(account);
                userTb.setPw("");
                userTb.setNickName("");
                userTb.setLoginType("GOOGLE");
                result.put("resultCode", true);
                return result;

            } catch (Exception e) {
                result.put("resultCode", false);
                return result;
            }
        } else {
            result.put("message", "already");
            result.put("resultCode", false);
            return result;
        }
    }

//    @ApiOperation(value = "회원 삭제", notes = "")
//    @RequestMapping(value = "/user/my-info", method = RequestMethod.DELETE)
//    public HashMap deleteMyInfo(@RequestHeader("token") String tokenHeader) {
//
//        HashMap<String, Object> result = new HashMap<>();
//
//        if (!jwtTokenProvider.validateToken(tokenHeader)) {
//            result.put("message", "Token validate");
//            result.put("resultCode", "false");
//            return result;
//        }
//
//        int user_id = jwtTokenProvider.getUserId(tokenHeader);
//
//        try {
//
//            musicRepository
//
//            result.put("resultCode", true);
//            return result;
//
//        } catch (Exception e) {
//            log.info("{}", e);
//            result.put("message", "error");
//            result.put("resultCode", false);
//            return result;
//        }
//    }

}