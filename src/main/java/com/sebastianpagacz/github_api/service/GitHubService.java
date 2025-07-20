package com.sebastianpagacz.github_api.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.sebastianpagacz.github_api.exceptions.OwnerNotFoundException;

@Service
public class GitHubService{

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "https://api.github.com";

    public record Owner(String login) {}
    public record Repository(String name, boolean fork, Owner owner, Branch branch) {}
    public record Branch(String name, Commit commit) {}
    public record Commit(String sha) {}
    public record BranchDto(String name, String lastCommitSha) {}
    public record FinalResponse(String repoName, String ownerLogin, List<BranchDto> branches) {}

    public List<FinalResponse> GetRepos(String username){
        String url = baseUrl + "/users/" + username + "/repos";

        ResponseEntity<Repository[]> resposne = restTemplate.getForEntity(url, Repository[].class);

        if(resposne.getStatusCode() == HttpStatus.NOT_FOUND){
            throw new OwnerNotFoundException("User '$s' was not found".formatted(username));
        }

        Repository[] repos = resposne.getBody();

        return Arrays.stream(repos)
            .filter(repo -> !repo.fork())
            .map(repo -> {
                String branchesUrl = baseUrl + "/repos/" + repo.owner().login() + "/" + repo.name() + "/branches";
                Branch[] branches = restTemplate.getForObject(branchesUrl, Branch[].class);

                List<BranchDto> branchDtos = branches == null ? List.of() :
                    Arrays.stream(branches)
                        .map(b -> new BranchDto(b.name(), b.commit().sha()))
                        .toList();

                return new FinalResponse(repo.name(), repo.owner().login(), branchDtos);
            })
            .toList();
    }
}