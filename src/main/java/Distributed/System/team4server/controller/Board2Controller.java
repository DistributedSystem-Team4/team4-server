package Distributed.System.team4server.controller;

import Distributed.System.team4server.utils.SecurityUtil;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
public class Board2Controller {

    private static final Logger log = LoggerFactory.getLogger(Log.class);

    @PostMapping("/write")
    public ResponseEntity<String> writeReview(Neo4jProperties.Authentication authentication) {
        String id = SecurityUtil.getCurrentMemberId();
//        return ResponseEntity.ok().bo///dy(authentication.getUsername() + "님의 리뷰 등록이 완료되었습니다.");
        return ResponseEntity.ok().body(id + "님의 리뷰 등록이 완료되었습니다.");
    }
}
