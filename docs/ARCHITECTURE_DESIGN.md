# FlowMaster 统一架构设计规范

## 📋 架构设计概览

**项目**: FlowMaster - 企业级智能审批流平台  
**设计原则**: 统一性、一致性、可扩展性、可维护性  
**架构思维**: 统一返回结构、统一异常处理、统一日志记录、统一安全控制  

## 🎯 核心架构组件

### 1. 统一返回数据结构

#### 1.1 响应结果封装
```java
/**
 * 统一响应结果封装
 * @param <T> 数据类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    
    /**
     * 响应码
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    /**
     * 请求追踪ID
     */
    private String traceId;
    
    /**
     * 成功响应
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), 
                           ResultCode.SUCCESS.getMessage(), 
                           data, 
                           System.currentTimeMillis(),
                           TraceContext.getTraceId());
    }
    
    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return success(null);
    }
    
    /**
     * 失败响应
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), 
                           resultCode.getMessage(), 
                           null, 
                           System.currentTimeMillis(),
                           TraceContext.getTraceId());
    }
    
    /**
     * 失败响应（自定义消息）
     */
    public static <T> Result<T> error(ResultCode resultCode, String message) {
        return new Result<>(resultCode.getCode(), 
                           message, 
                           null, 
                           System.currentTimeMillis(),
                           TraceContext.getTraceId());
    }
    
    /**
     * 失败响应（自定义码和消息）
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, 
                           message, 
                           null, 
                           System.currentTimeMillis(),
                           TraceContext.getTraceId());
    }
}
```

#### 1.2 响应码枚举
```java
/**
 * 统一响应码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    
    // 成功
    SUCCESS(200, "操作成功"),
    
    // 客户端错误
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    CONFLICT(409, "资源冲突"),
    VALIDATION_ERROR(422, "参数验证失败"),
    
    // 服务器错误
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),
    
    // 业务错误
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    INVALID_CREDENTIALS(1003, "用户名或密码错误"),
    USER_DISABLED(1004, "用户已被禁用"),
    
    // 工作流错误
    PROCESS_DEFINITION_NOT_FOUND(2001, "流程定义不存在"),
    PROCESS_INSTANCE_NOT_FOUND(2002, "流程实例不存在"),
    TASK_NOT_FOUND(2003, "任务不存在"),
    TASK_ALREADY_COMPLETED(2004, "任务已完成"),
    INVALID_TASK_ASSIGNEE(2005, "任务指派人无效"),
    
    // 审批错误
    APPROVAL_RULE_NOT_FOUND(3001, "审批规则不存在"),
    APPROVAL_REQUEST_NOT_FOUND(3002, "审批请求不存在"),
    APPROVAL_ALREADY_PROCESSED(3003, "审批已处理"),
    INVALID_APPROVAL_DECISION(3004, "审批决定无效"),
    
    // 权限错误
    PERMISSION_DENIED(4001, "权限不足"),
    ROLE_NOT_FOUND(4002, "角色不存在"),
    PERMISSION_NOT_FOUND(4003, "权限不存在"),
    INVALID_PERMISSION_ASSIGNMENT(4004, "权限分配无效"),
    
    // 系统错误
    DATABASE_ERROR(5001, "数据库操作失败"),
    CACHE_ERROR(5002, "缓存操作失败"),
    MESSAGE_QUEUE_ERROR(5003, "消息队列操作失败"),
    EXTERNAL_SERVICE_ERROR(5004, "外部服务调用失败"),
    FILE_UPLOAD_ERROR(5005, "文件上传失败"),
    FILE_DOWNLOAD_ERROR(5006, "文件下载失败");
    
    private final Integer code;
    private final String message;
}
```

#### 1.3 分页响应结构
```java
/**
 * 分页响应结果
 * @param <T> 数据类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    
    /**
     * 数据列表
     */
    private List<T> records;
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 当前页码
     */
    private Long current;
    
    /**
     * 每页大小
     */
    private Long size;
    
    /**
     * 总页数
     */
    private Long pages;
    
    /**
     * 是否有下一页
     */
    private Boolean hasNext;
    
    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;
    
    /**
     * 创建分页结果
     */
    public static <T> PageResult<T> of(List<T> records, Long total, Long current, Long size) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setRecords(records);
        pageResult.setTotal(total);
        pageResult.setCurrent(current);
        pageResult.setSize(size);
        pageResult.setPages((total + size - 1) / size);
        pageResult.setHasNext(current < pageResult.getPages());
        pageResult.setHasPrevious(current > 1);
        return pageResult;
    }
}
```

