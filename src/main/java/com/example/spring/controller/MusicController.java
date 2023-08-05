package com.example.spring.controller;


import com.example.spring.dao.MusicLikeTb;
import com.example.spring.dao.MusicTb;
import com.example.spring.dao.PlayListCategoryTb;
import com.example.spring.dao.PlayListMusicTb;
import com.example.spring.dto.PlayListDto;
import com.example.spring.repository.MusicLikeRepository;
import com.example.spring.repository.MusicRepository;
import com.example.spring.repository.PlayListCategoryRepository;
import com.example.spring.repository.PlayListMusicRepository;
import com.example.spring.utils.JwtTokenProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Slf4j
public class MusicController {


    @Autowired
    private MusicLikeRepository musicLikeRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private PlayListMusicRepository playListMusicRepository;

    @Autowired
    private PlayListCategoryRepository playListCategoryRepository;


    @ApiOperation(value = "내가 좋아요 한 음악 리스트 조회", notes = "")
    @RequestMapping(value = "/music/like", method = RequestMethod.GET)
    public Map<String, Object> getMusicLike(@RequestHeader String tokenHeader) {

        Map<String, Object> result = new HashMap<>();

        int user_id = jwtTokenProvider.getUserId(tokenHeader);

        List<MusicLikeTb> musicLikeTbList = musicLikeRepository.getMusicLikeTbsByUserNoAndType(user_id, 1);

        List<MusicTb> musicTbList = new ArrayList<>();

        for (MusicLikeTb musicLikeTb : musicLikeTbList) {
            MusicTb musicTb = musicRepository.getReferenceById(musicLikeTb.getMusicNo());
            musicTbList.add(musicTb);
        }

        result.put("list", musicTbList);
        result.put("resultCode", true);
        return result;
    }

    @ApiOperation(value = "음악 좋아요", notes = "한번 누르면 좋아요, 두번 누르면 좋아요 해제")
    @RequestMapping(value = "/music/like/{music_num}", method = RequestMethod.PUT)
    public Map<String, Object> putMusicLike(@RequestHeader String tokenHeader, @PathVariable int music_num) {

        Map<String, Object> result = new HashMap<>();

        int user_id = jwtTokenProvider.getUserId(tokenHeader);

        if (!musicRepository.existsById(music_num)) {
            result.put("message", "not exist music");
            result.put("resultCode", false);
            return result;
        }

        if (!musicLikeRepository.existsMusicLikeTbByMusicNoAndUserNo(music_num, user_id)) {
            MusicLikeTb musicLikeTb = new MusicLikeTb();
            musicLikeTb.setMusicNo(music_num);
            musicLikeTb.setUserNo(user_id);
            musicLikeTb.setType(1);
            musicLikeRepository.save(musicLikeTb);
            result.put("message", "put");
        } else {
            MusicLikeTb musicLikeTb = musicLikeRepository.getMusicLikeTbByMusicNoAndUserNoAndType(music_num, user_id, 1);
            musicLikeRepository.delete(musicLikeTb);
            result.put("message", "unPut");
        }
        result.put("resultCode", true);
        return result;
    }

    @ApiOperation(value = "내가 싫어요 한 음악 리스트 조회", notes = "")
    @RequestMapping(value = "/music/unlike", method = RequestMethod.GET)
    public Map<String, Object> getMusicUnLike(@RequestHeader String tokenHeader) {

        Map<String, Object> result = new HashMap<>();

        int user_id = jwtTokenProvider.getUserId(tokenHeader);

        List<MusicLikeTb> musicLikeTbList = musicLikeRepository.getMusicLikeTbsByUserNoAndType(user_id, 1);

        List<MusicTb> musicTbList = new ArrayList<>();
        for (MusicLikeTb musicLikeTb : musicLikeTbList) {
            System.out.println(musicLikeTb.getMusicNo());
        }

        result.put("list", musicTbList);
        result.put("resultCode", true);
        return result;
    }

    @ApiOperation(value = "음악 싫어요", notes = "한번 누르면 싫어요, 두번 누르면 싫어요 해제")
    @RequestMapping(value = "/music/unlike/{music_num}", method = RequestMethod.PUT)
    public Map<String, Object> putMusicUnLike(@RequestHeader String tokenHeader, @PathVariable int music_num) {

        Map<String, Object> result = new HashMap<>();

        int user_id = jwtTokenProvider.getUserId(tokenHeader);

        if (!musicRepository.existsById(music_num)) {
            result.put("message", "not exist music");
            result.put("resultCode", false);
            return result;
        }

        if (!musicLikeRepository.existsMusicLikeTbByMusicNoAndUserNo(music_num, user_id)) {
            MusicLikeTb musicLikeTb = new MusicLikeTb();
            musicLikeTb.setMusicNo(music_num);
            musicLikeTb.setUserNo(user_id);
            musicLikeTb.setType(1);
            musicLikeRepository.save(musicLikeTb);
            result.put("message", "put");
        } else {
            MusicLikeTb musicLikeTb = musicLikeRepository.getMusicLikeTbByMusicNoAndUserNoAndType(music_num, user_id, 2);
            musicLikeRepository.delete(musicLikeTb);
            result.put("message", "unPut");
        }

        result.put("resultCode", true);
        return result;
    }


