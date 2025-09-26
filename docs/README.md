# FlowMaster - 企业级智能审批流平台

## 📋 项目简介

FlowMaster 是一个企业级智能审批流管理平台，支持复杂的业务流程自动化管理。平台采用微服务架构设计，具备高可用性、高扩展性和高安全性，能够满足大型企业的审批流程管理需求。

### 🎯 核心特性

- **多触发方式**: 支持API调用、消息队列监听、定时任务、事件驱动等多种触发方式
- **多渠道通知**: 集成站内信、短信、微信、邮件、钉钉、企业微信等多种通知渠道
- **灵活集成**: 可作为独立系统部署，也可无缝集成到现有业务系统
- **状态回调**: 审批结果实时回调至业务系统，支持状态同步和事件通知
- **可视化设计**: 基于React的低代码/零代码流程设计器，支持拖拽式流程建模
- **响应式布局**: 支持PC端、移动端多终端访问，提供原生移动应用
- **企业级安全**: 完善的权限控制、审计日志、数据加密、单点登录(SSO)
- **智能分析**: 流程性能分析、审批效率统计、业务洞察报告
- **高可用性**: 支持集群部署、故障转移、数据备份恢复

## 🏗️ 技术架构

### 统一架构设计

#### 架构设计原则
```yaml
architecture-principles:
  uniformity:
    description: "统一性原则"
    components:
      - "统一返回结构 (Result<T>)"
      - "统一异常处理 (GlobalExceptionHandler)"
      - "统一日志格式 (JSON格式)"
      - "统一安全控制 (JWT验证)"
  
  consistency:
    description: "一致性原则"
    components:
      - "命名一致性 (驼峰命名)"
      - "代码风格一致性 (格式化工具)"
      - "注释一致性 (JavaDoc)"
      - "配置一致性 (YAML格式)"
  
  extensibility:
    description: "可扩展性原则"
    components:
      - "接口抽象 (Service接口)"
      - "配置外部化 (application.yml)"
      - "插件化设计 (SPI机制)"
      - "版本兼容 (API版本管理)"
  
  maintainability:
    description: "可维护性原则"
    components:
      - "模块化设计 (DDD分层)"
      - "文档完善 (代码文档)"
      - "测试覆盖 (单元测试)"
      - "监控完善 (Prometheus)"
```

#### 核心架构组件
```yaml
core-components:
  response-structure:
    description: "统一返回结构"
    components:
      - "Result<T> - 统一响应封装"
      - "PageResult<T> - 分页响应"
      - "ResultCode - 响应码枚举"
      - "TraceContext - 请求追踪"
  
  exception-handling:
    description: "统一异常处理"
    components:
      - "BusinessException - 业务异常"
      - "SystemException - 系统异常"
      - "ParameterException - 参数异常"
      - "GlobalExceptionHandler - 全局异常处理器"
  
  logging-system:
    description: "统一日志系统"
    components:
      - "LogUtils - 日志工具类"
      - "MDC - 日志上下文"
      - "Logback - 日志框架"
      - "ELK Stack - 日志聚合"
  
  security-control:
    description: "统一安全控制"
    components:
      - "JWT - 身份认证"
      - "RBAC - 权限控制"
      - "CORS - 跨域配置"
      - "Rate Limiting - 限流控制"
  
  validation-system:
    description: "统一参数验证"
    components:
      - "ValidationUtils - 验证工具类"
      - "Custom Annotations - 自定义验证注解"
      - "Bean Validation - 标准验证"
      - "Parameter Validation - 参数验证"
  
  cache-management:
    description: "统一缓存管理"
    components:
      - "CacheUtils - 缓存工具类"
      - "Redis - 分布式缓存"
      - "Caffeine - 本地缓存"
      - "Cache Strategy - 缓存策略"
  
  message-queue:
    description: "统一消息队列"
    components:
      - "MessageUtils - 消息工具类"
      - "JMS - 消息服务"
      - "Kafka - 高吞吐消息"
      - "RabbitMQ - 可靠消息"
  
  monitoring-metrics:
    description: "统一监控指标"
    components:
      - "MonitoringUtils - 监控工具类"
      - "Micrometer - 指标收集"
      - "Prometheus - 指标存储"
      - "Grafana - 指标展示"
```

### DDD（领域驱动设计）架构

#### DDD分层架构
```yaml
ddd-layers:
  user-interface:
    description: "用户界面层"
    components: ["Controller", "DTO", "View"]
    responsibility: "处理用户请求，数据转换"
  
  application:
    description: "应用服务层"
    components: ["ApplicationService", "Command", "Query", "EventHandler"]
    responsibility: "业务流程编排，事务管理"
  
  domain:
    description: "领域层"
    components: ["Entity", "ValueObject", "Aggregate", "DomainService", "Repository"]
    responsibility: "核心业务逻辑，领域规则"
  
  infrastructure:
    description: "基础设施层"
    components: ["RepositoryImpl", "ExternalService", "MessageQueue", "Database"]
    responsibility: "技术实现，外部系统集成"
```

#### 领域模型设计
```yaml
domain-models:
  workflow:
    aggregate-root: "ProcessInstance"
    entities: ["ProcessDefinition", "Task", "ProcessVariable"]
    value-objects: ["ProcessStatus", "TaskStatus", "BusinessKey"]
    domain-services: ["ProcessEngine", "TaskAssignment"]
    repositories: ["ProcessInstanceRepository", "TaskRepository"]
  
  user:
    aggregate-root: "User"
    entities: ["Department", "Role", "Permission"]
    value-objects: ["UserId", "Email", "PhoneNumber"]
    domain-services: ["UserAuthentication", "PermissionCheck"]
    repositories: ["UserRepository", "DepartmentRepository"]
  
  approval:
    aggregate-root: "ApprovalRequest"
    entities: ["ApprovalTask", "ApprovalRule", "ApprovalHistory"]
    value-objects: ["ApprovalStatus", "ApprovalDecision", "Amount"]
    domain-services: ["ApprovalEngine", "RuleEngine"]
    repositories: ["ApprovalRequestRepository", "ApprovalTaskRepository"]
```

#### 聚合根设计原则
```yaml
aggregate-design:
  process-instance:
    invariants:
      - "流程实例必须有有效的流程定义"
      - "流程实例状态转换必须符合业务规则"
      - "任务分配必须基于用户权限"
    
    operations:
      - "startProcess()"
      - "completeTask()"
      - "suspendProcess()"
      - "resumeProcess()"
      - "cancelProcess()"
  
  user:
    invariants:
      - "用户必须有唯一的用户名和邮箱"
      - "用户密码必须符合安全策略"
      - "用户状态变更必须记录审计日志"
    
    operations:
      - "createUser()"
      - "updateUser()"
      - "changePassword()"
      - "assignRole()"
      - "deactivateUser()"
```

#### CQRS（命令查询职责分离）模式
```yaml
cqrs-pattern:
  command-side:
    description: "命令端 - 处理写操作"
    components: ["Command", "CommandHandler", "Aggregate", "EventStore"]
    responsibility: "业务逻辑执行，状态变更"
  
  query-side:
    description: "查询端 - 处理读操作"
    components: ["Query", "QueryHandler", "ReadModel", "Projection"]
    responsibility: "数据查询，视图构建"
  
  event-sourcing:
    description: "事件溯源"
    components: ["DomainEvent", "EventStore", "EventPublisher"]
    responsibility: "状态变更记录，事件重放"
```

#### 领域事件处理
```yaml
domain-events:
  process-started:
    publisher: "ProcessInstance"
    subscribers: ["NotificationService", "AuditService", "StatisticsService"]
    payload: ["processInstanceId", "businessKey", "startUser", "timestamp"]
  
  task-assigned:
    publisher: "Task"
    subscribers: ["NotificationService", "WebSocketService"]
    payload: ["taskId", "assignee", "processInstanceId", "dueDate"]
  
  approval-completed:
    publisher: "ApprovalRequest"
    subscribers: ["WorkflowService", "NotificationService", "BusinessSystem"]
    payload: ["approvalId", "decision", "approver", "timestamp"]
```

### DDD代码示例

#### 聚合根示例
```java
/**
 * 流程实例聚合根
 */
@Entity
@Table(name = "process_instances")
public class ProcessInstance {
    
    @Id
    private ProcessInstanceId id;
    
    @Embedded
    private BusinessKey businessKey;
    
    @Enumerated(EnumType.STRING)
    private ProcessStatus status;
    
    @Embedded
    private ProcessDefinitionId processDefinitionId;
    
    @Embedded
    private UserId startUserId;
    
    @ElementCollection
    @CollectionTable(name = "process_variables")
    private Map<String, ProcessVariable> variables;
    
    @OneToMany(mappedBy = "processInstance", cascade = CascadeType.ALL)
    private List<Task> tasks;
    
    @Transient
    private List<DomainEvent> domainEvents = new ArrayList<>();
    
    // 工厂方法
    public static ProcessInstance start(ProcessDefinitionId processDefinitionId, 
                                       BusinessKey businessKey, 
                                       UserId startUserId, 
                                       Map<String, ProcessVariable> variables) {
        ProcessInstance instance = new ProcessInstance();
        instance.id = ProcessInstanceId.generate();
        instance.processDefinitionId = processDefinitionId;
        instance.businessKey = businessKey;
        instance.startUserId = startUserId;
        instance.variables = variables;
        instance.status = ProcessStatus.RUNNING;
        instance.tasks = new ArrayList<>();
        
        // 发布领域事件
        instance.addDomainEvent(new ProcessStartedEvent(
            instance.id, 
            businessKey, 
            startUserId, 
            Instant.now()
        ));
        
        return instance;
    }
    
    // 业务方法
    public void completeTask(TaskId taskId, UserId userId, Map<String, Object> taskVariables) {
        Task task = findTaskById(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task not found: " + taskId);
        }
        
        if (!task.canBeCompletedBy(userId)) {
            throw new UnauthorizedTaskCompletionException("User cannot complete this task");
        }
        
        task.complete(userId, taskVariables);
        
        // 检查是否所有任务都已完成
        if (allTasksCompleted()) {
            this.status = ProcessStatus.COMPLETED;
            addDomainEvent(new ProcessCompletedEvent(this.id, Instant.now()));
        }
    }
    
    public void suspend(UserId userId, String reason) {
        if (this.status != ProcessStatus.RUNNING) {
            throw new InvalidProcessStatusException("Only running processes can be suspended");
        }
        
        this.status = ProcessStatus.SUSPENDED;
        addDomainEvent(new ProcessSuspendedEvent(this.id, userId, reason, Instant.now()));
    }
    
    // 私有方法
    private Task findTaskById(TaskId taskId) {
        return tasks.stream()
            .filter(task -> task.getId().equals(taskId))
            .findFirst()
            .orElse(null);
    }
    
    private boolean allTasksCompleted() {
        return tasks.stream().allMatch(Task::isCompleted);
    }
    
    private void addDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }
    
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
    
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
```

#### 值对象示例
```java
/**
 * 业务键值对象
 */
@Embeddable
public class BusinessKey {
    
    @Column(name = "business_key", nullable = false)
    private String value;
    
    protected BusinessKey() {} // JPA需要
    
    public BusinessKey(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Business key cannot be null or empty");
        }
        if (value.length() > 128) {
            throw new IllegalArgumentException("Business key too long");
        }
        this.value = value.trim();
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusinessKey that = (BusinessKey) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}

/**
 * 流程状态值对象
 */
public enum ProcessStatus {
    RUNNING("运行中"),
    SUSPENDED("已暂停"),
    COMPLETED("已完成"),
    CANCELLED("已取消"),
    TERMINATED("已终止");
    
    private final String description;
    
    ProcessStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean canTransitionTo(ProcessStatus target) {
        return switch (this) {
            case RUNNING -> target == SUSPENDED || target == COMPLETED || target == CANCELLED;
            case SUSPENDED -> target == RUNNING || target == CANCELLED;
            case COMPLETED, CANCELLED, TERMINATED -> false;
        };
    }
}
```

#### 应用服务示例
```java
/**
 * 流程应用服务
 */
@Service
@Transactional
public class ProcessApplicationService {
    
    private final ProcessInstanceRepository processInstanceRepository;
    private final ProcessDefinitionRepository processDefinitionRepository;
    private final DomainEventPublisher eventPublisher;
    private final FlowableEngineAdapter flowableEngine;
    
    public ProcessApplicationService(ProcessInstanceRepository processInstanceRepository,
                                   ProcessDefinitionRepository processDefinitionRepository,
                                   DomainEventPublisher eventPublisher,
                                   FlowableEngineAdapter flowableEngine) {
        this.processInstanceRepository = processInstanceRepository;
        this.processDefinitionRepository = processDefinitionRepository;
        this.eventPublisher = eventPublisher;
        this.flowableEngine = flowableEngine;
    }
    
    /**
     * 启动流程实例
     */
    public ProcessInstanceId startProcess(StartProcessCommand command) {
        // 验证流程定义存在
        ProcessDefinition processDefinition = processDefinitionRepository
            .findById(command.getProcessDefinitionId())
            .orElseThrow(() -> new ProcessDefinitionNotFoundException(
                "Process definition not found: " + command.getProcessDefinitionId()
            ));
        
        // 验证业务键唯一性
        if (processInstanceRepository.existsByBusinessKey(command.getBusinessKey())) {
            throw new DuplicateBusinessKeyException(
                "Business key already exists: " + command.getBusinessKey()
            );
        }
        
        // 创建流程实例聚合
        ProcessInstance processInstance = ProcessInstance.start(
            command.getProcessDefinitionId(),
            command.getBusinessKey(),
            command.getStartUserId(),
            command.getVariables()
        );
        
        // 保存聚合
        processInstanceRepository.save(processInstance);
        
        // 启动Flowable流程
        String flowableProcessInstanceId = flowableEngine.startProcess(
            processDefinition.getFlowableDefinitionKey(),
            processInstance.getId().getValue(),
            command.getVariables()
        );
        
        // 发布领域事件
        processInstance.getDomainEvents().forEach(eventPublisher::publish);
        processInstance.clearDomainEvents();
        
        return processInstance.getId();
    }
    
    /**
     * 完成任务
     */
    public void completeTask(CompleteTaskCommand command) {
        ProcessInstance processInstance = processInstanceRepository
            .findById(command.getProcessInstanceId())
            .orElseThrow(() -> new ProcessInstanceNotFoundException(
                "Process instance not found: " + command.getProcessInstanceId()
            ));
        
        // 执行领域逻辑
        processInstance.completeTask(
            command.getTaskId(),
            command.getUserId(),
            command.getTaskVariables()
        );
        
        // 更新Flowable任务
        flowableEngine.completeTask(command.getTaskId().getValue(), command.getTaskVariables());
        
        // 保存聚合
        processInstanceRepository.save(processInstance);
        
        // 发布领域事件
        processInstance.getDomainEvents().forEach(eventPublisher::publish);
        processInstance.clearDomainEvents();
    }
}
```