### 2. 统一异常处理

#### 2.1 异常体系设计
```java
/**
 * 业务异常基类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
    
    private final Integer code;
    private final String message;
    private final Object[] args;
    
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.args = null;
    }
    
    public BusinessException(ResultCode resultCode, Object... args) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.args = args;
    }
    
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.args = null;
    }
    
    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.args = null;
    }
}

/**
 * 系统异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemException extends RuntimeException {
    
    private final Integer code;
    private final String message;
    
    public SystemException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
    
    public SystemException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
    
    public SystemException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    public SystemException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}

/**
 * 参数异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ParameterException extends RuntimeException {
    
    private final Integer code;
    private final String message;
    private final String field;
    
    public ParameterException(String field, String message) {
        super(message);
        this.code = ResultCode.VALIDATION_ERROR.getCode();
        this.message = message;
        this.field = field;
    }
    
    public ParameterException(String message) {
        super(message);
        this.code = ResultCode.VALIDATION_ERROR.getCode();
        this.message = message;
        this.field = null;
    }
}
```

#### 2.2 全局异常处理器
```java
/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * 业务异常处理
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 系统异常处理
     */
    @ExceptionHandler(SystemException.class)
    public Result<Void> handleSystemException(SystemException e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 参数异常处理
     */
    @ExceptionHandler(ParameterException.class)
    public Result<Void> handleParameterException(ParameterException e) {
        log.warn("参数异常: {}", e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 参数验证异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("参数验证异常: {}", e.getMessage(), e);
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return Result.error(ResultCode.VALIDATION_ERROR, message);
    }
    
    /**
     * 参数绑定异常处理
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        log.warn("参数绑定异常: {}", e.getMessage(), e);
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return Result.error(ResultCode.VALIDATION_ERROR, message);
    }
    
    /**
     * 参数类型转换异常处理
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("参数类型转换异常: {}", e.getMessage(), e);
        String message = String.format("参数 %s 类型转换失败", e.getName());
        return Result.error(ResultCode.VALIDATION_ERROR, message);
    }
    
    /**
     * 请求方法不支持异常处理
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("请求方法不支持异常: {}", e.getMessage(), e);
        return Result.error(ResultCode.METHOD_NOT_ALLOWED);
    }
    
    /**
     * 请求媒体类型不支持异常处理
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result<Void> handleMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.warn("请求媒体类型不支持异常: {}", e.getMessage(), e);
        return Result.error(ResultCode.BAD_REQUEST, "不支持的媒体类型");
    }
    
    /**
     * 认证异常处理
     */
    @ExceptionHandler(AuthenticationException.class)
    public Result<Void> handleAuthenticationException(AuthenticationException e) {
        log.warn("认证异常: {}", e.getMessage(), e);
        return Result.error(ResultCode.UNAUTHORIZED);
    }
    
    /**
     * 授权异常处理
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("授权异常: {}", e.getMessage(), e);
        return Result.error(ResultCode.FORBIDDEN);
    }
    
    /**
     * 资源不存在异常处理
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("资源不存在异常: {}", e.getMessage(), e);
        return Result.error(ResultCode.NOT_FOUND);
    }
    
    /**
     * 数据库异常处理
     */
    @ExceptionHandler(DataAccessException.class)
    public Result<Void> handleDataAccessException(DataAccessException e) {
        log.error("数据库异常: {}", e.getMessage(), e);
        return Result.error(ResultCode.DATABASE_ERROR);
    }
    
    /**
     * 缓存异常处理
     */
    @ExceptionHandler(CacheException.class)
    public Result<Void> handleCacheException(CacheException e) {
        log.error("缓存异常: {}", e.getMessage(), e);
        return Result.error(ResultCode.CACHE_ERROR);
    }
    
    /**
     * 消息队列异常处理
     */
    @ExceptionHandler(MessagingException.class)
    public Result<Void> handleMessagingException(MessagingException e) {
        log.error("消息队列异常: {}", e.getMessage(), e);
        return Result.error(ResultCode.MESSAGE_QUEUE_ERROR);
    }
    
    /**
     * 通用异常处理
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("未知异常: {}", e.getMessage(), e);
        return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
    }
}
```