    //good
    @ApiOperation(value = "내 플레이리스트 조회", notes = "")
    @RequestMapping(value = "/music/play-category", method = RequestMethod.GET)
    public Map<String, Object> myPlayList(@RequestHeader String tokenHeader) {

        Map<String, Object> result = new HashMap<>();


        int user_id = jwtTokenProvider.getUserId(tokenHeader);

        List<PlayListCategoryTb> playListCategoryTbs = playListCategoryRepository.getPlayListCategoryTbsByUserNo(user_id);
        List<PlayListDto> playListDtos = new ArrayList<>();



        for (PlayListCategoryTb playListCategoryTb : playListCategoryTbs) {

            List<PlayListMusicTb> playListMusicTb = playListMusicRepository.getPlayListMusicTbsByCategoryNoAndUserNo(playListCategoryTb.getCategoryNo(), playListCategoryTb.getUserNo());
            List<MusicTb> musicTbList = new ArrayList<>();
            for (PlayListMusicTb musicLikeTb : playListMusicTb) {
                MusicTb musicTb = musicRepository.getReferenceById(musicLikeTb.getMusicNo());
                musicTbList.add(musicTb);
            }
            PlayListDto playListDto = new PlayListDto(playListCategoryTb, musicTbList);
            playListDtos.add(playListDto);

        }
        result.put("list", playListDtos);
        result.put("resultCode", true);
        return result;

    }