#### 命令和查询示例
```java
/**
 * 启动流程命令
 */
public class StartProcessCommand {
    private ProcessDefinitionId processDefinitionId;
    private BusinessKey businessKey;
    private UserId startUserId;
    private Map<String, ProcessVariable> variables;
    
    // 构造函数、getter、setter
    public StartProcessCommand(ProcessDefinitionId processDefinitionId,
                              BusinessKey businessKey,
                              UserId startUserId,
                              Map<String, ProcessVariable> variables) {
        this.processDefinitionId = processDefinitionId;
        this.businessKey = businessKey;
        this.startUserId = startUserId;
        this.variables = variables;
    }
    
    // getters...
}

/**
 * 流程实例查询
 */
public class ProcessInstanceQuery {
    private ProcessInstanceId processInstanceId;
    private BusinessKey businessKey;
    private ProcessStatus status;
    private UserId startUserId;
    private LocalDateTime startTimeFrom;
    private LocalDateTime startTimeTo;
    private int page;
    private int size;
    
    // 构造函数、getter、setter
}

/**
 * 流程实例查询处理器
 */
@Component
public class ProcessInstanceQueryHandler {
    
    private final ProcessInstanceReadModelRepository readModelRepository;
    
    public ProcessInstanceQueryHandler(ProcessInstanceReadModelRepository readModelRepository) {
        this.readModelRepository = readModelRepository;
    }
    
    public Page<ProcessInstanceReadModel> handle(ProcessInstanceQuery query) {
        Specification<ProcessInstanceReadModel> spec = ProcessInstanceSpecification
            .builder()
            .processInstanceId(query.getProcessInstanceId())
            .businessKey(query.getBusinessKey())
            .status(query.getStatus())
            .startUserId(query.getStartUserId())
            .startTimeFrom(query.getStartTimeFrom())
            .startTimeTo(query.getStartTimeTo())
            .build();
        
        return readModelRepository.findAll(spec, 
            PageRequest.of(query.getPage(), query.getSize()));
    }
}
```

#### DDD架构优势

```yaml
ddd-benefits:
  business-alignment:
    - "业务逻辑集中在领域层，与技术实现分离"
    - "领域专家和开发团队使用统一的语言（Ubiquitous Language）"
    - "业务规则变更时影响范围可控"
  
  maintainability:
    - "清晰的层次结构，职责分离明确"
    - "聚合边界明确，降低系统复杂度"
    - "领域事件解耦，提高系统灵活性"
  
  testability:
    - "领域逻辑可以独立测试"
    - "Mock外部依赖，提高测试效率"
    - "业务规则测试覆盖完整"
  
  scalability:
    - "微服务边界清晰，易于拆分"
    - "CQRS模式支持读写分离"
    - "事件驱动架构支持异步处理"
```

#### DDD最佳实践

```yaml
ddd-best-practices:
  aggregate-design:
    - "保持聚合根尽可能小"
    - "通过ID引用其他聚合，避免直接对象引用"
    - "确保聚合内的一致性边界"
    - "使用领域事件处理跨聚合的交互"
  
  domain-modeling:
    - "优先使用值对象而非原始类型"
    - "将业务规则封装在领域对象中"
    - "使用工厂方法创建复杂对象"
    - "避免贫血模型，保持行为和数据在一起"
  
  event-handling:
    - "领域事件应该是不可变的"
    - "事件处理应该是幂等的"
    - "使用事件存储进行状态重建"
    - "异步处理非关键路径的事件"
  
  repository-pattern:
    - "仓储接口定义在领域层"
    - "仓储实现放在基础设施层"
    - "使用规约模式处理复杂查询"
    - "避免在仓储中暴露技术细节"
```

### 技术选型理由

#### 工作流引擎选择：Flowable 7.x
- **选择理由**: 
  - 相比Activiti，Flowable提供更好的BPMN 2.0支持和更活跃的社区
  - 相比Camunda，Flowable更轻量级，学习成本更低
  - 支持CMMN案例管理，适合复杂业务场景
  - 提供REST API和Java API双重接口
- **性能指标**: 支持10万+并发流程实例，平均响应时间<100ms

#### 微服务框架：Spring Boot 3.x + Spring Cloud
- **选择理由**:
  - Spring Boot 3.x基于Java 17+，性能提升30%
  - Spring Cloud提供完整的微服务解决方案
  - 生态成熟，社区活跃，企业级应用广泛
- **版本兼容性**: 支持JDK 17-21，长期支持版本

### 后端技术栈
- **框架**: Spring Boot 3.x + Spring Cloud 2023.x
- **工作流引擎**: Flowable 7.x (BPMN 2.0 + CMMN 1.1)
- **数据库**: MySQL 8.0 (主库) + Redis 7.x (缓存) + MongoDB (日志存储)
- **消息队列**: Apache Kafka 3.x (高吞吐) + RabbitMQ 3.x (可靠消息)
- **API网关**: Spring Cloud Gateway + Kong (企业级网关)
- **服务注册**: Nacos 2.x (服务发现 + 配置管理)
- **配置中心**: Nacos Config + Apollo (多环境配置)
- **监控**: Micrometer + Prometheus + Grafana + Jaeger (链路追踪)
- **日志**: Logback + ELK Stack (Elasticsearch + Logstash + Kibana)
- **安全**: Spring Security 6.x + JWT + OAuth2 + RBAC
- **缓存**: Redis + Caffeine (本地缓存)
- **搜索引擎**: Elasticsearch 8.x (全文搜索)

### 前端技术栈
- **框架**: React 18 + TypeScript 5.x
- **状态管理**: Redux Toolkit + RTK Query + Zustand (轻量状态)
- **UI组件库**: Ant Design 5.x + Ant Design Pro
- **样式**: SCSS + CSS Modules + Tailwind CSS
- **构建工具**: Vite 4.x + Webpack 5 (兼容性)
- **低代码平台**: 自研流程设计器 (基于React Flow)
- **图表库**: AntV G6 (流程图渲染) + ECharts (数据可视化)
- **移动端**: React Native + Expo (跨平台) / PWA
- **测试**: Jest + React Testing Library + Cypress (E2E)
- **代码质量**: ESLint + Prettier + Husky + lint-staged

### 基础设施
- **容器化**: Docker 24.x + Docker Compose 2.x
- **编排**: Kubernetes 1.28+ + Helm 3.x
- **CI/CD**: Jenkins 2.x / GitLab CI / GitHub Actions
- **代码质量**: SonarQube 9.x + SonarLint
- **API文档**: Swagger 3.x / OpenAPI 3.0 + Knife4j
- **服务网格**: Istio 1.19+ (可选，用于高级流量管理)
- **存储**: MinIO (对象存储) + NFS (文件存储)
- **网络**: Calico (CNI) + Ingress Controller (Nginx/Traefik)
- **备份**: Velero (K8s备份) + MySQL Dump + Redis RDB

## 🎨 系统架构图

### 整体架构图
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                前端层                                        │
├─────────────────────────────────────────────────────────────────────────────┤
│  React Web App  │  React Mobile  │  低代码设计器  │  管理后台  │  PWA应用    │
└─────────────────────────────────────────────────────────────────────────────┘
                                        │
┌─────────────────────────────────────────────────────────────────────────────┐
│                               API网关层                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│  Spring Cloud Gateway + Kong + 负载均衡 + 限流熔断 + 认证授权 + API管理      │
└─────────────────────────────────────────────────────────────────────────────┘
                                        │
┌─────────────────────────────────────────────────────────────────────────────┐
│                               微服务层                                       │
├─────────────────────────────────────────────────────────────────────────────┤
│ 用户服务 │ 流程服务 │ 审批服务 │ 通知服务 │ 集成服务 │ 监控服务 │ 文件服务    │
└─────────────────────────────────────────────────────────────────────────────┘
                                        │
┌─────────────────────────────────────────────────────────────────────────────┐
│                               数据层                                         │
├─────────────────────────────────────────────────────────────────────────────┤
│  MySQL主从  │  Redis集群  │  Kafka集群  │  MinIO  │  Elasticsearch  │ MongoDB │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 微服务架构详细图
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           微服务架构详细图                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐   │
│  │   用户服务   │    │   流程服务   │    │   审批服务   │    │   通知服务   │   │
│  │  User-Svc   │    │ Workflow-Svc│    │Approval-Svc │    │Notify-Svc   │   │
│  │             │    │             │    │             │    │             │   │
│  │ • 用户管理   │    │ • 流程定义   │    │ • 任务处理   │    │ • 消息发送   │   │
│  │ • 组织架构   │    │ • 流程实例   │    │ • 审批历史   │    │ • 模板管理   │   │
│  │ • 权限控制   │    │ • 流程监控   │    │ • 统计分析   │    │ • 渠道配置   │   │
│  └─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘   │
│                                                                             │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐   │
│  │   集成服务   │    │   监控服务   │    │   文件服务   │    │   搜索服务   │   │
│  │Integrate-Svc│    │ Monitor-Svc │    │  File-Svc   │    │ Search-Svc │   │
│  │             │    │             │    │             │    │             │   │
│  │ • API管理   │    │ • 性能监控   │    │ • 文件上传   │    │ • 全文搜索   │   │
│  │ • Webhook   │    │ • 日志分析   │    │ • 文件存储   │    │ • 数据索引   │   │
│  │ • 第三方集成 │    │ • 告警管理   │    │ • 文件预览   │    │ • 搜索优化   │   │
│  └─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘   │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 数据流架构图
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              数据流架构图                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  业务系统 ──→ API网关 ──→ 微服务 ──→ 数据库                                   │
│     │           │          │         │                                      │
│     │           │          │         ▼                                      │
│     │           │          │    ┌─────────┐                                 │
│     │           │          │    │ MySQL   │                                 │
│     │           │          │    │ 主从    │                                 │
│     │           │          │    └─────────┘                                 │
│     │           │          │         │                                      │
│     │           │          │         ▼                                      │
│     │           │          │    ┌─────────┐                                 │
│     │           │          │    │ Redis   │                                 │
│     │           │          │    │ 缓存    │                                 │
│     │           │          │    └─────────┘                                 │
│     │           │          │         │                                      │
│     │           │          │         ▼                                      │
│     │           │          │    ┌─────────┐                                 │
│     │           │          │    │ Kafka   │                                 │
│     │           │          │    │ 消息队列 │                                 │
│     │           │          │    └─────────┘                                 │
│     │           │          │         │                                      │
│     │           │          │         ▼                                      │
│     │           │          │    ┌─────────┐                                 │
│     │           │          │    │ ELK     │                                 │
│     │           │          │    │ 日志    │                                 │
│     │           │          │    └─────────┘                                 │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

## 🔒 安全架构设计

### 认证授权机制

#### JWT Token 设计
```yaml
# JWT配置
jwt:
  secret: ${JWT_SECRET:your-secret-key}
  expiration: 7200000  # 2小时
  refresh-expiration: 604800000  # 7天
  issuer: flowmaster
  audience: flowmaster-users
```

#### 权限模型 (RBAC + ABAC)
- **RBAC (基于角色的访问控制)**:
  - 角色: 系统管理员、流程管理员、审批人、普通用户
  - 权限: 流程创建、流程审批、用户管理、系统配置
- **ABAC (基于属性的访问控制)**:
  - 数据权限: 部门数据隔离、个人数据访问
  - 时间权限: 工作时间访问、节假日限制
  - 位置权限: IP白名单、地理位置限制

#### 安全策略
```yaml
security:
  password:
    min-length: 8
    require-uppercase: true
    require-lowercase: true
    require-numbers: true
    require-special-chars: true
    max-attempts: 5
    lockout-duration: 300000  # 5分钟
  
  session:
    timeout: 1800000  # 30分钟
    max-sessions: 3
    concurrent-login: false
  
  encryption:
    algorithm: AES-256-GCM
    key-rotation: 90  # 90天轮换
```

### 数据安全

#### 敏感数据加密
- **数据库加密**: 
  - 字段级加密: AES-256加密敏感字段
  - 传输加密: TLS 1.3
  - 存储加密: 数据库透明加密(TDE)
- **文件加密**: 
  - 上传文件: AES-256加密存储
  - 文件访问: 临时URL + 过期时间
- **API加密**:
  - 请求签名: HMAC-SHA256
  - 响应加密: 敏感数据脱敏

#### 审计日志
```yaml
audit:
  events:
    - user.login
    - user.logout
    - process.create
    - process.approve
    - process.reject
    - data.access
    - config.change
  
  retention:
    period: 2555  # 7年
    compression: true
    backup: true
  
  fields:
    - timestamp
    - user-id
    - action
    - resource
    - ip-address
    - user-agent
    - result
```

### 网络安全

