package com.example.testjava;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

public class FindSlowTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

  private final long THRESHOLD;

  public FindSlowTestExtension(long THRESHOLD) {
    this.THRESHOLD = THRESHOLD;
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) throws Exception {
    ExtensionContext.Store store = getStore(context);
    store.put("START_TIME", System.currentTimeMillis());
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {

    // 리플렉션 사용
    Method requiredTestMethod = context.getRequiredTestMethod();
    Test annotation = requiredTestMethod.getAnnotation(Test.class);


    String testMethodName = context.getRequiredTestMethod().getName();
    ExtensionContext.Store store = getStore(context);
    Long start_time = store.remove("START_TIME", long.class);
    long duration = System.currentTimeMillis() - start_time;
    if (duration > THRESHOLD) { // 1초 이상 걸리는테스트
      System.out.printf("Please consider mark method [%s] with @SlowTest \n", testMethodName);
    }
  }

  private static ExtensionContext.Store getStore(ExtensionContext context) {
    String testClassName = context.getRequiredTestClass().getName();
    String testMethodName = context.getRequiredTestMethod().getName();
    ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(testClassName, testMethodName));
    return store;
  }

}
