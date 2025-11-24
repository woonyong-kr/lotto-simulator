.PHONY: up re down clean logs status

_build:
	@./gradlew :lotto-core:build
	@./gradlew build -x test
	@DOCKER_BUILDKIT=1 docker-compose build

up:
	@$(MAKE) _build
	@docker-compose up -d

re:
	@$(MAKE) _build
	@docker-compose up -d --force-recreate

down:
	@docker-compose down

clean:
	@docker-compose down -v
	@docker volume prune -f
	@docker system prune -f

logs:
	@docker-compose logs -f

status:
	@docker-compose ps
	@echo "JPA DDL: $$(grep JPA_DDL_AUTO .env | cut -d '=' -f2)"