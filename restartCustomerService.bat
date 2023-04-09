call mvn clean install
call docker stop customer-service
call docker rm customer-service
call docker-compose up --build -d customer-service
call docker logs customer-service -f
