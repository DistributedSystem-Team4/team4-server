package Distributed.System.team4server.dto;

import Distributed.System.team4server.config.jwt.JwtFilter;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class DefaultResponseDto {

    private HttpStatus status;
    private Object result;

    //기본
    public DefaultResponseDto() {
        this.status = HttpStatus.BAD_REQUEST;
        this.result = null;
    }

    //status
    public static ResponseEntity<DefaultResponseDto> result(HttpStatus status, Object result) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");

        DefaultResponseDto response = new DefaultResponseDto();
        response.setStatus(status);
        response.setResult(result);

        return new ResponseEntity<>(response, headers, status);
    }

    //총 게시물 수도 같이 반환하는 dto
    public static ResponseEntity<DefaultResponseDto> result(HttpStatus status, Object result, long totalBoard) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
        headers.add("Totalboard", String.valueOf(totalBoard));

        DefaultResponseDto response = new DefaultResponseDto();
        response.setStatus(status);
        response.setResult(result);

        return new ResponseEntity<>(response, headers, status);
    }

    //status, jwt (로그인 시 토큰 발급용)
    public static ResponseEntity<DefaultResponseDto> result(HttpStatus status, Object result, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
        headers.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token);

        DefaultResponseDto response = new DefaultResponseDto();
        response.setStatus(status);
        response.setResult(result);

        return new ResponseEntity<>(response, headers, status);
    }

    //status, result=게시글 생성 완료

    //status, result=게시글정보

    //status, result=게시글 배열
}