#### API安全
- **限流策略**: 
  - 用户级: 1000次/小时
  - IP级: 5000次/小时
  - 接口级: 根据业务重要性分级
- **防护机制**:
  - CSRF防护: SameSite Cookie + CSRF Token
  - XSS防护: Content Security Policy + 输入验证
  - SQL注入防护: 参数化查询 + 输入验证
  - 文件上传安全: 文件类型检查 + 病毒扫描

#### 网络安全配置
```yaml
network:
  cors:
    allowed-origins: ["https://yourdomain.com"]
    allowed-methods: ["GET", "POST", "PUT", "DELETE"]
    allowed-headers: ["*"]
    allow-credentials: true
  
  ssl:
    protocol: TLSv1.3
    cipher-suites: ["TLS_AES_256_GCM_SHA384", "TLS_CHACHA20_POLY1305_SHA256"]
    hsts: true
    hsts-max-age: 31536000
```

### 合规性要求

#### 数据保护合规
- **GDPR合规**: 
  - 数据主体权利: 访问、更正、删除、数据可携带
  - 数据处理记录: 处理目的、法律依据、保留期限
  - 隐私影响评估: DPIA流程
- **等保合规**:
  - 等级保护三级要求
  - 安全管理制度
  - 安全技术措施
  - 安全管理措施

#### 安全监控
- **实时监控**:
  - 异常登录检测
  - 权限变更监控
  - 数据访问异常
  - 系统漏洞扫描
- **告警机制**:
  - 邮件告警: 安全事件
  - 短信告警: 紧急安全事件
  - 钉钉告警: 运维团队

## ⚡ 性能指标与优化策略

### 性能指标要求

#### 响应时间指标
```yaml
performance:
  api-response-time:
    p50: 100ms    # 50%请求响应时间
    p95: 300ms    # 95%请求响应时间
    p99: 500ms    # 99%请求响应时间
    max: 1000ms   # 最大响应时间
  
  database-query:
    simple-query: 10ms
    complex-query: 100ms
    batch-operation: 500ms
  
  workflow-execution:
    process-start: 50ms
    task-complete: 100ms
    process-end: 200ms
```

#### 吞吐量指标
```yaml
throughput:
  concurrent-users: 10000
  requests-per-second: 5000
  workflow-instances: 1000/hour
  notifications: 10000/hour
```

#### 资源使用指标
```yaml
resources:
  cpu-usage: 70%
  memory-usage: 80%
  disk-io: 1000 IOPS
  network-bandwidth: 100Mbps
```

### 缓存策略

#### 多级缓存架构
```yaml
cache-strategy:
  l1-cache:  # 应用内缓存
    provider: Caffeine
    max-size: 10000
    expire-after-write: 300s
    expire-after-access: 60s
  
  l2-cache:  # Redis分布式缓存
    provider: Redis
    expire-time: 3600s
    max-memory: 2GB
    eviction-policy: LRU
  
  cdn-cache:  # CDN缓存
    static-resources: 7d
    api-responses: 300s
```

#### 缓存更新策略
- **Cache-Aside**: 应用负责缓存更新
- **Write-Through**: 同步写入缓存和数据库
- **Write-Behind**: 异步写入数据库
- **Refresh-Ahead**: 提前刷新即将过期的缓存

### 数据库优化

#### 索引策略
```sql
-- 用户表索引
CREATE INDEX idx_user_dept ON users(department_id);
CREATE INDEX idx_user_status ON users(status);
CREATE INDEX idx_user_email ON users(email);

-- 流程实例表索引
CREATE INDEX idx_process_def ON process_instances(process_definition_id);
CREATE INDEX idx_process_status ON process_instances(status);
CREATE INDEX idx_process_create_time ON process_instances(create_time);

-- 任务表索引
CREATE INDEX idx_task_assignee ON tasks(assignee);
CREATE INDEX idx_task_status ON tasks(status);
CREATE INDEX idx_task_process ON tasks(process_instance_id);
```

#### 分库分表策略
```yaml
sharding:
  strategy: horizontal
  shard-key: user_id
  shard-count: 8
  
  tables:
    process_instances:
      shard-count: 16
      shard-key: process_instance_id
    
    tasks:
      shard-count: 16
      shard-key: task_id
```

### 前端性能优化

#### 资源优化
```yaml
frontend-optimization:
  bundle-splitting:
    vendor: true
    routes: true
    components: true
  
  lazy-loading:
    routes: true
    components: true
    images: true
  
  compression:
    gzip: true
    brotli: true
  
  cdn:
    static-assets: true
    api-proxy: true
```

#### 渲染优化
- **虚拟滚动**: 大列表性能优化
- **防抖节流**: 搜索和输入优化
- **内存管理**: 组件卸载清理
- **预加载**: 关键资源预加载

### 微服务性能优化

#### 连接池配置
```yaml
connection-pools:
  database:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
  
  redis:
    lettuce:
      pool-max-active: 20
      pool-max-idle: 10
      pool-min-idle: 5
      pool-max-wait: 3000
```

#### 异步处理
```yaml
async-processing:
  thread-pools:
    workflow-executor:
      core-size: 10
      max-size: 50
      queue-capacity: 1000
    
    notification-sender:
      core-size: 5
      max-size: 20
      queue-capacity: 500
  
  message-queues:
    workflow-events:
      partition-count: 8
      replication-factor: 3
    
    notifications:
      partition-count: 4
      replication-factor: 2
```

### 监控与调优

#### 性能监控指标
```yaml
monitoring:
  jvm-metrics:
    - heap.used
    - heap.max
    - gc.pause.time
    - threads.active
  
  application-metrics:
    - http.requests.duration
    - database.connections.active
    - cache.hit.ratio
    - workflow.instances.active
  
  business-metrics:
    - approval.process.time
    - user.login.success.rate
    - notification.delivery.rate
```

#### 性能调优建议
1. **JVM调优**:
   - 堆内存: -Xms4g -Xmx8g
   - GC算法: G1GC
   - GC日志: 启用详细GC日志

2. **数据库调优**:
   - 连接池大小: 根据并发量调整
   - 查询优化: 使用EXPLAIN分析慢查询
   - 索引优化: 定期分析索引使用情况

3. **缓存调优**:
   - 缓存命中率目标: >90%
   - 缓存预热: 系统启动时预加载热点数据
   - 缓存穿透防护: 布隆过滤器

## 📁 项目结构

