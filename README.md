# Book Curation Service
알라딘 api연동을 통한 책 소개

## 프로젝트 소개
- 사용자가 처음 책을 접할때, 제목이 아닌 책의 요약된 내용으로 접할 수 있도록 ui를 상하좌우 슬라이드 형태로 구성했습니다.

---

## Stacks 🐈

### Environment
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white)
![Github](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white)

### Development
![JAVA](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white)
![SPRINGBOOT](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MYSQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![HTML](https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS](https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JAVASCRIPT](https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![JPA](https://img.shields.io/badge/jpa-purple?style=for-the-badge&logo=jpa&logoColor=purple)

---

## 주요 기능 📦

### ⭐️ 상하좌우 슬라이드 기능
- 좌우 슬라이드시 책이 변경됩니다.
- gpt api연동하여 책 속의 명대사를 가져옵니다.
- 상하 슬라이드시 같은 책 안에 책소개, 책내용 중 일부, gpt 추천내용, 추천사의 내용이 무작위로 노출됩니다. (Future 인터페이스를 활용하여 조회속도를 10초 -> 4초로 줄였습니다.)

### ⭐️ 로그인, 회원가입 기능
- 회원가입은 간단하게 사용자 로그인 아이디, 비밀번호 정보만으로 가입 가능.
- 세션 방식으로 사용자 정보 관리.
- bcrypt방식을 적용하여 비밀번호 보관.

### ⭐️ 봤던 책 이력 저장
- 슬라이드를 통해 오늘 본 책은 다음날 보이지 않도록 구현하였습니다.