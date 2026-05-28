---
name: github
description: GitHub Issue 또는 PR을 생성한다. Issue는 요구사항 명세서, PR은 스펙이다.
argument-hint: create-issue | create-pr
arguments: [ action ]
allowed-tools: Bash
disable-model-invocation: true
---

`$action`에 따라 아래 작업을 수행한다.

---

## create-issue

현재 브랜치와 작업 내용을 파악하여 GitHub Issue를 생성한다.

Issue는 **요구사항 명세서**다. 무엇을, 왜 해야 하는지를 기술한다. 구현 방법은 포함하지 않는다.

- `gh issue list`로 현재 브랜치에 연결된 Issue가 이미 있으면 중단하고 사용자에게 알린다.
- 브랜치의 커밋들을 바탕으로 제목과 본문을 작성한다.
- `gh issue create`로 Issue를 생성하고 URL을 출력한다.

제목 형식: `<제목>`  
본문 형식:

```
## 배경
<!-- 이 작업이 필요한 이유와 맥락 -->

## 요구사항
- [ ] 

## 완료 조건
<!-- 이 Issue가 완료되었다고 판단하는 기준 -->
```

---

## create-pr

현재 브랜치의 변경 사항을 파악하여 GitHub PR을 생성한다.

PR은 **스펙**이다. 무엇을 어떻게 구현했는지를 기술한다. Issue의 요구사항 중 이 PR에서 다루는 범위를 명확히 한다.

- `gh pr list`로 현재 브랜치에 열린 PR이 이미 있으면 중단하고 사용자에게 알린다.
- `git log develop..HEAD`와 변경된 파일을 확인하고 제목과 본문을 작성한다.
- 브랜치에 연결된 Issue 번호가 있으면 본문에 `Closes #<번호>`를 포함한다.
- **항상 `--base develop`으로 PR을 생성한다. `main`을 base로 사용하지 않는다.**
- `gh pr create --base develop`으로 PR을 생성하고 URL을 출력한다.

제목 형식: `<제목>`  
본문 형식:

```
## 구현 내용
<!-- 무엇을 어떻게 구현했는지 -->

## 주요 변경 사항
- 

## 검증 방법
- [ ] 

Closes #<issue-number>
```
