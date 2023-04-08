package io.howstheairtoday.appcommunityexternalapi.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/post")
public class SampleController {

    @GetMapping("/hello")
    public List<String> member() {
        return Arrays.asList("게시글", "제목", "내용");
    }
}
