call docker stop logstash
call docker rm logstash
call docker-compose up --build -d logstash
call docker logs logstash -f