### 3. 统一日志记录

#### 3.1 日志配置
```yaml
# logback-spring.xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- 控制台输出 -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <timestamp/>
                <logLevel/>
                <loggerName/>
                <message/>
                <mdc/>
                <stackTrace/>
            </providers>
        </encoder>
    </appender>
    
    <!-- 文件输出 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/flowmaster.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/flowmaster.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxFileSize>100MB</maxFileSize>
            <maxHistory>30</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
        </rollingPolicy>
        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <timestamp/>
                <logLevel/>
                <loggerName/>
                <message/>
                <mdc/>
                <stackTrace/>
            </providers>
        </encoder>
    </appender>
    
    <!-- 错误日志 -->
    <appender name="ERROR_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/flowmaster-error.log</file>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/flowmaster-error.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxFileSize>100MB</maxFileSize>
            <maxHistory>30</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
        </rollingPolicy>
        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <timestamp/>
                <logLevel/>
                <loggerName/>
                <message/>
                <mdc/>
                <stackTrace/>
            </providers>
        </encoder>
    </appender>
    
    <!-- 审计日志 -->
    <appender name="AUDIT_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/flowmaster-audit.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/flowmaster-audit.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxFileSize>100MB</maxFileSize>
            <maxHistory>30</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
        </rollingPolicy>
        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <timestamp/>
                <logLevel/>
                <loggerName/>
                <message/>
                <mdc/>
            </providers>
        </encoder>
    </appender>
    
    <!-- 性能日志 -->
    <appender name="PERFORMANCE_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/flowmaster-performance.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/flowmaster-performance.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxFileSize>100MB</maxFileSize>
            <maxHistory>30</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
        </rollingPolicy>
        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <timestamp/>
                <logLevel/>
                <loggerName/>
                <message/>
                <mdc/>
            </providers>
        </encoder>
    </appender>
    
    <!-- 日志级别配置 -->
    <logger name="com.flowmaster" level="INFO" additivity="false">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
        <appender-ref ref="ERROR_FILE"/>
    </logger>
    
    <logger name="AUDIT" level="INFO" additivity="false">
        <appender-ref ref="AUDIT_FILE"/>
    </logger>
    
    <logger name="PERFORMANCE" level="INFO" additivity="false">
        <appender-ref ref="PERFORMANCE_FILE"/>
    </logger>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
        <appender-ref ref="ERROR_FILE"/>
    </root>
</configuration>
```

#### 3.2 日志工具类
```java
/**
 * 日志工具类
 */
@Slf4j
public class LogUtils {
    
    /**
     * 业务日志
     */
    public static void businessLog(String operation, String userId, String details) {
        MDC.put("operation", operation);
        MDC.put("userId", userId);
        MDC.put("details", details);
        log.info("业务操作: {}", operation);
        MDC.clear();
    }
    
    /**
     * 审计日志
     */
    public static void auditLog(String action, String userId, String resource, String result) {
        Logger auditLogger = LoggerFactory.getLogger("AUDIT");
        auditLogger.info("审计日志 - 操作: {}, 用户: {}, 资源: {}, 结果: {}", 
                        action, userId, resource, result);
    }
    
    /**
     * 性能日志
     */
    public static void performanceLog(String method, long duration, String params) {
        Logger performanceLogger = LoggerFactory.getLogger("PERFORMANCE");
        performanceLogger.info("性能日志 - 方法: {}, 耗时: {}ms, 参数: {}", 
                              method, duration, params);
    }
    
    /**
     * 安全日志
     */
    public static void securityLog(String event, String userId, String ip, String details) {
        log.warn("安全事件 - 事件: {}, 用户: {}, IP: {}, 详情: {}", 
                event, userId, ip, details);
    }
    
    /**
     * 错误日志
     */
    public static void errorLog(String operation, String userId, Throwable throwable) {
        log.error("操作失败 - 操作: {}, 用户: {}, 错误: {}", 
                 operation, userId, throwable.getMessage(), throwable);
    }
}
```