    //good
    @ApiOperation(value = "나의 플레이리스트 목록 추가", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "추가할 플레이스트 목록 이름", required = true),
    })
    @RequestMapping(value = "/music/play-category", method = RequestMethod.POST)
    public Map<String, Object> setPlayListCategory(@RequestHeader String tokenHeader, @RequestBody Map<String, Object> data) {

        Map<String, Object> result = new HashMap<>();

        try {

            int user_id = jwtTokenProvider.getUserId(tokenHeader);
            String category = data.get("category").toString();

            PlayListCategoryTb playListCategoryTb = new PlayListCategoryTb();
            playListCategoryTb.setCategory(category);
            playListCategoryTb.setUserNo(user_id);
            playListCategoryRepository.save(playListCategoryTb);
            result.put("resultCode", true);
            return result;

        } catch (Exception e) {
            log.info("{}", e);
            result.put("message", "db error");
            result.put("resultCode", false);
            return result;
        }
    }

    @ApiOperation(value = "나의 플레이리스트 목록 수정", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "변경될 플레이리스트 목록 이름", required = true),
    })
    @RequestMapping(value = "/music/play-category/{category_no}", method = RequestMethod.POST)
    public Map<String, Object> setPlayListCategory(@RequestHeader String tokenHeader, @PathVariable int category_no, @RequestBody Map<String, Object> data) {

        Map<String, Object> result = new HashMap<>();

        try {

            int user_id = jwtTokenProvider.getUserId(tokenHeader);
            String category = data.get("category").toString();

            PlayListCategoryTb playListCategoryTb = playListCategoryRepository.getPlayListCategoryTbByCategoryNo(category_no);
            playListCategoryTb.setCategory(category);
            playListCategoryRepository.save(playListCategoryTb);

            result.put("resultCode", true);
            return result;

        } catch (Exception e) {
            log.info("{}", e);
            result.put("message", "db error");
            result.put("resultCode", false);
            return result;
        }
    }

    //good
    @ApiOperation(value = "나의 플레이리스트 목록 삭제", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_no", value = "삭제할 플레이스트 목록 넘버", required = true),
    })
    @RequestMapping(value = "/music/play-category", method = RequestMethod.DELETE)
    public Map<String, Object> delPlayListCategory(@RequestHeader String tokenHeader, @RequestBody Map<String, Object> data) {

        Map<String, Object> result = new HashMap<>();

        try {

            int category_no = Integer.parseInt(data.get("category_no").toString());
            int user_id = jwtTokenProvider.getUserId(tokenHeader);

            if(!playListCategoryRepository.existsById(category_no)){
                result.put("message", "not exist");
                result.put("resultCode", false);
                return result;
            }

            PlayListCategoryTb playListCategoryTb = playListCategoryRepository.getReferenceById(category_no);
            if (playListCategoryTb.getUserNo() == user_id) {

                playListMusicRepository.deleteAll(playListMusicRepository.getPlayListMusicTbsByCategoryNoAndUserNo(category_no, user_id));
                playListCategoryRepository.delete(playListCategoryTb);
                result.put("resultCode", true);
                return result;

            } else {
                log.info("No Permission");
                result.put("resultCode", false);
                result.put("message", "No Permission");
                return result;
            }

        } catch (Exception e) {
            log.info("{}", e);
            result.put("message", "db error");
            result.put("resultCode", false);
            return result;
        }
    }

    //good
    @ApiOperation(value = "나의 플레이리스트에 음악 추가", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "music_no", value = "음악 추가할 플레이스트 목록의 넘버", required = true),
            @ApiImplicitParam(name = "category_no", value = "플레이리스트 카테고리 넘버", required = true),
    })
    @RequestMapping(value = "/music/play-music", method = RequestMethod.POST)
    public Map<String, Object> setPlayListMusic(@RequestHeader String tokenHeader, @RequestBody Map<String, Object> data) {

        Map<String, Object> result = new HashMap<>();

        try {

            int user_id = jwtTokenProvider.getUserId(tokenHeader);
            int category_no = Integer.parseInt(data.get("category_no").toString());
            int music_no = Integer.parseInt(data.get("music_no").toString());
            if (playListCategoryRepository.existsById(category_no)) {

                PlayListCategoryTb playListCategoryTb = playListCategoryRepository.getReferenceById(category_no);

                if (playListCategoryTb.getUserNo() == user_id) {
                    PlayListMusicTb playListMusicTb = new PlayListMusicTb();
                    playListMusicTb.setCategoryNo(category_no);
                    playListMusicTb.setMusicNo(music_no);
                    playListMusicTb.setUserNo(user_id);

                    if(playListMusicRepository.existsPlayListMusicTbByMusicNoAndCategoryNo(music_no, category_no)){
                        result.put("message", "already exist");
                        result.put("resultCode", false);
                        return result;
                    }else{
                        playListMusicRepository.save(playListMusicTb);
                        result.put("resultCode", true);
                        return result;
                    }


                } else {
                    log.info("No Permission");
                    result.put("message", "No Permission");
                    result.put("resultCode", false);
                    return result;
                }

            } else {
                log.info("not exist category");
                result.put("message", "not exist category");
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



    //good
    @ApiOperation(value = "나의 플레이리스트에 있는 음악 삭제", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "music_no", value = "삭제할 음악 넘버", required = true),
            @ApiImplicitParam(name = "category_no", value = "어느 카테고리에 있는 음악을 삭제할지", required = true),
    })
    @RequestMapping(value = "/music/play-music", method = RequestMethod.DELETE)
    public Map<String, Object> delPlayListMusic(@RequestHeader String tokenHeader, @RequestBody Map<String, Object> data) {

        Map<String, Object> result = new HashMap<>();

        try {

            int user_id = jwtTokenProvider.getUserId(tokenHeader);
            int category_no = Integer.parseInt(data.get("category_no").toString());
            int music_no = Integer.parseInt(data.get("music_no").toString());

            if (playListCategoryRepository.existsById(category_no)) {

                PlayListCategoryTb playListCategoryTb = playListCategoryRepository.getReferenceById(category_no);
                if (playListCategoryTb.getUserNo() == user_id) {
                    if(playListMusicRepository.existsPlayListMusicTbByMusicNoAndCategoryNo(music_no, category_no)){
                        playListMusicRepository.removePlayListMusicTbByMusicNoAndCategoryNo(music_no, category_no);
                        result.put("resultCode", true);
                        return result;
                    }else{
                        result.put("message", "not exist");
                        result.put("resultCode", false);
                        return result;
                    }


                }else{
                    result.put("message", "No Permission");
                    result.put("resultCode", false);
                    return result;
                }

            } else {
                log.info("not exist category");
                result.put("message", "not exist category");
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

    @ApiOperation(value = "기본 컬렉션 음악 불러오기", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "emotion_type", value = "어떤 감정에 있는 음악을 불러올 것인지(빈값도 가능)", required = false),
            @ApiImplicitParam(name = "situation_type", value = "어떤 상황에 있는 음악을 불러올 것인지(빈값도 가능)", required = false),
    })
    @RequestMapping(value = "/music/collection", method = RequestMethod.GET)
    public Map<String, Object> getCollectionMusic(@RequestParam String emotion_type, @RequestParam String situation_type) {

        Map<String, Object> result = new HashMap<>();

        try {

            List<MusicTb> musicTbList = musicRepository.findByEmotionContainsAndSituationContains(emotion_type, situation_type);
            result.put("list", musicTbList);
            result.put("resultCode", true);
            return result;

        } catch (Exception e) {
            log.info("{}", e);
            result.put("message", "db error");
            result.put("resultCode", false);
            return result;
        }
    }


}
