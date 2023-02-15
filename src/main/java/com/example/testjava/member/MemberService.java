package com.example.testjava.member;

import com.example.testjava.domain.Member;

import java.util.Optional;

public interface MemberService {

  Optional<Member> findById(Long memberId);

  void validate(Long memberId);
}
