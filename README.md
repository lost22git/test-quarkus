
## Requirement

install graalvm jdk-21

```shell
sdk install java 21-graalce
```


## build static linking binary

```shell
./gradlew clean && ./gradlew native -Pstatic -x test
```
