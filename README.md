# GitHub API

GitHub API is a Spring Boot application that retrieves a list of public GitHub repositories for a given user and returns the names of non-fork repositories along with their branches and last commit SHAs.

## Tech Stack
- Java 21
- Spring Boot 3.5
- Maven
- REST API
- JUnit 5

## Installation

1. Requires JDK 21+
2. Clone repository

```bash
git clone https://github.com/SebastianPagacz/github-api
cd github-api
```

## Usage
> To Run the application:
``` bash
./mvnw spring-boot:run
```
> Access the API at:
```bash
http://localhost:8080/repositories/{username}
```

> **Note:** This app uses unauthenticated GitHub API calls, which are rate-limited to 60 requests per hour per IP.

## Request Results
```json
[
  {
    "repoName": "git-consortium",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "master",
        "lastCommitSha": "b33a9c7c02ad93f621fa38f0e9fc9e867e12fa0e"
      }
    ]
  }
]
```
