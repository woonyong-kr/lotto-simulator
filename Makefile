.PHONY: up re down clean logs status

_build:
	@./gradlew build -x test
	@DOCKER_BUILDKIT=1 docker-compose build

up:
	@DB_VOLUME_ENABLED=$$(grep DB_VOLUME_ENABLED .env | cut -d '=' -f2); \
	$(MAKE) _build; \
	if [ "$$DB_VOLUME_ENABLED" = "false" ]; then \
		docker-compose up -d; \
	else \
		docker-compose -f docker-compose.yml -f docker-compose.override.yml up -d; \
	fi

re:
	@DB_VOLUME_ENABLED=$$(grep DB_VOLUME_ENABLED .env | cut -d '=' -f2); \
	$(MAKE) _build; \
	if [ "$$DB_VOLUME_ENABLED" = "false" ]; then \
		docker-compose up -d --force-recreate; \
	else \
		docker-compose -f docker-compose.yml -f docker-compose.override.yml up -d --force-recreate; \
	fi

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
	@echo "DB Volume: $$(grep DB_VOLUME_ENABLED .env | cut -d '=' -f2)"