package com.example.testjava.member;

import com.example.testjava.domain.Member;
import com.example.testjava.domain.Study;

import java.util.Optional;

public interface MemberService {

  Optional<Member> findById(Long memberId);

  void validate(Long memberId);

  void notify(Study newstudy);

  void notify(Member member);
}
