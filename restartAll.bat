call mvn clean install
call docker-compose down --remove-orphans
call docker-compose up --build -d
