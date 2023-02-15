package com.example.testjava.study;

import com.example.testjava.domain.Member;
import com.example.testjava.domain.Study;
import com.example.testjava.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

  /**
   * @Mock 어노테이션만 사용한다고 Mock 객체를 생성하지는 않는다.
   * 어노테이션을 처리해줄 확장을 등록 해야된다. > @ExtendWith(MockitoExtension.class)
   */
  @Mock
  MemberService memberService;

  @Mock
  StudyRepository studyRepository;

  @DisplayName("Mock 객체 생성")
  @Test
  void createStudyService(@Mock MemberService memberService,
                          @Mock StudyRepository studyRepository) {

//    MemberService memberService = mock(MemberService.class);
    // 인터페이스나 클래스 타입을 매개변수로 넘겨주면 Mock 객체를 생성
//    StudyRepository studyRepository = Mockito.mock(StudyRepository.class);

    StudyService studyService = new StudyService(memberService, studyRepository);
    assertNotNull(studyService);

  }

  @DisplayName("Mock 객체 Stubbing 테스트")
  @Test
  void createNewStudy(@Mock MemberService memberService,
                      @Mock StudyRepository studyRepository) {

    StudyService studyService = new StudyService(memberService, studyRepository);
    assertNotNull(studyService);

    /**
     * createNewStudy() 메소드를 호출할떄,
     * memberservice와 studyRepository가 어떻게 동작해야되는지. 가정하여 알려주어여 한다.
     * e.g. memberservice가 이렇게 동작할 것이다. studyRepository가 이렇게 동작할 것이다.
     */

    Member member = new Member();
    member.setId(1L);
    member.setEmail("wotjr7386@gmail.com");

    /**
     * when() : memberService에서 findById() 메소드에 매개변수로 1L을 넘기고 호출하면
     * thenReturn() : 그러면 Optional.of(member)를 리턴이 되도록 정의
     *
     * findById(1L)의 파라미터가 중요하다. 다르게 되면 아래 테스트 오류
     * 위와 같은 파라미터를 파라미터 매처라고한다.
     */
//    when(memberService.findById(1L)).thenReturn(Optional.of(member));

    // 어떤 memberId로 호출하든 결과는 항상 같은 Optional.of(member)로 조회된다.
    when(memberService.findById(any())).thenReturn(Optional.of(member));

    Study study = new Study(10, "java");

    /**
     * 실행하면 메소드 내의 Optional<Member> member = memberService.findById(memberId);
     * 반환되는 Member는 thenReturn(Optional.of(member))이 된다.
     */
    Optional<Member> findById = memberService.findById(1L);
    assertEquals(member.getEmail(), findById.get().getEmail());
    assertEquals(member.getEmail(), memberService.findById(3L).get().getEmail());

    // 1L로 조회를 하면 런타임 예외를 던지도록 Stubbing
    when(memberService.findById(1L)).thenThrow(new RuntimeException());
    assertThrows(RuntimeException.class, () -> memberService.findById(1L));

    // 1L로 memberService의 validate를 호출하면 IllegalArgumentException를 던짐.
    doThrow(new IllegalArgumentException()).when(memberService).validate(1L);
    assertThrows(IllegalArgumentException.class, () -> memberService.validate(1L));

    memberService.validate(2L);
  }

  @DisplayName("메소드가 동일한 매개변수로 여러번 호출될 때 각기 다른 stubbing을 조작할 수 있다.")
  @Test
  void createNewStudy2() {

    StudyService studyService = new StudyService(memberService, studyRepository);
    assertNotNull(studyService);

    Member member = new Member();
    member.setId(1L);
    member.setEmail("wotjr7386@gmail.com");

    when(memberService.findById(any()))
            .thenReturn(Optional.of(member))
            .thenThrow(new RuntimeException())
            .thenReturn(Optional.empty());

    Optional<Member> findMember = memberService.findById(1L);
    assertEquals(member.getEmail(), findMember.get().getEmail());
    assertThrows(RuntimeException.class, () -> memberService.findById(2L));
    assertEquals(Optional.empty(), memberService.findById(3L));
  }

  @DisplayName("stubbing 연습문제")
  @Test
  void stubbingPractice() {

    StudyService studyService = new StudyService(memberService, studyRepository);

    Study study = new Study(10, "테스트");

    Member member = new Member();
    member.setId(1L);
    member.setEmail("wotjr7386@gmail.com");

    // TODO memberService 객체에 findById 메소드를 1L 값으로 호출하면 member 객체를 리턴하도록 Stubbing
    when(memberService.findById(1L)).thenReturn(Optional.of(member));
    // TODO studyRepository 객체에 save 메소드를 study 객체로 호출하면 study 객체를 리턴하도록 Stubbing
    when(studyRepository.save(study)).thenReturn(study);

    studyService.createNewStudy(1L, study);
    assertEquals(member, study.getOwner());
  }

  @DisplayName("Verify - 어떤일이 일어났는지 확인")
  @Test
  void verified() {

    StudyService studyService = new StudyService(memberService, studyRepository);
    assertNotNull(studyService);

    Member member = new Member();
    member.setId(1L);
    member.setEmail("wotjr7386@gmail.com");

    Study study = new Study(10, "테스트");

    when(memberService.findById(1L)).thenReturn(Optional.of(member));
    when(studyRepository.save(study)).thenReturn(study);

    studyService.createNewStudy(1L, study);

    // memberService Mock객체에 딱 한번(imes(1))만 study 매개 변수를 가진 notify() 호출 되어야 한다.
    verify(memberService, times(1)).notify(study);

    // 특정 시점이후 해당 목에 어떠한 액션도 일어나면 안된다.
//    verifyNoMoreInteractions(memberService);

    verify(memberService, times(1)).notify(member);
//    Mockito.verify(memberService, times(1)).notify(any()); 지금 해당 메소드는 오버리딩 상태인데 any() 넣으니 어떤 타입인지 추론을 못하므로 컴파일 오류
    verify(memberService, never()).validate(any());

    /**
     * 호출 순서 검증
     */
    InOrder inOrder = inOrder(memberService);
    inOrder.verify(memberService).notify(study);
    inOrder.verify(memberService).notify(member);

  }

  @DisplayName("BDD 스타일 Mockito API")
  @Test
  void bddStyle() {
    // Given
    StudyService studyService = new StudyService(memberService, studyRepository);
    assertNotNull(studyService);

    Member member = new Member();
    member.setId(1L);
    member.setEmail("wotjr7386@gmail.com");

    Study study = new Study(10, "테스트");

    given(memberService.findById(1L)).willReturn(Optional.of(member));
    given(studyRepository.save(study)).willReturn(study);

    // When
    studyService.createNewStudy(1L, study);

    // Then
    then(memberService).should(times(1)).notify(study);
    then(memberService).shouldHaveNoInteractions();

  }


}