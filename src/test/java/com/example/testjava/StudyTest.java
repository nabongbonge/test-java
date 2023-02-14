package com.example.testjava;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {
  @DisplayName("assertAll 테스트")
  @Test
  void create_new_study() {
    Study study = new Study(0);
    assertNotNull(study);

    /**
     * 연관 테스트를 한번에 실행
     */
    assertAll(
            //    assertEquals(StudyStatus.DRAFT, study.getStatus(), "스터디를 처음 만들면 상태값이 DRAFT여야 한다.");
            /**
             * 문자열 연산을 처리해야 되는경우, 람다식을 통해 처리하면 실제로 호출때면 연산이 수행되기 떄문에 성능상의 이점을 가짐
             */
            () -> assertEquals(StudyStatus.DRAFT, study.getStatus(),
                    () -> "스터디를 처음 만들면 " + StudyStatus.DRAFT + "상태다."),
            () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야 한다.")
    );


  }


  @DisplayName("assertThrows 테스트")
  @Test
  void create_new_study1() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Study(-1));
    assertEquals("limit은 0보다 커야 한다.", exception.getMessage());
  }

  @DisplayName("assertTimeout 테스트")
  @Test
  void create_new_study2() {
    // 테스트가 실행되는 시간이 100ms 이내인지 확인하고 단, 테스트가 종료될때까지 실행된다.
    assertTimeout(Duration.ofMillis(100), () -> {
      new Study(10);
      Thread.sleep(1000);
    });
  }

  /**
   * 스프링 트랜잭션은 기본적으로 ThreadLocal을 사용하고 테스트에서 롤백이 기본이다.
   * 하지만, assertTimeoutPreemptively 같은 경우 내부에 별도 쓰레드로 처리되기 떄문에 트랜잭션이 제대로 동작하지 않을 수 있다.
   * 롤백이 아닌 커밋이 될 수 있기에 주의하여 사용하자.
   * 가능하다면 assertTimeout를 사용하자.
   */
  @DisplayName("assertTimeoutPreemptively 테스트")
  @Test
  void create_new_study3() {
    // 테스트가 실행되는 시간이 100ms 이내인지 확인하고 단, 테스트의 수행시간이 설정한 타임아웃 시간이 지나는 순간 종료된다.
    assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
      new Study(10);
      Thread.sleep(1000);
    });
  }

  @DisplayName("assume 테스트")
  @Test
  void create_new_study4() {
    String env = System.getenv("TEST_ENV");
    System.out.println("env = " + env);
    assumeTrue("LOCAL".equals(env)); // true인 경우만 다음 코드를 실행한다.

    assumingThat("LOCAL".equalsIgnoreCase(env), () -> {
      System.out.println(":::::");
      Study actual = new Study(100);
      assertThat(actual.getLimit()).isGreaterThan(0);
    });
  }

  @DisplayName("@EnabledOnOs 테스트")
  @Test
  @EnabledOnOs(OS.MAC)
  void create_new_study5() {
    System.out.println("@EnabledOnOs(OS.MAC)");
  }

  @DisplayName("@DisabledOnOs 테스트")
  @Test
  @DisabledOnOs({OS.MAC, OS.LINUX})
  void create_new_study6() {
    System.out.println("@DisabledOnOs(OS.MAC)");
  }

  @DisplayName("@DisabledOnJre 테스트")
  @Test
  @DisabledOnJre(JRE.JAVA_17)
  void create_new_study7() {
    System.out.println("@DisabledOnJre(JRE.JAVA_17)");
  }

  @DisplayName("@EnabledOnJre 테스트")
  @Test
  @EnabledOnJre(JRE.JAVA_17)
  void create_new_study8() {
    System.out.println("@EnabledOnJre(JRE.JAVA_17)");
  }

  @DisplayName("@EnabledIfEnvironmentVariable 테스트")
  @Test
  @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")
  void create_new_study9() {
    System.out.println("@EnabledIfEnvironmentVariable(named = \"TEST_ENV\", matches = \"LOCAL\")");
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
