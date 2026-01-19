package com.practice.leetcode.interview;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║                    INFRASTRUCTURE INTERVIEW Q&A                            ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
public class InfrastructureQA {

  // ═══════════════════════════════════════════════════════════════════════════
  // DOCKER
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Container vs Virtual Machine?                                            │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                     │ Container           │ VM                              │
   * │ Virtualization      │ OS-level            │ Hardware-level                  │
   * │ Size                │ MBs                 │ GBs                             │
   * │ Startup             │ Seconds             │ Minutes                         │
   * │ Isolation           │ Process             │ Full OS                         │
   * │ Performance         │ Near-native         │ Overhead                        │
   * │ Kernel              │ Shared              │ Separate                        │
   * │                                                                             │
   * │ Container: Microservices, CI/CD, portable apps                              │
   * │ VM: Strong isolation, different OS, legacy apps                             │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Dockerfile basic commands?                                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ FROM      - Base image                                                      │
   * │ RUN       - Execute command (creates layer)                                 │
   * │ COPY      - Copy files from host                                            │
   * │ ADD       - Like COPY + URL support + auto-extract                          │
   * │ WORKDIR   - Set working directory                                           │
   * │ ENV       - Set environment variable                                        │
   * │ EXPOSE    - Document port (doesn't publish)                                 │
   * │ CMD       - Default command (can be overridden)                             │
   * │ ENTRYPOINT- Executable (args appended)                                      │
   * │                                                                             │
   * │ BEST PRACTICES:                                                             │
   * │ - Use multi-stage builds (giảm image size)                                  │
   * │ - Minimize layers (combine RUN commands)                                    │
   * │ - Use .dockerignore                                                         │
   * │ - Don't run as root                                                         │
   * │ - Use specific tags (không dùng :latest)                                    │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // KUBERNETES
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Kubernetes core concepts?                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ POD: Smallest deployable unit, 1+ containers, shared network/storage        │
   * │ DEPLOYMENT: Manages ReplicaSets, rolling updates, rollbacks                 │
   * │ SERVICE: Stable network endpoint cho pods                                   │
   * │   - ClusterIP: Internal only                                                │
   * │   - NodePort: External via node port                                        │
   * │   - LoadBalancer: External via cloud LB                                     │
   * │ INGRESS: HTTP(S) routing, TLS, virtual hosts                                │
   * │ CONFIGMAP: Configuration data                                               │
   * │ SECRET: Sensitive data (base64 encoded, not encrypted!)                     │
   * │ NAMESPACE: Virtual cluster, resource isolation                              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: How K8s maintains desired state?                                         │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ DECLARATIVE: Bạn define desired state trong YAML                            │
   * │ CONTROL LOOP: Controllers continuously compare actual vs desired            │
   * │                                                                             │
   * │ EXAMPLE:                                                                    │
   * │ spec.replicas: 3                                                            │
   * │ → Actual: 2 pods running                                                    │
   * │ → Controller: "Need 1 more, creating..."                                    │
   * │ → Actual: 3 pods running ✓                                                  │
   * │                                                                             │
   * │ SELF-HEALING: Pod dies → Controller creates new one                         │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CI/CD
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: CI vs CD?                                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ CI (Continuous Integration):                                                │
   * │ - Merge code frequently (multiple times/day)                                │
   * │ - Automated build và tests                                                  │
   * │ - Fast feedback loop                                                        │
   * │                                                                             │
   * │ CD (Continuous Delivery):                                                   │
   * │ - Every commit is DEPLOYABLE                                                │
   * │ - Deploy to staging automatically                                           │
   * │ - Production deploy is MANUAL approval                                      │
   * │                                                                             │
   * │ CD (Continuous Deployment):                                                 │
   * │ - Every commit goes to PRODUCTION automatically                             │
   * │ - Requires high test coverage và monitoring                                 │
   * │                                                                             │
   * │ TYPICAL PIPELINE:                                                           │
   * │ Commit → Build → Unit Test → Integration Test → Deploy Staging              │
   * │ → Acceptance Test → Deploy Production → Smoke Test                          │
   * │                                                                             │
   * │ TOOLS: Jenkins, GitLab CI, GitHub Actions, CircleCI, ArgoCD                 │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // CLOUD CONCEPTS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: IaaS vs PaaS vs SaaS?                                                    │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ IaaS (Infrastructure as a Service):                                         │
   * │ - Bạn manage: OS, runtime, app, data                                        │
   * │ - Provider manage: Virtualization, servers, storage, network                │
   * │ - Ví dụ: AWS EC2, Azure VMs, GCP Compute Engine                             │
   * │                                                                             │
   * │ PaaS (Platform as a Service):                                               │
   * │ - Bạn manage: App, data                                                     │
   * │ - Provider manage: OS, runtime, middleware                                  │
   * │ - Ví dụ: Heroku, AWS Elastic Beanstalk, Google App Engine                   │
   * │                                                                             │
   * │ SaaS (Software as a Service):                                               │
   * │ - Bạn chỉ use, không manage gì                                              │
   * │ - Ví dụ: Gmail, Salesforce, Slack                                           │
   * │                                                                             │
   * │ SERVERLESS (FaaS):                                                          │
   * │ - Chỉ deploy function, pay per execution                                    │
   * │ - AWS Lambda, Azure Functions, Google Cloud Functions                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // MONITORING & LOGGING
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Observability - Logs vs Metrics vs Traces?                               │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ LOGS: What happened (events, errors)                                        │
   * │ - Text, unstructured or structured (JSON)                                   │
   * │ - ELK Stack (Elasticsearch, Logstash, Kibana), Loki                         │
   * │                                                                             │
   * │ METRICS: How system is performing (numbers over time)                       │
   * │ - CPU, memory, request count, latency                                       │
   * │ - Prometheus + Grafana, Datadog                                             │
   * │                                                                             │
   * │ TRACES: Request flow across services                                        │
   * │ - Distributed tracing cho microservices                                     │
   * │ - Jaeger, Zipkin, AWS X-Ray                                                 │
   * │                                                                             │
   * │ ALERTING: Trigger when thresholds crossed                                   │
   * │ - PagerDuty, OpsGenie, Prometheus Alertmanager                              │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // MESSAGE QUEUES
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Kafka vs RabbitMQ?                                                       │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │                     │ Kafka               │ RabbitMQ                        │
   * │ Model               │ Log-based           │ Queue-based                     │
   * │ Message retention   │ Configurable time   │ Until consumed                  │
   * │ Consumer groups     │ ✓ (partitions)      │ Limited                         │
   * │ Throughput          │ Very high           │ Lower                           │
   * │ Message ordering    │ Per partition       │ Per queue                       │
   * │ Replay messages     │ ✓                   │ ✗                               │
   * │                                                                             │
   * │ Kafka: Event streaming, high throughput, log aggregation                    │
   * │ RabbitMQ: Task queues, complex routing, lower latency                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Why use Message Queues?                                                  │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ DECOUPLING: Producers không cần biết consumers                              │
   * │ ASYNC PROCESSING: Không block waiting for response                          │
   * │ LOAD LEVELING: Buffer requests during spikes                                │
   * │ RELIABILITY: Persist messages, retry on failure                             │
   * │ SCALABILITY: Add more consumers khi cần                                     │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */

  // ═══════════════════════════════════════════════════════════════════════════
  // MICROSERVICES PATTERNS
  // ═══════════════════════════════════════════════════════════════════════════
  /**
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Circuit Breaker Pattern?                                                 │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ Prevent cascading failures khi service downstream fail                      │
   * │                                                                             │
   * │ STATES:                                                                     │
   * │ CLOSED: Normal operation, requests go through                               │
   * │ OPEN: Too many failures, fail fast without calling service                  │
   * │ HALF-OPEN: Test với limited requests, decide to close or stay open          │
   * │                                                                             │
   * │ IMPLEMENTATION: Resilience4j, Hystrix (deprecated), Spring Cloud            │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Service Discovery?                                                       │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ How services find each other trong dynamic environment                      │
   * │                                                                             │
   * │ CLIENT-SIDE: Client query registry, choose instance                         │
   * │ - Ví dụ: Netflix Eureka, Consul                                             │
   * │                                                                             │
   * │ SERVER-SIDE: Query router/load balancer                                     │
   * │ - Ví dụ: Kubernetes Services, AWS ELB                                       │
   * └─────────────────────────────────────────────────────────────────────────────┘
   *
   * ┌─────────────────────────────────────────────────────────────────────────────┐
   * │ Q: Other important patterns?                                                │
   * ├─────────────────────────────────────────────────────────────────────────────┤
   * │ API GATEWAY: Single entry point, routing, auth, rate limiting               │
   * │ SAGA: Distributed transactions across services                              │
   * │ EVENT SOURCING: Store events, rebuild state from events                     │
   * │ CQRS: Separate read and write models                                        │
   * │ SIDECAR: Helper container alongside main container                          │
   * │ BULKHEAD: Isolate resources to prevent failure propagation                  │
   * └─────────────────────────────────────────────────────────────────────────────┘
   */
}
