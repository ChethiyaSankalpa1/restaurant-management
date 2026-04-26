@echo off
cd /d "C:\Users\ACER\Desktop\New folder\restaurant-management"
set CLASSPATH=.;src\main\java;src\main\resources
java -Dspring.config.location=src/main/resources/application.properties -cp "%CLASSPATH%" com.restaurant.RestaurantManagementApplication
pause
