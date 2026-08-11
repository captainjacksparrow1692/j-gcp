package j_gcp.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {

     // Ключи для кэширования в Redis
     public static final String USER_CACHE = "users";
     // Названия топиков Kafka
     public static final String USER_TOPIC = "users_topic";
     // Сообщения об ошибках (опционально)
     public static final String USER_NOT_FOUND = "User not found with id: ";
}