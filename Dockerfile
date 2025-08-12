# Используем JDK 21
FROM eclipse-temurin:21-jdk

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем и собираем проект
COPY . .

# Собираем jar-файл
RUN ./mvnw clean package -DskipTests

# Запускаем приложение
CMD sh -c "java -jar target/*.jar"