```
flowmaster/
├── flowmaster-backend/                    # 后端微服务
│   ├── flowmaster-common/                # 公共组件库 (统一架构组件)
│   │   ├── src/main/java/com/flowmaster/common/
│   │   │   ├── response/                 # 统一返回结构
│   │   │   │   ├── Result.java           # 统一响应封装
│   │   │   │   ├── PageResult.java       # 分页响应
│   │   │   │   ├── ResultCode.java       # 响应码枚举
│   │   │   │   └── TraceContext.java     # 请求追踪
│   │   │   ├── exception/                # 统一异常处理
│   │   │   │   ├── BusinessException.java # 业务异常
│   │   │   │   ├── SystemException.java  # 系统异常
│   │   │   │   ├── ParameterException.java # 参数异常
│   │   │   │   └── GlobalExceptionHandler.java # 全局异常处理器
│   │   │   ├── logging/                 # 统一日志记录
│   │   │   │   ├── LogUtils.java         # 日志工具类
│   │   │   │   ├── MDCUtils.java         # MDC工具类
│   │   │   │   └── AuditLogger.java      # 审计日志
│   │   │   ├── security/                # 统一安全控制
│   │   │   │   ├── JwtUtils.java         # JWT工具类
│   │   │   │   ├── SecurityUtils.java    # 安全工具类
│   │   │   │   └── PermissionUtils.java  # 权限工具类
│   │   │   ├── validation/              # 统一参数验证
│   │   │   │   ├── ValidationUtils.java # 验证工具类
│   │   │   │   ├── PhoneNumber.java      # 手机号验证注解
│   │   │   │   └── ValidEnum.java        # 枚举验证注解
│   │   │   ├── cache/                   # 统一缓存管理
│   │   │   │   ├── CacheUtils.java       # 缓存工具类
│   │   │   │   └── CacheConfig.java      # 缓存配置
│   │   │   ├── message/                 # 统一消息队列
│   │   │   │   ├── MessageUtils.java     # 消息工具类
│   │   │   │   └── MessageConfig.java    # 消息配置
│   │   │   ├── monitoring/              # 统一监控指标
│   │   │   │   ├── MonitoringUtils.java  # 监控工具类
│   │   │   │   └── MonitoringConfig.java # 监控配置
│   │   │   └── util/                    # 工具类
│   │   │       ├── DateUtils.java        # 日期工具类
│   │   │       ├── StringUtils.java      # 字符串工具类
│   │   │       └── JsonUtils.java        # JSON工具类
│   │   ├── src/main/resources/
│   │   │   ├── application.yml           # 公共配置
│   │   │   └── logback-spring.xml        # 日志配置
│   │   └── pom.xml
│   │
│   ├── flowmaster-gateway/               # API网关服务
│   │   ├── src/main/java/com/flowmaster/gateway/
│   │   │   ├── config/                   # 网关配置
│   │   │   ├── filter/                   # 过滤器
│   │   │   ├── handler/                  # 处理器
│   │   │   └── GatewayApplication.java
│   │   ├── src/main/resources/
│   │   │   ├── application.yml
│   │   │   └── bootstrap.yml
│   │   └── pom.xml
│   │
│   ├── flowmaster-auth/                  # 认证授权服务
│   │   ├── src/main/java/com/flowmaster/auth/
│   │   │   ├── controller/               # 认证控制器
│   │   │   ├── service/                  # 认证服务
│   │   │   ├── security/                 # 安全配置
│   │   │   ├── jwt/                      # JWT处理
│   │   │   ├── oauth2/                   # OAuth2配置
│   │   │   └── AuthApplication.java
│   │   └── pom.xml
│   │
│   ├── flowmaster-user/                  # 用户管理服务 (DDD架构)
│   │   ├── src/main/java/com/flowmaster/user/
│   │   │   ├── application/              # 应用服务层
│   │   │   │   ├── service/              # 应用服务
│   │   │   │   ├── command/              # 命令对象
│   │   │   │   ├── query/                # 查询对象
│   │   │   │   └── handler/              # 命令/查询处理器
│   │   │   ├── domain/                   # 领域层
│   │   │   │   ├── model/                # 领域模型
│   │   │   │   │   ├── aggregate/        # 聚合根
│   │   │   │   │   ├── entity/           # 实体
│   │   │   │   │   ├── valueobject/      # 值对象
│   │   │   │   │   └── event/            # 领域事件
│   │   │   │   ├── service/              # 领域服务
│   │   │   │   ├── repository/           # 仓储接口
│   │   │   │   └── specification/        # 规约模式
│   │   │   ├── infrastructure/           # 基础设施层
│   │   │   │   ├── repository/           # 仓储实现
│   │   │   │   ├── persistence/          # 持久化
│   │   │   │   ├── external/             # 外部服务
│   │   │   │   └── config/               # 配置
│   │   │   ├── interfaces/              # 用户界面层
│   │   │   │   ├── rest/                 # REST控制器
│   │   │   │   ├── dto/                  # 数据传输对象
│   │   │   │   ├── mapper/               # 对象映射
│   │   │   │   └── validator/            # 验证器
│   │   │   └── UserApplication.java
│   │   └── pom.xml
│   │
│   ├── flowmaster-workflow/              # 工作流引擎服务 (DDD架构)
│   │   ├── src/main/java/com/flowmaster/workflow/
│   │   │   ├── application/              # 应用服务层
│   │   │   │   ├── service/              # 应用服务
│   │   │   │   ├── command/              # 命令对象
│   │   │   │   │   ├── StartProcessCommand.java
│   │   │   │   │   ├── CompleteTaskCommand.java
│   │   │   │   │   └── SuspendProcessCommand.java
│   │   │   │   ├── query/                # 查询对象
│   │   │   │   │   ├── ProcessInstanceQuery.java
│   │   │   │   │   └── TaskQuery.java
│   │   │   │   └── handler/              # 命令/查询处理器
│   │   │   ├── domain/                   # 领域层
│   │   │   │   ├── model/                # 领域模型
│   │   │   │   │   ├── aggregate/        # 聚合根
│   │   │   │   │   │   ├── ProcessInstance.java
│   │   │   │   │   │   └── ProcessDefinition.java
│   │   │   │   │   ├── entity/           # 实体
│   │   │   │   │   │   ├── Task.java
│   │   │   │   │   │   └── ProcessVariable.java
│   │   │   │   │   ├── valueobject/      # 值对象
│   │   │   │   │   │   ├── ProcessStatus.java
│   │   │   │   │   │   ├── TaskStatus.java
│   │   │   │   │   │   └── BusinessKey.java
│   │   │   │   │   └── event/            # 领域事件
│   │   │   │   │       ├── ProcessStartedEvent.java
│   │   │   │   │       └── TaskCompletedEvent.java
│   │   │   │   ├── service/              # 领域服务
│   │   │   │   │   ├── ProcessEngine.java
│   │   │   │   │   └── TaskAssignmentService.java
│   │   │   │   ├── repository/           # 仓储接口
│   │   │   │   │   ├── ProcessInstanceRepository.java
│   │   │   │   │   └── TaskRepository.java
│   │   │   │   └── specification/        # 规约模式
│   │   │   │       └── ProcessSpecification.java
│   │   │   ├── infrastructure/           # 基础设施层
│   │   │   │   ├── repository/           # 仓储实现
│   │   │   │   ├── persistence/          # 持久化
│   │   │   │   │   ├── entity/           # JPA实体
│   │   │   │   │   └── mapper/           # 对象映射
│   │   │   │   ├── external/             # 外部服务
│   │   │   │   │   └── FlowableEngineAdapter.java
│   │   │   │   └── config/               # 配置
│   │   │   ├── interfaces/              # 用户界面层
│   │   │   │   ├── rest/                 # REST控制器
│   │   │   │   │   ├── ProcessController.java
│   │   │   │   │   └── TaskController.java
│   │   │   │   ├── dto/                  # 数据传输对象
│   │   │   │   ├── mapper/               # 对象映射
│   │   │   │   └── validator/            # 验证器
│   │   │   └── WorkflowApplication.java
│   │   └── pom.xml
│   │
│   ├── flowmaster-approval/              # 审批业务服务 (DDD架构)
│   │   ├── src/main/java/com/flowmaster/approval/
│   │   │   ├── application/              # 应用服务层
│   │   │   │   ├── service/              # 应用服务
│   │   │   │   ├── command/              # 命令对象
│   │   │   │   │   ├── SubmitApprovalCommand.java
│   │   │   │   │   ├── ApproveCommand.java
│   │   │   │   │   └── RejectCommand.java
│   │   │   │   ├── query/                # 查询对象
│   │   │   │   │   ├── ApprovalRequestQuery.java
│   │   │   │   │   └── ApprovalStatisticsQuery.java
│   │   │   │   └── handler/              # 命令/查询处理器
│   │   │   ├── domain/                   # 领域层
│   │   │   │   ├── model/                # 领域模型
│   │   │   │   │   ├── aggregate/        # 聚合根
│   │   │   │   │   │   ├── ApprovalRequest.java
│   │   │   │   │   │   └── ApprovalRule.java
│   │   │   │   │   ├── entity/           # 实体
│   │   │   │   │   │   ├── ApprovalTask.java
│   │   │   │   │   │   └── ApprovalHistory.java
│   │   │   │   │   ├── valueobject/      # 值对象
│   │   │   │   │   │   ├── ApprovalStatus.java
│   │   │   │   │   │   ├── ApprovalDecision.java
│   │   │   │   │   │   └── Amount.java
│   │   │   │   │   └── event/            # 领域事件
│   │   │   │   │       ├── ApprovalSubmittedEvent.java
│   │   │   │   │       └── ApprovalCompletedEvent.java
│   │   │   │   ├── service/              # 领域服务
│   │   │   │   │   ├── ApprovalEngine.java
│   │   │   │   │   └── RuleEngine.java
│   │   │   │   ├── repository/           # 仓储接口
│   │   │   │   │   ├── ApprovalRequestRepository.java
│   │   │   │   │   └── ApprovalTaskRepository.java
│   │   │   │   └── specification/        # 规约模式
│   │   │   │       └── ApprovalSpecification.java
│   │   │   ├── infrastructure/           # 基础设施层
│   │   │   │   ├── repository/           # 仓储实现
│   │   │   │   ├── persistence/          # 持久化
│   │   │   │   ├── external/             # 外部服务
│   │   │   │   └── config/               # 配置
│   │   │   ├── interfaces/              # 用户界面层
│   │   │   │   ├── rest/                 # REST控制器
│   │   │   │   ├── dto/                  # 数据传输对象
│   │   │   │   ├── mapper/               # 对象映射
│   │   │   │   └── validator/            # 验证器
│   │   │   └── ApprovalApplication.java
│   │   └── pom.xml
│   │
│   ├── flowmaster-notification/          # 通知服务
│   │   ├── src/main/java/com/flowmaster/notification/
│   │   │   ├── controller/               # 通知控制器
│   │   │   ├── service/                  # 通知服务
│   │   │   ├── channel/                  # 通知渠道
│   │   │   │   ├── email/                # 邮件通知
│   │   │   │   ├── sms/                  # 短信通知
│   │   │   │   ├── wechat/               # 微信通知
│   │   │   │   ├── dingtalk/             # 钉钉通知
│   │   │   │   └── inapp/                # 站内信
│   │   │   ├── template/                 # 通知模板
│   │   │   ├── queue/                    # 消息队列
│   │   │   └── NotificationApplication.java
│   │   └── pom.xml
│   │
│   ├── flowmaster-integration/           # 集成服务
│   │   ├── src/main/java/com/flowmaster/integration/
│   │   │   ├── controller/               # 集成控制器
│   │   │   ├── service/                  # 集成服务
│   │   │   ├── api/                      # API管理
│   │   │   ├── webhook/                  # Webhook处理
│   │   │   ├── connector/                # 连接器
│   │   │   └── IntegrationApplication.java
│   │   └── pom.xml
│   │
│   ├── flowmaster-file/                  # 文件服务
│   │   ├── src/main/java/com/flowmaster/file/
│   │   │   ├── controller/               # 文件控制器
│   │   │   ├── service/                  # 文件服务
│   │   │   ├── storage/                  # 存储管理
│   │   │   ├── preview/                  # 文件预览
│   │   │   └── FileApplication.java
│   │   └── pom.xml
│   │
│   ├── flowmaster-search/                 # 搜索服务
│   │   ├── src/main/java/com/flowmaster/search/
│   │   │   ├── controller/               # 搜索控制器
│   │   │   ├── service/                  # 搜索服务
│   │   │   ├── index/                    # 索引管理
│   │   │   ├── query/                    # 查询处理
│   │   │   └── SearchApplication.java
│   │   └── pom.xml
│   │
│   ├── flowmaster-monitor/               # 监控服务
│   │   ├── src/main/java/com/flowmaster/monitor/
│   │   │   ├── controller/               # 监控控制器
│   │   │   ├── service/                  # 监控服务
│   │   │   ├── metrics/                  # 指标收集
│   │   │   ├── alert/                    # 告警处理
│   │   │   ├── health/                   # 健康检查
│   │   │   └── MonitorApplication.java
│   │   └── pom.xml
│   │
│   └── flowmaster-common/                # 公共组件
│       ├── src/main/java/com/flowmaster/common/
│       │   ├── config/                   # 公共配置
│       │   ├── exception/                # 异常处理
│       │   ├── util/                     # 工具类
│       │   ├── constant/                 # 常量定义
│       │   ├── annotation/               # 自定义注解
│       │   ├── aspect/                   # 切面处理
│       │   ├── interceptor/              # 拦截器
│       │   ├── filter/                   # 过滤器
│       │   ├── security/                 # 安全组件
│       │   ├── cache/                    # 缓存组件
│       │   ├── mq/                       # 消息队列组件
│       │   ├── audit/                    # 审计组件
│       │   └── tracing/                  # 链路追踪
│       └── pom.xml
│
├── flowmaster-frontend/                  # 前端应用
│   ├── flowmaster-web/                  # Web端应用
│   │   ├── src/
│   │   │   ├── components/              # 通用组件
│   │   │   ├── pages/                    # 页面组件
│   │   │   │   ├── login/               # 登录页面
│   │   │   │   ├── dashboard/            # 仪表板
│   │   │   │   ├── workflow/            # 工作流页面
│   │   │   │   ├── approval/            # 审批页面
│   │   │   │   ├── user/                # 用户管理
│   │   │   │   └── settings/            # 系统设置
│   │   │   ├── hooks/                    # 自定义Hooks
│   │   │   ├── services/                 # API服务
│   │   │   ├── store/                    # 状态管理
│   │   │   ├── utils/                    # 工具函数
│   │   │   ├── types/                    # TypeScript类型
│   │   │   ├── styles/                   # 样式文件
│   │   │   └── App.tsx
│   │   ├── public/
│   │   ├── package.json
│   │   ├── vite.config.ts
│   │   └── tsconfig.json
│   │
│   ├── flowmaster-mobile/               # 移动端应用
│   │   ├── src/
│   │   │   ├── components/              # 移动端组件
│   │   │   ├── screens/                 # 屏幕组件
│   │   │   ├── navigation/              # 导航配置
│   │   │   ├── services/                # API服务
│   │   │   ├── store/                   # 状态管理
│   │   │   └── App.tsx
│   │   ├── android/                     # Android配置
│   │   ├── ios/                         # iOS配置
│   │   └── package.json
│   │
│   ├── flowmaster-designer/             # 流程设计器
│   │   ├── src/
│   │   │   ├── components/              # 设计器组件
│   │   │   │   ├── palette/             # 组件面板
│   │   │   │   ├── canvas/              # 画布组件
│   │   │   │   ├── properties/          # 属性面板
│   │   │   │   └── toolbar/             # 工具栏
│   │   │   ├── nodes/                   # 节点组件
│   │   │   ├── edges/                   # 连线组件
│   │   │   ├── utils/                   # 工具函数
│   │   │   └── App.tsx
│   │   ├── public/
│   │   └── package.json
│   │
│   └── flowmaster-admin/                # 管理后台
│       ├── src/
│       │   ├── components/              # 管理组件
│       │   ├── pages/                   # 管理页面
│       │   │   ├── system/              # 系统管理
│       │   │   ├── user/                # 用户管理
│       │   │   ├── workflow/            # 流程管理
│       │   │   ├── monitor/             # 监控管理
│       │   │   └── logs/               # 日志管理
│       │   ├── services/                # API服务
│       │   └── App.tsx
│       └── package.json
│
├── flowmaster-infrastructure/            # 基础设施
│   ├── docker/                          # Docker配置
│   │   ├── docker-compose.yml           # 开发环境
│   │   ├── docker-compose.prod.yml      # 生产环境
│   │   ├── mysql/                       # MySQL配置
│   │   │   ├── Dockerfile
│   │   │   └── my.cnf
│   │   ├── redis/                       # Redis配置
│   │   │   ├── Dockerfile
│   │   │   └── redis.conf
│   │   ├── kafka/                       # Kafka配置
│   │   │   └── docker-compose.kafka.yml
│   │   └── elk/                         # ELK配置
│   │       └── docker-compose.elk.yml
│   │
│   ├── k8s/                             # Kubernetes配置
│   │   ├── namespace.yaml               # 命名空间
│   │   ├── configmap.yaml               # 配置映射
│   │   ├── secret.yaml                  # 密钥
│   │   ├── mysql/                       # MySQL部署
│   │   │   ├── deployment.yaml
│   │   │   ├── service.yaml
│   │   │   └── pvc.yaml
│   │   ├── redis/                       # Redis部署
│   │   │   ├── deployment.yaml
│   │   │   ├── service.yaml
│   │   │   └── configmap.yaml
│   │   ├── kafka/                       # Kafka部署
│   │   │   ├── zookeeper.yaml
│   │   │   ├── kafka.yaml
│   │   │   └── service.yaml
│   │   ├── services/                    # 微服务部署
│   │   │   ├── gateway.yaml
│   │   │   ├── auth.yaml
│   │   │   ├── user.yaml
│   │   │   ├── workflow.yaml
│   │   │   ├── approval.yaml
│   │   │   ├── notification.yaml
│   │   │   ├── integration.yaml
│   │   │   ├── file.yaml
│   │   │   ├── search.yaml
│   │   │   └── monitor.yaml
│   │   ├── ingress.yaml                 # 入口配置
│   │   ├── hpa.yaml                     # 自动扩缩容
│   │   └── monitoring/                  # 监控配置
│   │       ├── prometheus.yaml
│   │       ├── grafana.yaml
│   │       └── alertmanager.yaml
│   │
│   ├── helm/                            # Helm Charts
│   │   ├── flowmaster/                  # 主Chart
│   │   │   ├── Chart.yaml
│   │   │   ├── values.yaml
│   │   │   ├── values-dev.yaml
│   │   │   ├── values-prod.yaml
│   │   │   └── templates/
│   │   │       ├── deployment.yaml
│   │   │       ├── service.yaml
│   │   │       ├── ingress.yaml
│   │   │       ├── configmap.yaml
│   │   │       ├── secret.yaml
│   │   │       └── hpa.yaml
│   │   └── charts/                      # 子Charts
│   │
│   ├── scripts/                         # 部署脚本
│   │   ├── deploy.sh                    # 部署脚本
│   │   ├── backup.sh                    # 备份脚本
│   │   ├── restore.sh                   # 恢复脚本
│   │   ├── health-check.sh              # 健康检查
│   │   ├── scale.sh                     # 扩缩容脚本
│   │   └── monitoring/                  # 监控脚本
│   │       ├── setup-prometheus.sh
│   │       ├── setup-grafana.sh
│   │       └── setup-alertmanager.sh
│   │
│   └── docs/                           # 文档
│       ├── deployment/                  # 部署文档
│       │   ├── docker.md
│       │   ├── kubernetes.md
│       │   ├── helm.md
│       │   └── production.md
│       ├── monitoring/                  # 监控文档
│       │   ├── prometheus.md
│       │   ├── grafana.md
│       │   └── alerting.md
│       └── troubleshooting/            # 故障排除
│           ├── common-issues.md
│           └── performance-tuning.md
│
├── flowmaster-sdk/                      # SDK和集成工具
│   ├── java-sdk/                        # Java集成SDK
│   │   ├── src/main/java/com/flowmaster/sdk/
│   │   │   ├── client/                  # 客户端
│   │   │   ├── model/                   # 数据模型
│   │   │   ├── exception/               # 异常处理
│   │   │   ├── config/                  # 配置
│   │   │   └── FlowMasterClient.java
│   │   ├── src/test/java/               # 测试代码
│   │   ├── pom.xml
│   │   └── README.md
│   │
│   ├── node-sdk/                        # Node.js集成SDK
│   │   ├── src/
│   │   │   ├── client/                  # 客户端
│   │   │   ├── models/                  # 数据模型
│   │   │   ├── exceptions/              # 异常处理
│   │   │   ├── config/                  # 配置
│   │   │   └── index.ts
│   │   ├── tests/                       # 测试代码
│   │   ├── package.json
│   │   └── README.md
│   │
│   ├── python-sdk/                      # Python集成SDK
│   │   ├── flowmaster/
│   │   │   ├── client.py
│   │   │   ├── models.py
│   │   │   ├── exceptions.py
│   │   │   └── config.py
│   │   ├── tests/
│   │   ├── setup.py
│   │   └── README.md
│   │
│   └── webhook/                         # Webhook工具
│       ├── examples/                    # 示例代码
│       │   ├── java/
│       │   ├── nodejs/
│       │   ├── python/
│       │   └── php/
│       ├── templates/                   # 模板
│       └── README.md
│
├── flowmaster-database/                 # 数据库相关
│   ├── mysql/                          # MySQL脚本
│   │   ├── init/                       # 初始化脚本
│   │   │   ├── 01-create-database.sql
│   │   │   ├── 02-create-tables.sql
│   │   │   ├── 03-create-indexes.sql
│   │   │   └── 04-insert-data.sql
│   │   ├── migration/                  # 迁移脚本
│   │   │   ├── V1__Initial_schema.sql
│   │   │   ├── V2__Add_user_table.sql
│   │   │   └── V3__Add_workflow_tables.sql
│   │   └── backup/                     # 备份脚本
│   │       ├── backup.sh
│   │       └── restore.sh
│   │
│   ├── redis/                          # Redis配置
│   │   ├── redis.conf                   # 配置文件
│   │   ├── cluster.conf                 # 集群配置
│   │   └── scripts/                    # 脚本
│   │       ├── setup-cluster.sh
│   │       └── backup.sh
│   │
│   └── elasticsearch/                  # Elasticsearch配置
│       ├── elasticsearch.yml
│       ├── mappings/                    # 索引映射
│       │   ├── workflow.json
│       │   ├── task.json
│       │   └── audit.json
│       └── templates/                   # 索引模板
│
├── flowmaster-tests/                    # 测试相关
│   ├── unit-tests/                      # 单元测试
│   │   ├── backend/                     # 后端测试
│   │   └── frontend/                    # 前端测试
│   ├── integration-tests/               # 集成测试
│   │   ├── api-tests/                   # API测试
│   │   ├── workflow-tests/              # 工作流测试
│   │   └── performance-tests/           # 性能测试
│   ├── e2e-tests/                       # 端到端测试
│   │   ├── cypress/                     # Cypress测试
│   │   └── playwright/                  # Playwright测试
│   └── load-tests/                      # 负载测试
│       ├── jmeter/                      # JMeter测试
│       └── k6/                          # K6测试
│
├── .github/                             # GitHub配置
│   ├── workflows/                       # CI/CD工作流
│   │   ├── ci.yml                       # 持续集成
│   │   ├── cd.yml                       # 持续部署
│   │   ├── security.yml                 # 安全扫描
│   │   └── release.yml                  # 发布流程
│   └── ISSUE_TEMPLATE/                 # Issue模板
│
├── docs/                               # 项目文档
│   ├── api/                            # API文档
│   │   ├── rest-api.md
│   │   ├── websocket-api.md
│   │   └── sdk/                        # SDK文档
│   ├── architecture/                   # 架构文档
│   │   ├── system-design.md
│   │   ├── database-design.md
│   │   └── security-design.md
│   ├── development/                    # 开发文档
│   │   ├── coding-standards.md
│   │   ├── testing-guide.md
│   │   └── contribution-guide.md
│   └── deployment/                     # 部署文档
│       ├── docker.md
│       ├── kubernetes.md
│       └── production.md
│
├── scripts/                            # 项目脚本
│   ├── build.sh                        # 构建脚本
│   ├── test.sh                         # 测试脚本
│   ├── lint.sh                         # 代码检查
│   ├── format.sh                       # 代码格式化
│   └── release.sh                      # 发布脚本
│
├── .gitignore                          # Git忽略文件
├── .gitattributes                      # Git属性
├── .editorconfig                       # 编辑器配置
├── .eslintrc.js                        # ESLint配置
├── .prettierrc                         # Prettier配置
├── .sonar-project.properties           # SonarQube配置
├── docker-compose.yml                  # Docker Compose
├── pom.xml                             # Maven根配置
├── package.json                        # Node.js根配置
├── README.md                           # 项目说明
├── LICENSE                             # 许可证
└── CHANGELOG.md                        # 变更日志
```

