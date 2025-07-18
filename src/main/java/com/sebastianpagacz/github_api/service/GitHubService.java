package com.sebastianpagacz.github_api.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GitHubService{

    private final WebClient webClient = WebClient.create("https://api.github.com");

    public record Owner(String login) {}
    public record Repository(String name, boolean fork, Owner owner, Branch branch) {}
    public record Branch(String name, Commit commit) {}
    public record Commit(String sha) {}
    public record BranchDto(String name, String lastCommitSha) {}
    public record FinalRespone(String repoName, String ownerLogin, List<BranchDto> branches) {}

    public List<FinalRespone> GetRepos(String username){
        List<Repository> response = webClient.get()
            .uri("/users/{username}/repos", username)
            .retrieve()
            .bodyToFlux(Repository.class)
            .collectList()
            .block();

            return response.stream()
            .filter(repo -> !repo.fork())
            .map(repo -> {
                List<Branch> branches = webClient.get()
                .uri("https://api.github.com/repos/{owner}/{repo}/branches", repo.owner.login, repo.name)
                .retrieve()
                .bodyToFlux(Branch.class)
                .collectList()
                .block();

                List<BranchDto> branchesDto = branches == null ? List.of() : 
                    branches.stream()
                    .map(b -> new BranchDto(b.name(), b.commit().sha()))
                    .toList();

                return new FinalRespone(repo.name(), repo.owner().login(), branchesDto);
            })
            .toList();
    }
}