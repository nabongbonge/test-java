package com.example.testjava;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

  @Test
  @DisplayName("스터디 만들기")
  void create_new_study() {
    Study study = new Study();
    assertNotNull(study);
    System.out.println("create");
  }

  @Test
  void create_new_study1() {
    System.out.println("create1");
  }

  /**
   * 모든 테스트 호출되기 전 딱 한번만 호출
   * 반드시 접근제어자는 private 사용X
   * 반드시 static 선언
   * 반드시 리턴 타입은 void 사용
   */
  @BeforeAll
  static void beforeAll() {
    System.out.println("before all");
  }

  @Disabled // 테스트 제외
  @AfterAll
  static void afterAll() {
    System.out.println("after all");
  }

  /**
   * 각 테스트 호출되기 전에 호출
   */
  @BeforeEach
  void beforeEach() {
    System.out.println("before each");
  }

  @AfterEach
  void afterEach() {
    System.out.println("after each");
  }

}