### 4. 统一安全控制

#### 4.1 安全配置
```java
/**
 * 安全配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF
            .csrf().disable()
            // 禁用Session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // 异常处理
            .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
            .and()
            // 请求授权
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            // 添加JWT过滤器
            .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

#### 4.2 JWT工具类
```java
/**
 * JWT工具类
 */
@Component
@Slf4j
public class JwtUtils {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;
    
    /**
     * 生成Token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("authorities", userDetails.getAuthorities());
        return createToken(claims, userDetails.getUsername());
    }
    
    /**
     * 创建Token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    
    /**
     * 验证Token
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            log.error("Token验证失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    /**
     * 获取过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    /**
     * 获取声明
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * 获取所有声明
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    
    /**
     * 检查Token是否过期
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    /**
     * 刷新Token
     */
    public String refreshToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return createToken(claims, claims.getSubject());
        } catch (Exception e) {
            log.error("Token刷新失败: {}", e.getMessage());
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
    }
}
```

### 5. 统一参数验证

#### 5.1 验证注解
```java
/**
 * 自定义验证注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface ValidEnum {
    String message() default "无效的枚举值";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Enum<?>> enumClass();
}

/**
 * 手机号验证注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
    String message() default "手机号格式不正确";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

/**
 * 手机号验证器
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    
    private static final String PHONE_PATTERN = "^1[3-9]\\d{9}$";
    
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return true; // 允许为空，使用@NotNull单独验证
        }
        return phoneNumber.matches(PHONE_PATTERN);
    }
}
```

#### 5.2 验证工具类
```java
/**
 * 验证工具类
 */
@Component
public class ValidationUtils {
    
    /**
     * 验证对象
     */
    public static void validate(Object object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new ParameterException(message);
        }
    }
    
    /**
     * 验证对象（指定组）
     */
    public static void validate(Object object, Class<?>... groups) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(object, groups);
        
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new ParameterException(message);
        }
    }
    
    /**
     * 验证属性
     */
    public static void validateProperty(Object object, String propertyName) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validateProperty(object, propertyName);
        
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new ParameterException(message);
        }
    }
}
```

### 6. 统一缓存管理

#### 6.1 缓存配置
```java
/**
 * 缓存配置
 */
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .recordStats());
        return cacheManager;
    }
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // 设置序列化器
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonVisibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        
        template.afterPropertiesSet();
        return template;
    }
}
```

#### 6.2 缓存工具类
```java
/**
 * 缓存工具类
 */
@Component
@Slf4j
public class CacheUtils {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 设置缓存
     */
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error("设置缓存失败: {}", e.getMessage(), e);
            throw new SystemException(ResultCode.CACHE_ERROR);
        }
    }
    
    /**
     * 设置缓存（带过期时间）
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        } catch (Exception e) {
            log.error("设置缓存失败: {}", e.getMessage(), e);
            throw new SystemException(ResultCode.CACHE_ERROR);
        }
    }
    
    /**
     * 获取缓存
     */
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("获取缓存失败: {}", e.getMessage(), e);
            throw new SystemException(ResultCode.CACHE_ERROR);
        }
    }
    
    /**
     * 删除缓存
     */
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("删除缓存失败: {}", e.getMessage(), e);
            throw new SystemException(ResultCode.CACHE_ERROR);
        }
    }
    
    /**
     * 批量删除缓存
     */
    public void delete(Collection<String> keys) {
        try {
            redisTemplate.delete(keys);
        } catch (Exception e) {
            log.error("批量删除缓存失败: {}", e.getMessage(), e);
            throw new SystemException(ResultCode.CACHE_ERROR);
        }
    }
    
    /**
     * 检查缓存是否存在
     */
    public boolean exists(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("检查缓存是否存在失败: {}", e.getMessage(), e);
            throw new SystemException(ResultCode.CACHE_ERROR);
        }
    }
}
```

### 7. 统一消息队列

#### 7.1 消息队列配置
```java
/**
 * 消息队列配置
 */
