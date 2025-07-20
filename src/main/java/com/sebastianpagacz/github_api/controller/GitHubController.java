package com.sebastianpagacz.github_api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sebastianpagacz.github_api.service.GitHubService;
import com.sebastianpagacz.github_api.service.GitHubService.FinalResponse;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class GitHubController {

    private final GitHubService _gitHubService;

    public GitHubController(GitHubService gitHubService) {
        _gitHubService = gitHubService;
    }

    @GetMapping("/repositories/{username}")
    public List<FinalResponse> getMethodName(@PathVariable String username) {
        return _gitHubService.GetRepos(username);
    }
    
}
