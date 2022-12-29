# 자바 ORM 표준 JPA 프로그래밍 - 기본편

## 강의 목표

- 객체와 테이블 설계 매핑
- JPA 내부 동작 방식 이해 (언제 어떤 SQL을 만들어서 실행하는지?

<br>

### 객체 ↔ 관계형 데이터베이스(RDBMS)의 차이

1. 상속
2. 연관 관계
3. 데이터타입
4. 데이터 식별 방법

<br>

### JPA  동작

- 애플리케이션 ↔ JDBC 사이에서 동작

<img src="images/1.png" width="80%">


<br>

#### JPA 인터페이스의 구현체 중 하나가 Hibernate (Hibernate, EclipseLink, DataNucleus)

<br>

### JPA - CRUD

- 저장 : `jpa.persist(member)`
- 조회 : `Member member = jpa.find(memberId)`
- 수정 (변경 감지) : `member.setName(”변경할 이름”)`
- 삭제 : `jpa.remove(member)`

<br>

### JPA와 패러다임 불일치 해결

1. 상속
  - `persist()`, `find()` 등을 하면 JPA가 알아서 상속 관계 데이터 값들을 넣고 빼줌
2. 연관 관계
3. 객체 그래프 탐색
4. 비교하기
   - 동일한 트랜잭션에서 동일한 식별자로 조회한 엔티티는 항상 같음을 보장

    ```java
    String memberId = "100";
    Member member1 = jpa.find(Member.class, memberId);
    Member member2 = jpa.find(Member.class, memberId);
    
    // True
    member1 == member2;
    ```


<br>

### JPA의 성능 최적화 기능

1. 1차 캐시와 동일성(identity) 보장
  - DB 격리 수준(Isolation Level)이 Read Commit(커밋된 읽기)이어도
    애플리케이션에서 Repeatable Read(반복 가능한 읽기) 보장
2. 트랜잭션을 지원하는 쓰기 지연
  - 트랜잭션 커밋시점까지 SQL을 보내지 않고 쌓아둠
  - 트랜잭션이 종료되어 commit()이 될 때 flush()를 통해 SQL이 한번에 나감
  - **수정, 삭제**할 때도 트랜잭션 커밋시점까지 쿼리를 묶어놓음으로써, 비즈니스 로직 수행동안 DB 로우 락이 걸리지 않는 효과
3. 지연 로딩 (Lazy Loading)
  - 지연 로딩: 객체가 실제 사용될 때 로딩
  - 즉시 로딩: JOIN SQL로 한번에 연관된 객체까지 미리 조회

  <img src="images/2.png" width="80%">

<br>

### 정리

- ORM 프레임워크는 단순히 SQL을 개발자 대신 생성하여 DB에 전달해주는 것 뿐만 아니라, 다양한 패러다임의 불일치 문제들도 해결해준다
  - 상속
    - SQL로 하면 join을 통해 각자 가져와서 객체에 세팅해줘야함
    - 상속 객체는 인스턴스 생성 시 부모것도 자동으로 사용 가능
      - JPA가 `persist()`, `find()`같은 명령어 한 줄로 다 해결해줌
  - 연관 관계
    - 참조 객체 ↔ 외래키(FK)
  - 객체 그래프 탐색
    - SQL 쿼리에 따라서 객체 그래프의 탐색 가능범위가 결정된다
    - 따라서 JPA의 지연로딩, 즉시로딩을 통해 원하는 객체로 타고타고타고 가서 조회하자
  - 비교 : 동일성(==), 동등성(`equals()`)

- 성능적인 관점도 이점을 챙길 수 있다
  - JPA는 애플리케이션과 DB 사이에 계층 하나를 두고 동작하므로 최적화 관점에서 시도해 볼 수 있는 것들이 많다
    - 식별자가 같은 엔티티 조회 → 1차 캐시

- 데이터 접근 추상화
  - 각 DB마다 같은 기능이여도 사용법이 다른 경우가 많다
  - JPA를 통해 특정 데이터베이스 기술에 종속되지 않도록 할 수 있다

    <img src="images/3.png" width="80%">


<br>

#### 모든 JPA의 데이터 변경 작업은 Transaction 안에서 이루어져야 한다

#### 모든 관계형 데이터베이스는 내부적으로 트랜잭션에 감싸져서 데이터 변경이 일어난다
  - RDB는 내부적으로 데이터 변경은 트랜잭션 안에서 일어나도록 설계되어 있다 

---