@Configuration
@EnableJms
public class JmsConfig {
    
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setDefaultDestinationName("flowmaster.queue");
        return template;
    }
    
    @Bean
    public Queue workflowQueue() {
        return new ActiveMQQueue("workflow.queue");
    }
    
    @Bean
    public Queue approvalQueue() {
        return new ActiveMQQueue("approval.queue");
    }
    
    @Bean
    public Queue notificationQueue() {
        return new ActiveMQQueue("notification.queue");
    }
}
```

#### 7.2 消息工具类
```java
/**
 * 消息工具类
 */
@Component
@Slf4j
public class MessageUtils {
    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    /**
     * 发送消息
     */
    public void sendMessage(String destination, Object message) {
        try {
            jmsTemplate.convertAndSend(destination, message);
            log.info("消息发送成功: 目标={}, 消息={}", destination, message);
        } catch (Exception e) {
            log.error("消息发送失败: {}", e.getMessage(), e);
            throw new SystemException(ResultCode.MESSAGE_QUEUE_ERROR);
        }
    }
    
    /**
     * 发送工作流消息
     */
    public void sendWorkflowMessage(WorkflowMessage message) {
        sendMessage("workflow.queue", message);
    }
    
    /**
     * 发送审批消息
     */
    public void sendApprovalMessage(ApprovalMessage message) {
        sendMessage("approval.queue", message);
    }
    
    /**
     * 发送通知消息
     */
    public void sendNotificationMessage(NotificationMessage message) {
        sendMessage("notification.queue", message);
    }
}
```

### 8. 统一监控指标

#### 8.1 监控配置
```java
/**
 * 监控配置
 */
@Configuration
@EnableScheduling
public class MonitoringConfig {
    
    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }
    
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
    
    @Bean
    public CountedAspect countedAspect(MeterRegistry registry) {
        return new CountedAspect(registry);
    }
}
```

#### 8.2 监控工具类
```java
/**
 * 监控工具类
 */
@Component
@Slf4j
public class MonitoringUtils {
    
    @Autowired
    private MeterRegistry meterRegistry;
    
    /**
     * 记录计数器
     */
    public void incrementCounter(String name, String... tags) {
        Counter.builder(name)
                .tags(tags)
                .register(meterRegistry)
                .increment();
    }
    
    /**
     * 记录计时器
     */
    public void recordTimer(String name, long duration, String... tags) {
        Timer.builder(name)
                .tags(tags)
                .register(meterRegistry)
                .record(duration, TimeUnit.MILLISECONDS);
    }
    
    /**
     * 记录仪表
     */
    public void recordGauge(String name, double value, String... tags) {
        Gauge.builder(name)
                .tags(tags)
                .register(meterRegistry, () -> value);
    }
    
    /**
     * 记录业务指标
     */
    public void recordBusinessMetric(String operation, String result, long duration) {
        incrementCounter("business.operation", "operation", operation, "result", result);
        recordTimer("business.duration", duration, "operation", operation);
    }
}
```

---

## 🎯 架构设计原则

### 1. 统一性原则
- **统一返回结构**: 所有API使用相同的Result结构
- **统一异常处理**: 所有异常通过GlobalExceptionHandler处理
- **统一日志格式**: 所有日志使用JSON格式
- **统一安全控制**: 所有请求通过JWT验证

### 2. 一致性原则
- **命名一致性**: 所有类、方法、变量命名遵循统一规范
- **代码风格一致性**: 使用统一的代码格式化工具
- **注释一致性**: 所有公共方法都有完整的JavaDoc注释
- **配置一致性**: 所有配置使用统一的格式和结构

### 3. 可扩展性原则
- **接口抽象**: 所有服务都定义接口，便于扩展
- **配置外部化**: 所有配置都可以外部化
- **插件化设计**: 支持插件化扩展
- **版本兼容**: 支持API版本管理

### 4. 可维护性原则
- **模块化设计**: 功能模块化，职责清晰
- **文档完善**: 完整的代码文档和API文档
- **测试覆盖**: 高测试覆盖率
- **监控完善**: 完善的监控和告警机制

---

**FlowMaster架构团队**  
*让审批流程更智能，让企业管理更高效！*
