package com.sebastianpagacz.github_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.sebastianpagacz.github_api.service.GitHubService.BranchDto;
import com.sebastianpagacz.github_api.service.GitHubService.FinalResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GithubApiApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private static RestTemplate restTemplate;

	public GithubApiApplicationTests() {
		restTemplate = new RestTemplate();
	}

	@Test
	public void getReturnsNonForkReposAndBranchesForExisitngUser(){
		// Given
		String username = "octocat";
		String baseUrl = "http://localhost:" + port + "/repositories/" + username;
		
		// When
		ResponseEntity<FinalResponse[]> responseEntity = restTemplate.getForEntity(baseUrl, FinalResponse[].class);
		FinalResponse[] repos = responseEntity.getBody();

		// Then
		assertNotNull(repos);
		assertTrue(repos.length > 0, "Should return at least one repository");

		for (FinalResponse r : repos){
			assertNotNull(r.repoName());
			assertNotNull(r.ownerLogin());
			assertEquals(username, r.ownerLogin());

		List<BranchDto> branches = r.branches();
		assertNotNull(branches);
		assertTrue(branches.size() > 0, "Repository must contain at least one branch");

		for(BranchDto b : branches){
			assertNotNull(b);
			assertNotNull(b.name());
			assertNotNull(b.lastCommitSha());
		}
		}
	}
}
