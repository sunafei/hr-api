FROM java:8

# VOLUME  ["/home/upload", "/home/upload"]
RUN mkdir /hr
COPY hr-api-1.0.jar /hr/hr.jar
# 添加字体
COPY /home/simsun.ttf /usr/share/fonts/simsun.ttf

# 设置镜像的时区,避免出现8小时的误差
ENV TZ=Asia/Shanghai

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/hr/hr.jar", "--spring.profiles.active=prod"]

EXPOSE 8091