## 🚀 快速开始

### 环境要求

- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Redis 6.0+
- Docker & Docker Compose

### 本地开发环境搭建

1. **克隆项目**
```bash
git clone https://github.com/your-org/flowmaster.git
cd flowmaster
```

2. **启动基础设施**
```bash
cd flowmaster-infrastructure/docker
docker-compose up -d mysql redis rabbitmq
```

3. **启动后端服务**
```bash
cd flowmaster-backend
./gradlew bootRun
```

4. **启动前端应用**
```bash
cd flowmaster-frontend/flowmaster-web
npm install
npm run dev
```

5. **访问应用**
- Web端: http://localhost:3000
- API文档: http://localhost:8080/swagger-ui.html
- 管理后台: http://localhost:3000/admin

## 📖 功能模块

### 1. 流程管理
- 流程模板设计
- 流程版本管理
- 流程实例监控
- 流程性能分析

### 2. 审批管理
- 待办任务处理
- 审批历史查询
- 审批统计分析
- 批量操作支持

### 3. 用户权限
- 组织架构管理
- 角色权限配置
- 用户组管理
- 数据权限控制

### 4. 通知集成
- 多渠道通知配置
- 通知模板管理
- 通知发送记录
- 通知效果统计

### 5. 系统集成
- API接口管理
- Webhook配置
- 消息队列集成
- 第三方系统对接

### 6. 监控运维
- 系统性能监控
- 业务指标统计
- 异常告警管理
- 日志分析查询

## 🔧 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/flowmaster?useUnicode=true&characterEncoding=utf8
    username: flowmaster
    password: your_password
```

### Redis配置
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: your_password
    database: 0
```

### 消息队列配置
```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

## 📚 API文档与集成示例

### REST API 示例

#### 1. 用户认证
```bash
# 用户登录
curl -X POST "https://api.flowmaster.com/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user@example.com",
    "password": "password123"
  }'

# 响应示例
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 7200,
    "user": {
      "id": "user123",
      "username": "user@example.com",
      "name": "张三",
      "department": "技术部",
      "roles": ["APPROVER"]
    }
  }
}
```

#### 2. 流程管理
```bash
# 创建流程实例
curl -X POST "https://api.flowmaster.com/workflow/instances" \
  -H "Authorization: Bearer {accessToken}" \
  -H "Content-Type: application/json" \
  -d '{
    "processDefinitionKey": "expense_approval",
    "businessKey": "EXP-2024-001",
    "variables": {
      "applicant": "user123",
      "amount": 5000,
      "reason": "出差费用",
      "department": "技术部"
    }
  }'

# 响应示例
{
  "code": 200,
  "message": "流程实例创建成功",
  "data": {
    "processInstanceId": "proc_inst_123",
    "businessKey": "EXP-2024-001",
    "status": "RUNNING",
    "currentTasks": [
      {
        "taskId": "task_456",
        "taskName": "部门经理审批",
        "assignee": "manager001",
        "dueDate": "2024-01-15T18:00:00Z"
      }
    ]
  }
}
```

#### 3. 任务处理
```bash
# 审批任务
curl -X POST "https://api.flowmaster.com/workflow/tasks/{taskId}/complete" \
  -H "Authorization: Bearer {accessToken}" \
  -H "Content-Type: application/json" \
  -d '{
    "action": "approve",
    "comment": "同意申请",
    "variables": {
      "approved": true,
      "approvalAmount": 5000
    }
  }'

# 拒绝任务
curl -X POST "https://api.flowmaster.com/workflow/tasks/{taskId}/complete" \
  -H "Authorization: Bearer {accessToken}" \
  -H "Content-Type: application/json" \
  -d '{
    "action": "reject",
    "comment": "金额超出预算",
    "variables": {
      "approved": false,
      "rejectReason": "预算不足"
    }
  }'
```

#### 4. 查询接口
```bash
# 查询待办任务
curl -X GET "https://api.flowmaster.com/workflow/tasks?assignee=user123&status=ASSIGNED" \
  -H "Authorization: Bearer {accessToken}"

# 查询流程历史
curl -X GET "https://api.flowmaster.com/workflow/instances/{processInstanceId}/history" \
  -H "Authorization: Bearer {accessToken}"
```

### WebSocket API 示例

#### 实时通知连接
```javascript
// 建立WebSocket连接
const ws = new WebSocket('wss://api.flowmaster.com/ws/notifications?token={accessToken}');

// 监听消息
ws.onmessage = function(event) {
  const notification = JSON.parse(event.data);
  console.log('收到通知:', notification);
  
  switch(notification.type) {
    case 'TASK_ASSIGNED':
      showTaskNotification(notification.data);
      break;
    case 'PROCESS_COMPLETED':
      showProcessNotification(notification.data);
      break;
    case 'APPROVAL_REQUIRED':
      showApprovalNotification(notification.data);
      break;
  }
};

// 发送心跳
setInterval(() => {
  if (ws.readyState === WebSocket.OPEN) {
    ws.send(JSON.stringify({ type: 'PING' }));
  }
}, 30000);
```

### Java SDK 集成示例

#### 1. Maven 依赖
```xml
<dependency>
    <groupId>com.flowmaster</groupId>
    <artifactId>flowmaster-java-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 2. 客户端配置
```java
@Configuration
public class FlowMasterConfig {
    
    @Bean
    public FlowMasterClient flowMasterClient() {
        return FlowMasterClient.builder()
            .baseUrl("https://api.flowmaster.com")
            .apiKey("your-api-key")
            .timeout(Duration.ofSeconds(30))
            .retryPolicy(RetryPolicy.of(3, Duration.ofSeconds(1)))
            .build();
    }
}
```

#### 3. 业务集成示例
```java
@Service
public class ExpenseService {
    
    @Autowired
    private FlowMasterClient flowMasterClient;
    
    /**
     * 提交费用申请
     */
    public String submitExpenseRequest(ExpenseRequest request) {
        // 创建流程实例
        ProcessInstanceRequest instanceRequest = ProcessInstanceRequest.builder()
            .processDefinitionKey("expense_approval")
            .businessKey("EXP-" + System.currentTimeMillis())
            .variables(Map.of(
                "applicant", request.getApplicantId(),
                "amount", request.getAmount(),
                "reason", request.getReason(),
                "department", request.getDepartment()
            ))
            .build();
            
        ProcessInstanceResponse response = flowMasterClient
            .workflow()
            .createProcessInstance(instanceRequest);
            
        return response.getProcessInstanceId();
    }
    
    /**
     * 处理审批任务
     */
    public void handleApprovalTask(String taskId, ApprovalDecision decision) {
        TaskCompleteRequest completeRequest = TaskCompleteRequest.builder()
            .taskId(taskId)
            .action(decision.isApproved() ? "approve" : "reject")
            .comment(decision.getComment())
            .variables(Map.of(
                "approved", decision.isApproved(),
                "approvalAmount", decision.getApprovalAmount()
            ))
            .build();
            
        flowMasterClient.workflow().completeTask(completeRequest);
    }
    
    /**
     * 查询待办任务
     */
    public List<Task> getPendingTasks(String assignee) {
        TaskQueryRequest queryRequest = TaskQueryRequest.builder()
            .assignee(assignee)
            .status(TaskStatus.ASSIGNED)
            .build();
            
        return flowMasterClient.workflow().queryTasks(queryRequest);
    }
}
```

### Node.js SDK 集成示例

#### 1. 安装依赖
```bash
npm install @flowmaster/node-sdk
```

#### 2. 客户端配置
```javascript
const FlowMasterClient = require('@flowmaster/node-sdk');

const client = new FlowMasterClient({
  baseUrl: 'https://api.flowmaster.com',
  apiKey: 'your-api-key',
  timeout: 30000,
  retryPolicy: {
    maxRetries: 3,
    retryDelay: 1000
  }
});
```

#### 3. 业务集成示例
```javascript
class ExpenseService {
  constructor(flowMasterClient) {
    this.client = flowMasterClient;
  }
  
  /**
   * 提交费用申请
   */
  async submitExpenseRequest(request) {
    const instanceRequest = {
      processDefinitionKey: 'expense_approval',
      businessKey: `EXP-${Date.now()}`,
      variables: {
        applicant: request.applicantId,
        amount: request.amount,
        reason: request.reason,
        department: request.department
      }
    };
    
    const response = await this.client.workflow.createProcessInstance(instanceRequest);
    return response.processInstanceId;
  }
  
  /**
   * 处理审批任务
   */
  async handleApprovalTask(taskId, decision) {
    const completeRequest = {
      taskId: taskId,
      action: decision.approved ? 'approve' : 'reject',
      comment: decision.comment,
      variables: {
        approved: decision.approved,
        approvalAmount: decision.approvalAmount
      }
    };
    
    await this.client.workflow.completeTask(completeRequest);
  }
  
  /**
   * 监听任务分配
   */
  async listenForTaskAssignments(userId) {
    const ws = this.client.websocket.connect(`/ws/notifications?token=${this.client.token}`);
    
    ws.on('message', (data) => {
      const notification = JSON.parse(data);
      if (notification.type === 'TASK_ASSIGNED' && 
          notification.data.assignee === userId) {
        this.handleTaskAssignment(notification.data);
      }
    });
  }
}

// 使用示例
const expenseService = new ExpenseService(client);

// 提交申请
const processInstanceId = await expenseService.submitExpenseRequest({
  applicantId: 'user123',
  amount: 5000,
  reason: '出差费用',
  department: '技术部'
});

// 监听任务分配
await expenseService.listenForTaskAssignments('user123');
```

### Webhook 集成示例

#### 1. Webhook 配置
```bash
curl -X POST "https://api.flowmaster.com/webhooks" \
  -H "Authorization: Bearer {accessToken}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "expense-webhook",
    "url": "https://your-system.com/webhook/flowmaster",
    "events": ["PROCESS_COMPLETED", "TASK_ASSIGNED", "APPROVAL_REQUIRED"],
    "secret": "your-webhook-secret"
  }'
```

