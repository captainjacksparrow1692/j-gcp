package uzumtech.j_gcp.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {

     // Ключи для кэширования в Redis
     public static final String USER_CACHE = "users";
     public static final String USER_STATS_CACHE = "user_stats";

     // Названия топиков Kafka
     public static final String USER_TOPIC = "users_topic";
     public static final String USER_DLT_TOPIC = "users_topic-DLT";

     // Форматы дат (если понадобятся для кастомной сериализации)
     public static final String DATE_FORMAT = "yyyy-MM-dd";

     // Сообщения об ошибках (опционально)
     public static final String USER_NOT_FOUND = "User not found with id: ";
}