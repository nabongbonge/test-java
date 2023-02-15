package com.example.testjava.study;

import com.example.testjava.domain.Member;
import com.example.testjava.domain.Study;
import com.example.testjava.member.MemberService;

import java.util.Optional;

public class StudyService {

  private final MemberService memberService;
  private final StudyRepository repository;

  public StudyService(MemberService memberService, StudyRepository repository) {
    assert memberService != null; // java.lang.AssertionError at com.example.testjava.study.StudyService.<init>(StudyService.java:15)
    assert repository != null;
    this.memberService = memberService;
    this.repository = repository;
  }

  public Study createNewStudy(Long memberId, Study study) {
    Optional<Member> member = memberService.findById(memberId);
    study.setOwner(member.orElseThrow(() -> new IllegalArgumentException("Member dosen`t exist for id : '" + memberId + "'")));
    Study newStudy = repository.save(study);
    memberService.notify(newStudy);
    memberService.notify(member.get());
    return newStudy;
  }
}
