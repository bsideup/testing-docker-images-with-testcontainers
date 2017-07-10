# Testing Docker images with Testcontainers

## Quick start

If you have Groovy on your machine:
```
$ groovy testcontainers.groovy
```

If you don't have Groovy on your machine:
```
$ docker-compose -f docker-compose.tests.yml run --rm tests
```