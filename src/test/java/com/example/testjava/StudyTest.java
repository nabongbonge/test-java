package com.example.testjava;

import org.assertj.core.util.Strings;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {
  @DisplayName("assertAll 테스트")
  @Test
  @Tag("fast")
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
  @Tag("fast")
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

  @DisplayName("@RepeatedTest 테스트")
  @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}") // @RepeatedTest 테스트, 1/10
  void create_new_study10(RepetitionInfo repetitionInfo) {
    System.out.println("test" + repetitionInfo.getCurrentRepetition() + "/"
            + repetitionInfo.getTotalRepetitions());
  }

  @DisplayName("@ParameterizedTest, @ValueSource 테스트")
  @ParameterizedTest(name = "{index} {displayName} str={0}") // 1 @ParameterizedTest 테스트 str=AAA
  @ValueSource(strings = {"AAA", "BBB", "CCC"})
  void create_new_study11(String str) {
    System.out.println(str);
  }

  @DisplayName("@ParameterizedTest, 다양한 Source 테스트")
  @ParameterizedTest
  @ValueSource(strings = {"AAA", "BBB", "CCC"})
  @EmptySource
  @NullSource
//  @NullAndEmptySource
  void create_new_study12(String str) {
    System.out.println(str);
  }

  @DisplayName("@ParameterizedTest, @ConvertWith 테스트")
  @ParameterizedTest
  @ValueSource(ints = {10, 20, 30})
  void create_new_study13(@ConvertWith(StudyConverter.class) Study study) {
    System.out.println(study.getLimit());
  }

  @DisplayName("@ParameterizedTest, @CsvSource 테스트")
  @ParameterizedTest
  @CsvSource({"10, '자바 스터디'", "20, 스프링"}) // ','를 딜리미터로 사용하는 어노테이션
  void create_new_study14(Integer limit, String name) {
    System.out.println(new Study(limit, name));
  }

  @DisplayName("@ParameterizedTest, @CsvSource, 기본 ArgumentsAccessor 테스트")
  @ParameterizedTest
  @CsvSource({"10, '자바 스터디'", "20, 스프링"})
  void create_new_study15(ArgumentsAccessor aa) {
    Study study = new Study(aa.getInteger(0), aa.getString(1));
    System.out.println(study);
  }

  @DisplayName("@ParameterizedTest, @CsvSource, 커스텀 ArgumentsAccessor 테스트")
  @ParameterizedTest
  @CsvSource({"10, '자바 스터디'", "20, 스프링"})
  void create_new_study16(@AggregateWith(StudyAggregator.class) Study study) {
    System.out.println(study);
  }

  /**
   * 반드시 static inner class 혹은 기본 public class 형태로 구성
   */
  static class StudyAggregator implements ArgumentsAggregator {

    @Override
    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
      return new Study(accessor.getInteger(0), accessor.getString(1));
    }
  }


  static class StudyConverter extends SimpleArgumentConverter {
    @Override
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
      assertEquals(Study.class, targetType, "Can only to Study");
      return new Study(Integer.parseInt(source.toString()));
    }
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