#### 2. Webhook 处理
```java
@RestController
@RequestMapping("/webhook")
public class FlowMasterWebhookController {
    
    @PostMapping("/flowmaster")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader("X-FlowMaster-Signature") String signature,
            @RequestBody String payload) {
        
        // 验证签名
        if (!verifySignature(signature, payload)) {
            return ResponseEntity.status(401).body("Invalid signature");
        }
        
        // 解析事件
        WebhookEvent event = JSON.parseObject(payload, WebhookEvent.class);
        
        // 处理事件
        switch (event.getType()) {
            case "PROCESS_COMPLETED":
                handleProcessCompleted(event.getData());
                break;
            case "TASK_ASSIGNED":
                handleTaskAssigned(event.getData());
                break;
            case "APPROVAL_REQUIRED":
                handleApprovalRequired(event.getData());
                break;
        }
        
        return ResponseEntity.ok("OK");
    }
    
    private void handleProcessCompleted(ProcessCompletedData data) {
        // 更新业务系统状态
        businessService.updateProcessStatus(
            data.getProcessInstanceId(), 
            data.getStatus()
        );
        
        // 发送通知
        notificationService.sendProcessCompletedNotification(data);
    }
}
```

### 错误处理示例

#### 统一错误响应格式
```json
{
  "code": 400,
  "message": "请求参数错误",
  "details": "amount字段不能为空",
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_123456789"
}
```

#### 常见错误码
- `400`: 请求参数错误
- `401`: 未授权访问
- `403`: 权限不足
- `404`: 资源不存在
- `409`: 资源冲突
- `429`: 请求频率限制
- `500`: 服务器内部错误
- `503`: 服务不可用

详细的API文档请参考：
- [REST API文档](./docs/api/rest-api.md)
- [WebSocket API文档](./docs/api/websocket-api.md)
- [SDK使用指南](./docs/sdk/)

## 📋 开发规范与最佳实践

### 代码规范

#### Java 代码规范
```java
/**
 * 流程实例服务类
 * 
 * @author FlowMaster Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
@Slf4j
@Validated
public class ProcessInstanceService {
    
    private static final String PROCESS_DEFINITION_KEY_PREFIX = "proc_def_";
    private static final int MAX_RETRY_ATTEMPTS = 3;
    
    private final ProcessInstanceRepository processInstanceRepository;
    private final WorkflowEngine workflowEngine;
    private final NotificationService notificationService;
    
    /**
     * 创建流程实例
     * 
     * @param request 流程实例创建请求
     * @return 流程实例ID
     * @throws BusinessException 业务异常
     */
    @Transactional(rollbackFor = Exception.class)
    public String createProcessInstance(@Valid ProcessInstanceRequest request) {
        try {
            // 参数验证
            validateProcessInstanceRequest(request);
            
            // 创建流程实例
            ProcessInstance instance = buildProcessInstance(request);
            ProcessInstance savedInstance = processInstanceRepository.save(instance);
            
            // 启动工作流
            String processInstanceId = workflowEngine.startProcess(
                request.getProcessDefinitionKey(),
                savedInstance.getId(),
                request.getVariables()
            );
            
            // 发送通知
            notificationService.sendProcessStartedNotification(processInstanceId);
            
            log.info("流程实例创建成功: processInstanceId={}, businessKey={}", 
                processInstanceId, request.getBusinessKey());
            
            return processInstanceId;
            
        } catch (Exception e) {
            log.error("流程实例创建失败: {}", e.getMessage(), e);
            throw new BusinessException("流程实例创建失败", e);
        }
    }
    
    /**
     * 验证流程实例请求参数
     */
    private void validateProcessInstanceRequest(ProcessInstanceRequest request) {
        Assert.notNull(request, "流程实例请求不能为空");
        Assert.hasText(request.getProcessDefinitionKey(), "流程定义Key不能为空");
        Assert.hasText(request.getBusinessKey(), "业务Key不能为空");
    }
}
```

#### 前端代码规范
```typescript
/**
 * 流程实例服务
 * @author FlowMaster Team
 * @version 1.0.0
 */

import { ProcessInstanceRequest, ProcessInstanceResponse } from '@/types/workflow';
import { ApiClient } from '@/utils/api-client';
import { Logger } from '@/utils/logger';

export class ProcessInstanceService {
  private readonly apiClient: ApiClient;
  private readonly logger: Logger;

  constructor(apiClient: ApiClient, logger: Logger) {
    this.apiClient = apiClient;
    this.logger = logger;
  }

  /**
   * 创建流程实例
   * @param request 流程实例创建请求
   * @returns Promise<ProcessInstanceResponse>
   */
  async createProcessInstance(
    request: ProcessInstanceRequest
  ): Promise<ProcessInstanceResponse> {
    try {
      this.validateRequest(request);
      
      const response = await this.apiClient.post<ProcessInstanceResponse>(
        '/workflow/instances',
        request
      );
      
      this.logger.info('流程实例创建成功', {
        processInstanceId: response.processInstanceId,
        businessKey: request.businessKey
      });
      
      return response;
    } catch (error) {
      this.logger.error('流程实例创建失败', error);
      throw error;
    }
  }

  /**
   * 验证请求参数
   */
  private validateRequest(request: ProcessInstanceRequest): void {
    if (!request) {
      throw new Error('流程实例请求不能为空');
    }
    if (!request.processDefinitionKey) {
      throw new Error('流程定义Key不能为空');
    }
    if (!request.businessKey) {
      throw new Error('业务Key不能为空');
    }
  }
}
```

### API 设计规范

#### RESTful API 设计原则
```yaml
api-design:
  naming-conventions:
    resources: plural-nouns  # /users, /processes, /tasks
    actions: verbs          # POST /users, PUT /users/{id}
    relationships: nested    # /users/{id}/processes
  
  http-methods:
    GET: 查询资源
    POST: 创建资源
    PUT: 更新整个资源
    PATCH: 部分更新资源
    DELETE: 删除资源
  
  status-codes:
    200: OK - 成功
    201: Created - 创建成功
    204: No Content - 删除成功
    400: Bad Request - 请求参数错误
    401: Unauthorized - 未授权
    403: Forbidden - 权限不足
    404: Not Found - 资源不存在
    409: Conflict - 资源冲突
    422: Unprocessable Entity - 业务逻辑错误
    500: Internal Server Error - 服务器错误
    503: Service Unavailable - 服务不可用
```

#### API 版本管理
```yaml
versioning:
  strategy: url-path  # /api/v1/users, /api/v2/users
  backward-compatibility: 2-versions
  deprecation-notice: 6-months
  
  examples:
    v1: /api/v1/workflow/instances
    v2: /api/v2/workflow/instances
```

### 数据库设计规范

#### 表命名规范
```sql
-- 表名：小写字母 + 下划线，复数形式
CREATE TABLE process_instances (
    id VARCHAR(64) PRIMARY KEY COMMENT '主键ID',
    process_definition_key VARCHAR(128) NOT NULL COMMENT '流程定义Key',
    business_key VARCHAR(128) NOT NULL COMMENT '业务Key',
    status VARCHAR(32) NOT NULL DEFAULT 'RUNNING' COMMENT '状态',
    start_user_id VARCHAR(64) NOT NULL COMMENT '启动用户ID',
    start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '启动时间',
    end_time TIMESTAMP NULL COMMENT '结束时间',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version INT NOT NULL DEFAULT 1 COMMENT '版本号',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标记',
    
    INDEX idx_process_def (process_definition_key),
    INDEX idx_business_key (business_key),
    INDEX idx_status (status),
    INDEX idx_start_user (start_user_id),
    INDEX idx_create_time (create_time),
    UNIQUE KEY uk_business_key (business_key, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程实例表';
```

#### 字段设计规范
```yaml
field-design:
  naming: snake_case
  types:
    id: VARCHAR(64)  # UUID或雪花算法ID
    status: VARCHAR(32)  # 状态枚举
    amount: DECIMAL(15,2)  # 金额字段
    time: TIMESTAMP  # 时间字段
    text: TEXT  # 长文本
    json: JSON  # JSON数据
  
  constraints:
    primary-key: 必须有主键
    foreign-key: 外键约束
    not-null: 非空约束
    default-value: 默认值
    comment: 字段注释
```

### 测试规范

#### 单元测试规范
```java
@ExtendWith(MockitoExtension.class)
class ProcessInstanceServiceTest {
    
    @Mock
    private ProcessInstanceRepository processInstanceRepository;
    
    @Mock
    private WorkflowEngine workflowEngine;
    
    @Mock
    private NotificationService notificationService;
    
    @InjectMocks
    private ProcessInstanceService processInstanceService;
    
    @Test
    @DisplayName("创建流程实例 - 成功场景")
    void createProcessInstance_Success() {
        // Given
        ProcessInstanceRequest request = ProcessInstanceRequest.builder()
            .processDefinitionKey("expense_approval")
            .businessKey("EXP-2024-001")
            .variables(Map.of("amount", 5000))
            .build();
            
        ProcessInstance savedInstance = ProcessInstance.builder()
            .id("proc_inst_123")
            .processDefinitionKey("expense_approval")
            .businessKey("EXP-2024-001")
            .build();
            
        when(processInstanceRepository.save(any(ProcessInstance.class)))
            .thenReturn(savedInstance);
        when(workflowEngine.startProcess(anyString(), anyString(), anyMap()))
            .thenReturn("proc_inst_123");
            
        // When
        String result = processInstanceService.createProcessInstance(request);
        
        // Then
        assertThat(result).isEqualTo("proc_inst_123");
        verify(processInstanceRepository).save(any(ProcessInstance.class));
        verify(workflowEngine).startProcess("expense_approval", "proc_inst_123", request.getVariables());
        verify(notificationService).sendProcessStartedNotification("proc_inst_123");
    }
    
    @Test
    @DisplayName("创建流程实例 - 参数为空异常")
    void createProcessInstance_NullRequest_ThrowsException() {
        // When & Then
        assertThatThrownBy(() -> processInstanceService.createProcessInstance(null))
            .isInstanceOf(BusinessException.class)
            .hasMessage("流程实例请求不能为空");
    }
}
```

#### 集成测试规范
```java
@SpringBootTest
@Testcontainers
class ProcessInstanceIntegrationTest {
    
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("flowmaster_test")
            .withUsername("test")
            .withPassword("test");
    
    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
            .withExposedPorts(6379);
    
    @Autowired
    private ProcessInstanceService processInstanceService;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    @Transactional
    @Rollback
    void createProcessInstance_IntegrationTest() {
        // Given
        ProcessInstanceRequest request = ProcessInstanceRequest.builder()
            .processDefinitionKey("expense_approval")
            .businessKey("EXP-2024-001")
            .variables(Map.of("amount", 5000))
            .build();
            
        // When
        String processInstanceId = processInstanceService.createProcessInstance(request);
        
        // Then
        assertThat(processInstanceId).isNotNull();
        
        // 验证数据库中的数据
        ProcessInstance instance = processInstanceRepository.findById(processInstanceId).orElse(null);
        assertThat(instance).isNotNull();
        assertThat(instance.getBusinessKey()).isEqualTo("EXP-2024-001");
    }
}
```

### 日志规范

#### 日志级别使用
```yaml
logging-levels:
  ERROR: 系统错误、异常情况
  WARN: 警告信息、潜在问题
  INFO: 重要业务流程、关键操作
  DEBUG: 调试信息、详细执行过程
  TRACE: 最详细的跟踪信息
```

#### 日志格式规范
```java
// 正确的日志记录方式
log.info("用户登录成功: userId={}, ip={}, userAgent={}", 
    userId, ipAddress, userAgent);

log.error("流程实例创建失败: processDefinitionKey={}, businessKey={}, error={}", 
    processDefinitionKey, businessKey, e.getMessage(), e);

// 避免的日志记录方式
log.info("用户登录成功: " + userId);  // 字符串拼接
log.error("流程实例创建失败", e);  // 缺少上下文信息
```

### 异常处理规范

#### 异常分类
```java
// 业务异常
public class BusinessException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;
    
    public BusinessException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

// 系统异常
public class SystemException extends RuntimeException {
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}

// 参数异常
public class ParameterException extends BusinessException {
    public ParameterException(String message) {
        super("PARAMETER_ERROR", message);
    }
}
```

#### 全局异常处理
```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ResponseEntity.badRequest()
            .body(ErrorResponse.of(e.getErrorCode(), e.getMessage()));
    }
    
    @ExceptionHandler(ParameterException.class)
    public ResponseEntity<ErrorResponse> handleParameterException(ParameterException e) {
        log.warn("参数异常: {}", e.getMessage());
        return ResponseEntity.badRequest()
            .body(ErrorResponse.of("PARAMETER_ERROR", e.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("系统异常", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.of("SYSTEM_ERROR", "系统内部错误"));
    }
}
```

### 代码审查规范

#### 审查检查清单
```yaml
code-review:
  functionality:
    - 代码是否实现了预期功能
    - 边界条件是否处理正确
    - 异常情况是否考虑周全
  
  code-quality:
    - 代码是否遵循命名规范
    - 是否有重复代码
    - 方法是否过于复杂
    - 注释是否充分
  
  security:
    - 是否有SQL注入风险
    - 是否有XSS漏洞
    - 敏感信息是否加密
    - 权限控制是否正确
  
  performance:
    - 是否有性能问题
    - 数据库查询是否优化
    - 是否有内存泄漏风险
    - 缓存使用是否合理
  
  testing:
    - 单元测试覆盖率是否达标
    - 测试用例是否充分
    - 集成测试是否通过
    - 性能测试是否通过
```

### Git 提交规范

#### 提交信息格式
```
<type>(<scope>): <subject>

<body>

<footer>
```

