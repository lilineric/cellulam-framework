# cellulam-framework
It is a Java-based framework to develop microservices.

## Spring
See [cellulam-spring-boot-project](https://github.com/lilineric/cellulam-spring-boot-project)

## Notes
### RuntimeContext
If you need to pass context in the thread pool, need to use `com.cellulam.core.concurrent.TtlExecutors` instead of `java.util.concurrent.Executors`

See more: [transmittable-thread-local](https://github.com/alibaba/transmittable-thread-local)

## TODO
- [x] Distributed UID
- [ ] Distributed Transaction
    - [ ] TCC
    - [ ] Saga
    - [ ] Transaction Table
    - [ ] Transaction Message

