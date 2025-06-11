# 🧠 Gitmago – 깃허브 기반 개발 활동 시각화 및 팀 프로젝트 협업

**Gitmago**는 소프트웨어마이스터고 연합 개발 커뮤니티 프로젝트로,  
GitHub 활동 기반 커밋 수, PR, Issue 등의 데이터를 수집하여 학생 개발자의 성장을 시각화하고 칭호를 부여하고,
팀원 모집, 팀 모집과 코드리뷰등의 기능들이 있습니다.



## 📌 프로젝트 목표

- **학생 개발자들의 지속적인 성장 유도**  
- **GitHub 활동 기반 커밋/이슈/PR 수집 및 칭호 발급**  
- **OAuth 연동으로 실시간 GitHub 데이터 동기화**  
- **자기계발형 포트폴리오 자동화 플랫폼으로 확장 가능**



## ⚙️ 기술 스택

| 분류 | 기술 |
|------|------|
| 언어 | Java 21 |
| 프레임워크 | Spring Boot 3 |
| 빌드 도구 | Gradle |
| DB | MongoDB (Atlas) |
| 인증 | GitHub OAuth + JWT |
| 배포 | AWS EC2, Docker |
| 문서화 | Swagger |
| 테스트 | JUnit 5 |
| 기타 | RestTemplate, Jackson, Lombok |


## 🔐 인증 흐름

1. 사용자는 GitHub OAuth로 로그인합니다.
2. `GithubService`에서 GitHub Access Token 및 사용자 정보를 저장합니다.
3. Access Token은 MongoDB에 저장되어 GraphQL을 통해 사용자의 커밋 수, PR, Issue 등 GitHub 활동 데이터를 가져오는 데 사용됩니다.
4. 커밋 수를 기반으로 `TitleCommitService`에서 자동으로 칭호를 부여합니다.





## 🧭 전체 구조
User ➝ GitHub OAuth ➝ GitHub API ➝ Gitmago 서버
➝ MongoDB 저장
➝ 커밋 수 계산 및 칭호 자동 부여






## ✅ 커밋 컨벤션
makefile
복사
편집
feat: 기능 구현
fix: 버그 수정
docs: 문서 수정
style: 포맷/세미콜론 등 스타일 수정
refactor: 리팩토링 (기능 변화 없음)
test: 테스트 코드 추가
chore: 빌드 설정, 패키지 변경 등