#### 提交类型
```yaml
commit-types:
  feat: 新功能
  fix: 修复bug
  docs: 文档更新
  style: 代码格式调整
  refactor: 代码重构
  test: 测试相关
  chore: 构建过程或辅助工具的变动
  perf: 性能优化
  ci: CI/CD相关
  build: 构建系统相关
```

#### 提交示例
```
feat(workflow): 添加流程实例批量创建功能

- 支持批量创建流程实例
- 添加批量操作的事务处理
- 优化批量操作的性能

Closes #123
```

## 📊 监控告警与日志管理

### 监控体系架构

#### 监控层次
```yaml
monitoring-layers:
  infrastructure:
    - CPU使用率
    - 内存使用率
    - 磁盘I/O
    - 网络流量
    - 容器资源
  
  application:
    - JVM指标
    - 应用性能
    - 业务指标
    - 错误率
    - 响应时间
  
  business:
    - 流程实例数量
    - 审批完成率
    - 用户活跃度
    - 系统使用率
```

#### 监控技术栈
```yaml
monitoring-stack:
  metrics-collection: Micrometer + Prometheus
  metrics-storage: Prometheus + InfluxDB
  visualization: Grafana
  alerting: AlertManager + PagerDuty
  logging: ELK Stack (Elasticsearch + Logstash + Kibana)
  tracing: Jaeger + Zipkin
  apm: New Relic / DataDog (可选)
```

### 关键指标监控

#### 系统指标
```yaml
system-metrics:
  cpu:
    threshold: 80%
    alert-level: warning
    critical-threshold: 95%
  
  memory:
    threshold: 85%
    alert-level: warning
    critical-threshold: 95%
  
  disk:
    threshold: 80%
    alert-level: warning
    critical-threshold: 90%
  
  network:
    bandwidth-threshold: 80%
    packet-loss-threshold: 1%
```

#### 应用指标
```yaml
application-metrics:
  jvm:
    heap-usage: 80%
    gc-pause-time: 100ms
    thread-count: 500
  
  database:
    connection-pool-usage: 80%
    query-response-time: 100ms
    slow-query-count: 10/min
  
  cache:
    hit-ratio: 90%
    eviction-rate: 5%
    memory-usage: 80%
  
  api:
    response-time-p95: 300ms
    error-rate: 1%
    throughput: 1000/min
```

#### 业务指标
```yaml
business-metrics:
  workflow:
    process-instance-count: 10000
    task-completion-rate: 95%
    average-process-time: 2h
  
  user:
    active-users: 1000/day
    login-success-rate: 99%
    session-duration: 30min
  
  notification:
    delivery-rate: 98%
    delivery-time: 5s
    failure-rate: 2%
```

### 告警配置

#### 告警规则
```yaml
alert-rules:
  critical:
    - name: "High CPU Usage"
      condition: "cpu_usage > 95%"
      duration: "2m"
      severity: "critical"
      notification: ["email", "sms", "slack"]
    
    - name: "Database Connection Pool Exhausted"
      condition: "db_connection_pool_usage > 95%"
      duration: "1m"
      severity: "critical"
      notification: ["email", "sms"]
    
    - name: "High Error Rate"
      condition: "api_error_rate > 5%"
      duration: "3m"
      severity: "critical"
      notification: ["email", "sms", "pagerduty"]
  
  warning:
    - name: "High Memory Usage"
      condition: "memory_usage > 85%"
      duration: "5m"
      severity: "warning"
      notification: ["email", "slack"]
    
    - name: "Slow API Response"
      condition: "api_response_time_p95 > 500ms"
      duration: "5m"
      severity: "warning"
      notification: ["email"]
```

#### 告警通知配置
```yaml
notification-channels:
  email:
    smtp-server: "smtp.company.com"
    from: "alerts@flowmaster.com"
    recipients:
      - "devops@company.com"
      - "oncall@company.com"
  
  sms:
    provider: "twilio"
    recipients:
      - "+1234567890"
  
  slack:
    webhook-url: "https://hooks.slack.com/services/xxx"
    channel: "#alerts"
  
  pagerduty:
    integration-key: "your-integration-key"
    escalation-policy: "flowmaster-oncall"
```

### 日志管理

#### 日志分类
```yaml
log-categories:
  application:
    level: INFO
    format: JSON
    fields: [timestamp, level, logger, message, thread, mdc]
  
  access:
    level: INFO
    format: JSON
    fields: [timestamp, method, uri, status, response-time, ip, user-agent]
  
  audit:
    level: INFO
    format: JSON
    fields: [timestamp, user-id, action, resource, result, ip]
  
  security:
    level: WARN
    format: JSON
    fields: [timestamp, event-type, user-id, ip, details]
  
  performance:
    level: DEBUG
    format: JSON
    fields: [timestamp, operation, duration, memory, cpu]
```

#### 日志配置
```yaml
logging-config:
  appenders:
    console:
      type: Console
      pattern: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    
    file:
      type: RollingFile
      file: "logs/flowmaster.log"
      max-file-size: 100MB
      max-history: 30
      total-size-cap: 10GB
    
    elasticsearch:
      type: Elasticsearch
      hosts: ["elasticsearch:9200"]
      index: "flowmaster-logs"
      type: "log"
  
  loggers:
    root:
      level: INFO
      appenders: [console, file, elasticsearch]
    
    com.flowmaster:
      level: DEBUG
      appenders: [console, file, elasticsearch]
    
    org.springframework:
      level: WARN
      appenders: [console, file]
    
    org.flowable:
      level: INFO
      appenders: [console, file]
```

### 链路追踪

#### 分布式追踪配置
```yaml
tracing-config:
  jaeger:
    endpoint: "http://jaeger:14268/api/traces"
    sampler-type: "probabilistic"
    sampler-param: 0.1
  
  zipkin:
    endpoint: "http://zipkin:9411/api/v2/spans"
    sample-rate: 0.1
  
  custom-spans:
    - name: "workflow-process"
      tags: [process-definition-key, business-key, status]
    - name: "task-execution"
      tags: [task-id, assignee, action]
    - name: "notification-send"
      tags: [channel, recipient, status]
```

#### 追踪示例
```java
@Service
public class ProcessInstanceService {
    
    @Autowired
    private Tracer tracer;
    
    public String createProcessInstance(ProcessInstanceRequest request) {
        Span span = tracer.nextSpan()
            .name("create-process-instance")
            .tag("process-definition-key", request.getProcessDefinitionKey())
            .tag("business-key", request.getBusinessKey())
            .start();
            
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            // 业务逻辑
            String processInstanceId = doCreateProcessInstance(request);
            
            span.tag("process-instance-id", processInstanceId);
            span.tag("status", "success");
            
            return processInstanceId;
        } catch (Exception e) {
            span.tag("error", true);
            span.tag("error.message", e.getMessage());
            throw e;
        } finally {
            span.end();
        }
    }
}
```

### 性能监控

#### APM 配置
```yaml
apm-config:
  newrelic:
    license-key: "your-license-key"
    app-name: "FlowMaster"
    environment: "production"
  
  datadog:
    api-key: "your-api-key"
    service-name: "flowmaster"
    environment: "production"
  
  custom-metrics:
    - name: "workflow.process.count"
      type: "counter"
      description: "流程实例创建数量"
    
    - name: "workflow.task.duration"
      type: "histogram"
      description: "任务执行时间"
    
    - name: "workflow.approval.rate"
      type: "gauge"
      description: "审批通过率"
```

### 健康检查

#### 健康检查端点
```yaml
health-checks:
  liveness:
    path: "/actuator/health/liveness"
    checks:
      - database
      - redis
      - kafka
  
  readiness:
    path: "/actuator/health/readiness"
    checks:
      - database
      - redis
      - kafka
      - external-services
  
  custom-checks:
    - name: "workflow-engine"
      class: "WorkflowEngineHealthIndicator"
    - name: "notification-service"
      class: "NotificationServiceHealthIndicator"
```

#### 健康检查实现
```java
@Component
public class WorkflowEngineHealthIndicator implements HealthIndicator {
    
    @Autowired
    private WorkflowEngine workflowEngine;
    
    @Override
    public Health health() {
        try {
            // 检查工作流引擎状态
            boolean isHealthy = workflowEngine.isHealthy();
            
            if (isHealthy) {
                return Health.up()
                    .withDetail("engine", "flowable")
                    .withDetail("version", workflowEngine.getVersion())
                    .build();
            } else {
                return Health.down()
                    .withDetail("engine", "flowable")
                    .withDetail("error", "Engine not responding")
                    .build();
            }
        } catch (Exception e) {
            return Health.down()
                .withDetail("engine", "flowable")
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}
```

### 监控仪表板

#### Grafana 仪表板配置
```yaml
grafana-dashboards:
  system-overview:
    panels:
      - title: "CPU Usage"
        type: "graph"
        query: "rate(cpu_usage[5m])"
      
      - title: "Memory Usage"
        type: "graph"
        query: "rate(memory_usage[5m])"
      
      - title: "Disk I/O"
        type: "graph"
        query: "rate(disk_io[5m])"
  
  application-metrics:
    panels:
      - title: "API Response Time"
        type: "graph"
        query: "histogram_quantile(0.95, api_response_time)"
      
      - title: "Error Rate"
        type: "graph"
        query: "rate(api_errors[5m]) / rate(api_requests[5m])"
      
      - title: "Throughput"
        type: "graph"
        query: "rate(api_requests[5m])"
  
  business-metrics:
    panels:
      - title: "Process Instances"
        type: "graph"
        query: "workflow_process_instances"
      
      - title: "Task Completion Rate"
        type: "graph"
        query: "workflow_task_completion_rate"
      
      - title: "Active Users"
        type: "graph"
        query: "active_users"
```

### 日志分析

#### 日志查询示例
```yaml
log-queries:
  error-analysis:
    query: "level:ERROR AND timestamp:[now-1h TO now]"
    fields: ["timestamp", "logger", "message", "exception"]
  
  performance-analysis:
    query: "operation:workflow AND duration:>1000"
    fields: ["timestamp", "operation", "duration", "process-definition-key"]
  
  security-events:
    query: "event-type:security AND level:WARN"
    fields: ["timestamp", "event-type", "user-id", "ip", "details"]
  
  user-activity:
    query: "action:login AND timestamp:[now-1d TO now]"
    fields: ["timestamp", "user-id", "ip", "user-agent", "result"]
```

## 🚀 部署与运维指南

### 环境规划

#### 环境分类
```yaml
environments:
  development:
    purpose: "开发环境"
    instances: 1
    resources: "2C4G"
    data-retention: "7天"
    backup: "每日"
  
  testing:
    purpose: "测试环境"
    instances: 2
    resources: "4C8G"
    data-retention: "30天"
    backup: "每日"
  
  staging:
    purpose: "预生产环境"
    instances: 3
    resources: "8C16G"
    data-retention: "90天"
    backup: "每日"
  
  production:
    purpose: "生产环境"
    instances: 5
    resources: "16C32G"
    data-retention: "7年"
    backup: "每小时"
```

#### 资源规划
```yaml
resource-planning:
  production:
    api-gateway:
      replicas: 3
      resources:
        requests: "2C4G"
        limits: "4C8G"
    
    workflow-service:
      replicas: 5
      resources:
        requests: "4C8G"
        limits: "8C16G"
    
    database:
      mysql:
        master: "8C16G"
        slaves: ["4C8G", "4C8G"]
      redis:
        cluster: ["2C4G", "2C4G", "2C4G"]
    
    storage:
      mysql-data: "500GB"
      redis-data: "100GB"
      logs: "1TB"
      backups: "2TB"
```

### Docker 部署

#### Docker Compose 配置
```yaml
# docker-compose.yml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_DATABASE: flowmaster
      MYSQL_USER: flowmaster
      MYSQL_PASSWORD: ${MYSQL_PASSWORD}
    volumes:
      - mysql_data:/var/lib/mysql
      - ./config/mysql/my.cnf:/etc/mysql/conf.d/my.cnf
    ports:
      - "3306:3306"
    networks:
      - flowmaster-network

  redis:
    image: redis:7-alpine
    command: redis-server --appendonly yes --requirepass ${REDIS_PASSWORD}
    volumes:
      - redis_data:/data
    ports:
      - "6379:6379"
    networks:
      - flowmaster-network

  kafka:
    image: confluentinc/cp-kafka:latest
    environment:
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    ports:
      - "9092:9092"
    networks:
      - flowmaster-network

  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    networks:
      - flowmaster-network

  flowmaster-gateway:
    image: flowmaster/gateway:latest
    environment:
      SPRING_PROFILES_ACTIVE: docker
      MYSQL_HOST: mysql
      REDIS_HOST: redis
      KAFKA_HOST: kafka
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
      - kafka
    networks:
      - flowmaster-network

  flowmaster-workflow:
    image: flowmaster/workflow:latest
    environment:
      SPRING_PROFILES_ACTIVE: docker
      MYSQL_HOST: mysql
      REDIS_HOST: redis
      KAFKA_HOST: kafka
    depends_on:
      - mysql
      - redis
      - kafka
    networks:
      - flowmaster-network

volumes:
  mysql_data:
  redis_data:

networks:
  flowmaster-network:
    driver: bridge
```

#### Dockerfile 示例
```dockerfile
# 后端服务 Dockerfile
FROM openjdk:17-jre-slim

# 设置工作目录
WORKDIR /app

# 复制应用jar包
COPY target/flowmaster-workflow-*.jar app.jar

# 创建非root用户
RUN groupadd -r flowmaster && useradd -r -g flowmaster flowmaster
RUN chown -R flowmaster:flowmaster /app
USER flowmaster

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Kubernetes 部署

#### Namespace 配置
```yaml
# namespace.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: flowmaster
  labels:
    name: flowmaster
    environment: production
```

#### ConfigMap 配置
```yaml
# configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: flowmaster-config
  namespace: flowmaster
