package io.howstheairtoday.memberappexternalapi.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
public class SampleController {

    @GetMapping("/hello")
    public List<String> member() {
        return Arrays.asList("회원아이디", "닉네임", "토큰");
    }
}
