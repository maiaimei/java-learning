package org.example.api;

import jdk.incubator.concurrent.ScopedValue;

/**
 * Exception in thread "main" java.lang.NoClassDefFoundError: jdk/incubator/concurrent/ScopedValue
 * <p>
 * 以上报错解决方案：在运行配置（Run Configuration）中添加 VM 选项：--add-modules jdk.incubator.concurrent
 */
public class ScopedValueExample {

  public static void main(String[] args) {
    RequestService service = new RequestService();

    // 测试管理员用户
    try {
      service.handleUserRequest("admin123", "ADMIN");
    } catch (Exception e) {
      System.err.println("Error processing admin request: " + e.getMessage());
    }

    System.out.println("\n-------------------\n");

    // 测试普通用户
    try {
      service.handleUserRequest("user456", "USER");
    } catch (Exception e) {
      System.err.println("Error processing user request: " + e.getMessage());
    }
  }

  // 用户上下文类，存储用户信息
  record UserContext(String userId, String role) {

  }

  // 请求处理服务类
  public static class RequestService {

    // 定义一个作用域值来存储用户上下文
    private static final ScopedValue<UserContext> USER_CONTEXT = ScopedValue.newInstance();

    // 模拟处理用户请求
    public void handleUserRequest(String userId, String role) {
      // 创建用户上下文
      UserContext context = new UserContext(userId, role);

      // JDK 20 (JEP 429)
      // 在特定作用域内执行业务逻辑
      ScopedValue.where(USER_CONTEXT, context)
          .run(() -> {
            // 执行多个业务操作
            validateUser();
            processBusinessLogic();
            auditLog();
          });
    }

    private void validateUser() {
      // 从当前作用域获取用户上下文
      UserContext context = USER_CONTEXT.get();
      System.out.println("Validating user: " + context.userId());

      // 模拟权限检查
      if (!"ADMIN".equals(context.role())) {
        throw new SecurityException("Insufficient privileges");
      }
    }

    private void processBusinessLogic() {
      UserContext context = USER_CONTEXT.get();
      System.out.println("Processing request for user: " + context.userId());

      // 模拟嵌套方法调用
      performSubTask();
    }

    private void performSubTask() {
      // 在子方法中仍然可以访问同一作用域的值
      UserContext context = USER_CONTEXT.get();
      System.out.println("Performing subtask in context of user: " + context.userId());
    }

    private void auditLog() {
      UserContext context = USER_CONTEXT.get();
      System.out.println("Audit log: User " + context.userId() +
          " performed operation with role " + context.role());
    }
  }
}
