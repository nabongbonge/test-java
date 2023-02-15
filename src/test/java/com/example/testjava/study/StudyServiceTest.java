package com.example.testjava.study;

import com.example.testjava.domain.Member;
import com.example.testjava.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

  /**
   * @Mock 어노테이션만 사용한다고 Mock 객체를 생성하지는 않는다.
   * 어노테이션을 처리해줄 확장을 등록 해야된다. > @ExtendWith(MockitoExtension.class)
   */
//  @Mock
//  MemberService memberService;

//  @Mock
//  StudyRepository studyRepository;


  @Test
  void createStudyService(@Mock MemberService memberService,
                          @Mock StudyRepository studyRepository) {

    /**
     * createNewStudy() 메소드를 호출할떄,
     * memberservice와 studyRepository가 어떻게 동작해야되는지. 가정하여 알려주어여 한다.
     * e.g. memberservice가 이렇게 동작할 것이다. studyRepository가 이렇게 동작할 것이다.
     */
    Optional<Member> optional = memberService.findById(1L);


//    MemberService memberService = mock(MemberService.class);
    // 인터페이스나 클래스 타입을 매개변수로 넘겨주면 Mock 객체를 생성
//    StudyRepository studyRepository = Mockito.mock(StudyRepository.class);

    StudyService studyService = new StudyService(memberService, studyRepository);
    assertNotNull(studyService);

  }

}