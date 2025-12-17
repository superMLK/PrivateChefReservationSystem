package com.example.privatechefreservationsystem.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class LoggingAspect {


    /**
     * 攔截所有 Service 層的方法
     */
    @Around("execution(* com.example.privatechefreservationsystem.services..*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "SERVICE");
    }

    /**
     * 攔截所有 Controller 層的方法
     */
    @Around("execution(* com.example.privatechefreservationsystem.controllers..*.*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "CONTROLLER");
    }

    /**
     * 攔截帶有 @LogTrace 註解的方法（保留向後兼容性）
     */
    @Around("@annotation(logTrace)")
    public Object logAnnotatedMethods(ProceedingJoinPoint joinPoint, LogTrace logTrace) throws Throwable {
        return logMethodExecution(joinPoint, logTrace.value());
    }

    /**
     * 統一的日誌記錄邏輯
     */
    private Object logMethodExecution(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 獲取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        // 格式化參數（過濾掉敏感信息）
        String formattedArgs = formatArguments(args);

        // 記錄方法開始執行
        log.info("【{}】▶ {}.{} | 參數: {}",
                layer, className, methodName, formattedArgs);

        Object result;
        try {
            // 執行原方法
            result = joinPoint.proceed();

            long timeTaken = System.currentTimeMillis() - startTime;

            // 格式化返回值
            String formattedResult = formatResult(result);

            // 記錄方法成功結束
            log.info("【{}】✓ {}.{} | 耗時: {}ms | 返回: {}",
                    layer, className, methodName, timeTaken, formattedResult);

            return result;

        } catch (Throwable ex) {
            long timeTaken = System.currentTimeMillis() - startTime;

            // 記錄異常詳情
            log.error("【{}】✗ {}.{} | 耗時: {}ms | 異常: {} - {}",
                    layer, className, methodName, timeTaken,
                    ex.getClass().getSimpleName(), ex.getMessage());

            // 如果是業務異常，記錄為 WARN 級別，否則記錄堆疊追蹤
            if (ex.getClass().getSimpleName().contains("BusinessException") ||
                ex.getClass().getSimpleName().contains("SecurityException")) {
                log.warn("【{}】業務異常詳情: {}", layer, ex.getMessage());
            } else {
                log.error("【{}】系統異常堆疊:", layer, ex);
            }

            throw ex;
        }
    }

    /**
     * 格式化參數（隱藏敏感信息）
     */
    private String formatArguments(Object[] args) {
        if (args == null || args.length == 0) {
            return "無參數";
        }

        return Arrays.stream(args)
                .map(arg -> {
                    if (arg == null) {
                        return "null";
                    }

                    String argString = arg.toString();

                    // 隱藏密碼相關信息
                    if (argString.contains("password=") || argString.contains("Password=")) {
                        return argString.replaceAll("password=[^,)\\s]+", "password=***")
                                       .replaceAll("Password=[^,)\\s]+", "Password=***");
                    }

                    // 限制長度避免日誌過長
                    if (argString.length() > 200) {
                        return argString.substring(0, 197) + "...";
                    }

                    return argString;
                })
                .collect(Collectors.joining(", "));
    }

    /**
     * 格式化返回值
     */
    private String formatResult(Object result) {
        if (result == null) {
            return "null";
        }

        String resultString = result.toString();

        // 限制長度避免日誌過長
        if (resultString.length() > 200) {
            return resultString.substring(0, 197) + "...";
        }

        return resultString;
    }
}