data:
  application.yml: |
    spring:
      datasource:
        url: jdbc:mysql://mysql-service:3306/flowmaster
        username: flowmaster
        password: ${MYSQL_PASSWORD}
      redis:
        host: redis-service
        port: 6379
        password: ${REDIS_PASSWORD}
      kafka:
        bootstrap-servers: kafka-service:9092
    
    management:
      endpoints:
        web:
          exposure:
            include: health,info,metrics,prometheus
      endpoint:
        health:
          show-details: always
```

#### Secret 配置
```yaml
# secret.yaml
apiVersion: v1
kind: Secret
metadata:
  name: flowmaster-secrets
  namespace: flowmaster
type: Opaque
data:
  mysql-password: <base64-encoded-password>
  redis-password: <base64-encoded-password>
  jwt-secret: <base64-encoded-secret>
```

#### Deployment 配置
```yaml
# deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: flowmaster-workflow
  namespace: flowmaster
spec:
  replicas: 3
  selector:
    matchLabels:
      app: flowmaster-workflow
  template:
    metadata:
      labels:
        app: flowmaster-workflow
    spec:
      containers:
      - name: workflow-service
        image: flowmaster/workflow:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "k8s"
        - name: MYSQL_PASSWORD
          valueFrom:
            secretKeyRef:
              name: flowmaster-secrets
              key: mysql-password
        - name: REDIS_PASSWORD
          valueFrom:
            secretKeyRef:
              name: flowmaster-secrets
              key: redis-password
        resources:
          requests:
            memory: "2Gi"
            cpu: "1000m"
          limits:
            memory: "4Gi"
            cpu: "2000m"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        volumeMounts:
        - name: config-volume
          mountPath: /app/config
      volumes:
      - name: config-volume
        configMap:
          name: flowmaster-config
```

#### Service 配置
```yaml
# service.yaml
apiVersion: v1
kind: Service
metadata:
  name: flowmaster-workflow-service
  namespace: flowmaster
spec:
  selector:
    app: flowmaster-workflow
  ports:
  - port: 8080
    targetPort: 8080
    protocol: TCP
  type: ClusterIP
```

#### Ingress 配置
```yaml
# ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: flowmaster-ingress
  namespace: flowmaster
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
    cert-manager.io/cluster-issuer: "letsencrypt-prod"
spec:
  tls:
  - hosts:
    - api.flowmaster.com
    secretName: flowmaster-tls
  rules:
  - host: api.flowmaster.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: flowmaster-gateway-service
            port:
              number: 8080
```

### Helm 部署

#### Chart 结构
```
flowmaster/
├── Chart.yaml
├── values.yaml
├── values-dev.yaml
├── values-prod.yaml
├── templates/
│   ├── deployment.yaml
│   ├── service.yaml
│   ├── ingress.yaml
│   ├── configmap.yaml
│   ├── secret.yaml
│   └── hpa.yaml
└── charts/
```

#### Chart.yaml
```yaml
apiVersion: v2
name: flowmaster
description: FlowMaster Enterprise Workflow Platform
type: application
version: 1.0.0
appVersion: "1.0.0"
keywords:
  - workflow
  - approval
  - bpm
home: https://github.com/your-org/flowmaster
sources:
  - https://github.com/your-org/flowmaster
maintainers:
  - name: FlowMaster Team
    email: team@flowmaster.com
```

#### values.yaml
```yaml
# 全局配置
global:
  imageRegistry: "registry.company.com"
  imagePullSecrets: ["regcred"]
  storageClass: "fast-ssd"

# 镜像配置
image:
  repository: flowmaster/workflow
  tag: "1.0.0"
  pullPolicy: IfNotPresent

# 副本数配置
replicaCount: 3

# 服务配置
service:
  type: ClusterIP
  port: 8080

# 资源限制
resources:
  limits:
    cpu: 2000m
    memory: 4Gi
  requests:
    cpu: 1000m
    memory: 2Gi

# 自动扩缩容
autoscaling:
  enabled: true
  minReplicas: 3
  maxReplicas: 10
  targetCPUUtilizationPercentage: 70
  targetMemoryUtilizationPercentage: 80

# 数据库配置
database:
  host: mysql-service
  port: 3306
  name: flowmaster
  username: flowmaster

# Redis配置
redis:
  host: redis-service
  port: 6379

# 监控配置
monitoring:
  enabled: true
  serviceMonitor:
    enabled: true
    interval: 30s
```

### CI/CD 流水线

#### GitLab CI 配置
```yaml
# .gitlab-ci.yml
stages:
  - build
  - test
  - security
  - package
  - deploy

variables:
  DOCKER_REGISTRY: "registry.company.com"
  IMAGE_NAME: "flowmaster"

# 构建阶段
build:
  stage: build
  image: maven:3.8-openjdk-17
  script:
    - mvn clean compile
  artifacts:
    paths:
      - target/
    expire_in: 1 hour

# 测试阶段
test:
  stage: test
  image: maven:3.8-openjdk-17
  services:
    - mysql:8.0
    - redis:7-alpine
  variables:
    MYSQL_DATABASE: flowmaster_test
    MYSQL_ROOT_PASSWORD: test
  script:
    - mvn test
    - mvn jacoco:report
  artifacts:
    reports:
      junit:
        - target/surefire-reports/TEST-*.xml
      coverage_report:
        coverage_format: jacoco
        path: target/site/jacoco/jacoco.xml
  coverage: '/Total.*?([0-9]{1,3})%/'

# 安全扫描
security:
  stage: security
  image: securecodewarrior/docker-security-scan:latest
  script:
    - docker build -t $IMAGE_NAME:$CI_COMMIT_SHA .
    - docker run --rm -v /var/run/docker.sock:/var/run/docker.sock $IMAGE_NAME:$CI_COMMIT_SHA
  only:
    - main
    - develop

# 打包阶段
package:
  stage: package
  image: docker:latest
  services:
    - docker:dind
  script:
    - docker build -t $DOCKER_REGISTRY/$IMAGE_NAME:$CI_COMMIT_SHA .
    - docker push $DOCKER_REGISTRY/$IMAGE_NAME:$CI_COMMIT_SHA
    - docker tag $DOCKER_REGISTRY/$IMAGE_NAME:$CI_COMMIT_SHA $DOCKER_REGISTRY/$IMAGE_NAME:latest
    - docker push $DOCKER_REGISTRY/$IMAGE_NAME:latest
  only:
    - main
    - develop

# 部署到开发环境
deploy-dev:
  stage: deploy
  image: bitnami/kubectl:latest
  script:
    - kubectl config use-context dev-cluster
    - helm upgrade --install flowmaster ./helm/flowmaster -f ./helm/flowmaster/values-dev.yaml --set image.tag=$CI_COMMIT_SHA
  environment:
    name: development
    url: https://dev-api.flowmaster.com
  only:
    - develop

# 部署到生产环境
deploy-prod:
  stage: deploy
  image: bitnami/kubectl:latest
  script:
    - kubectl config use-context prod-cluster
    - helm upgrade --install flowmaster ./helm/flowmaster -f ./helm/flowmaster/values-prod.yaml --set image.tag=$CI_COMMIT_SHA
  environment:
    name: production
    url: https://api.flowmaster.com
  when: manual
  only:
    - main
```

### 备份与恢复

#### 数据库备份
```bash
#!/bin/bash
# backup-mysql.sh

# 配置
DB_HOST="mysql-service"
DB_NAME="flowmaster"
DB_USER="flowmaster"
BACKUP_DIR="/backup/mysql"
DATE=$(date +%Y%m%d_%H%M%S)

# 创建备份目录
mkdir -p $BACKUP_DIR

# 全量备份
mysqldump -h $DB_HOST -u $DB_USER -p$DB_PASSWORD \
  --single-transaction \
  --routines \
  --triggers \
  --all-databases > $BACKUP_DIR/full_backup_$DATE.sql

# 压缩备份文件
gzip $BACKUP_DIR/full_backup_$DATE.sql

# 删除7天前的备份
find $BACKUP_DIR -name "full_backup_*.sql.gz" -mtime +7 -delete

echo "MySQL backup completed: full_backup_$DATE.sql.gz"
```

#### Redis 备份
```bash
#!/bin/bash
# backup-redis.sh

REDIS_HOST="redis-service"
BACKUP_DIR="/backup/redis"
DATE=$(date +%Y%m%d_%H%M%S)

mkdir -p $BACKUP_DIR

# Redis RDB 备份
redis-cli -h $REDIS_HOST --rdb $BACKUP_DIR/redis_backup_$DATE.rdb

# 压缩备份文件
gzip $BACKUP_DIR/redis_backup_$DATE.rdb

# 删除7天前的备份
find $BACKUP_DIR -name "redis_backup_*.rdb.gz" -mtime +7 -delete

echo "Redis backup completed: redis_backup_$DATE.rdb.gz"
```

#### 应用数据备份
```bash
#!/bin/bash
# backup-app-data.sh

APP_DATA_DIR="/app/data"
BACKUP_DIR="/backup/app"
DATE=$(date +%Y%m%d_%H%M%S)

mkdir -p $BACKUP_DIR

# 备份应用数据
tar -czf $BACKUP_DIR/app_data_$DATE.tar.gz -C $APP_DATA_DIR .

# 删除30天前的备份
find $BACKUP_DIR -name "app_data_*.tar.gz" -mtime +30 -delete

echo "Application data backup completed: app_data_$DATE.tar.gz"
```

### 监控与告警

#### Prometheus 配置
```yaml
# prometheus.yml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

rule_files:
  - "flowmaster_rules.yml"

scrape_configs:
  - job_name: 'flowmaster-services'
    kubernetes_sd_configs:
      - role: endpoints
        namespaces:
          names:
            - flowmaster
    relabel_configs:
      - source_labels: [__meta_kubernetes_service_annotation_prometheus_io_scrape]
        action: keep
        regex: true
      - source_labels: [__meta_kubernetes_service_annotation_prometheus_io_path]
        action: replace
        target_label: __metrics_path__
        regex: (.+)

alerting:
  alertmanagers:
    - static_configs:
        - targets:
          - alertmanager:9093
```

#### AlertManager 配置
```yaml
# alertmanager.yml
global:
  smtp_smarthost: 'smtp.company.com:587'
  smtp_from: 'alerts@flowmaster.com'

route:
  group_by: ['alertname']
  group_wait: 10s
  group_interval: 10s
  repeat_interval: 1h
  receiver: 'web.hook'
  routes:
  - match:
      severity: critical
    receiver: 'critical-alerts'
  - match:
      severity: warning
    receiver: 'warning-alerts'

receivers:
- name: 'web.hook'
  webhook_configs:
  - url: 'http://webhook:5001/'

- name: 'critical-alerts'
  email_configs:
  - to: 'oncall@company.com'
    subject: 'FlowMaster Critical Alert: {{ .GroupLabels.alertname }}'
    body: |
      {{ range .Alerts }}
      Alert: {{ .Annotations.summary }}
      Description: {{ .Annotations.description }}
      {{ end }}
  slack_configs:
  - api_url: 'https://hooks.slack.com/services/xxx'
    channel: '#alerts'
    title: 'FlowMaster Critical Alert'
    text: '{{ range .Alerts }}{{ .Annotations.summary }}{{ end }}'

- name: 'warning-alerts'
  email_configs:
  - to: 'devops@company.com'
    subject: 'FlowMaster Warning Alert: {{ .GroupLabels.alertname }}'
```

### 故障处理

#### 常见故障处理
```yaml
troubleshooting:
  database-connection:
    symptoms: "数据库连接超时"
    causes: ["连接池耗尽", "网络问题", "数据库服务异常"]
    solutions:
      - "检查连接池配置"
      - "重启数据库服务"
      - "检查网络连接"
  
  memory-leak:
    symptoms: "内存使用率持续上升"
    causes: ["对象未释放", "缓存未清理", "线程泄漏"]
    solutions:
      - "分析堆转储文件"
      - "检查缓存配置"
      - "重启应用服务"
  
  performance-degradation:
    symptoms: "响应时间变慢"
    causes: ["数据库慢查询", "缓存失效", "资源不足"]
    solutions:
      - "优化数据库查询"
      - "检查缓存命中率"
      - "扩容服务实例"
```

#### 应急响应流程
```yaml
incident-response:
  severity-levels:
    p1: "系统完全不可用"
    p2: "核心功能受影响"
    p3: "部分功能异常"
    p4: "性能问题"
  
  response-times:
    p1: "15分钟内响应"
    p2: "1小时内响应"
    p3: "4小时内响应"
    p4: "24小时内响应"
  
  escalation:
    - level: "L1 - 一线支持"
      responsibility: "初步诊断和基础处理"
      contact: "oncall@company.com"
    
    - level: "L2 - 二线支持"
      responsibility: "深度分析和问题解决"
      contact: "senior-dev@company.com"
    
    - level: "L3 - 三线支持"
      responsibility: "架构级问题处理"
      contact: "architect@company.com"
```

## 🧪 测试

### 单元测试
```bash
./gradlew test
```

### 集成测试
```bash
./gradlew integrationTest
```

### 前端测试
```bash
cd flowmaster-frontend/flowmaster-web
npm run test
```

### 端到端测试
```bash
cd flowmaster-tests/e2e-tests/cypress
npm run e2e:test
```

### 负载测试
```bash
cd flowmaster-tests/load-tests/k6
k6 run load-test.js
```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 [MIT License](./LICENSE) 许可证。

## 📞 联系我们

- 项目主页: https://github.com/your-org/flowmaster
- 问题反馈: https://github.com/your-org/flowmaster/issues
- 邮箱: flowmaster@your-org.com
- 文档: https://docs.flowmaster.com

## 🙏 致谢

感谢以下开源项目的支持：
- [Flowable](https://www.flowable.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [React](https://reactjs.org/)
- [Ant Design](https://ant.design/)

---

**FlowMaster** - 让审批流程更智能，让企业管理更高效！
