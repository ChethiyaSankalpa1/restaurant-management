@echo off
cd /d "C:\Users\ACER\Desktop\New folder\restaurant-management"
echo Building minimal JAR...
jar cvf restaurant-app.jar -C target\classes .
echo Starting application...
java -jar restaurant-app.jar --spring.config.location=src/main/resources/application.properties
pause
