package uzumtech.j_gcp.constant.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Error {
    // 1xxx: System & Protocol Errors
    INTERNAL_SERVICE_ERROR(1000, "Непредвиденная внутренняя ошибка", HttpStatus.INTERNAL_SERVER_ERROR),
    METHOD_NOT_ALLOWED(1001, "HTTP метод не поддерживается", HttpStatus.METHOD_NOT_ALLOWED),
    JSON_PARSE_ERROR(1002, "Ошибка чтения JSON", HttpStatus.BAD_REQUEST),
    SERVICE_UNAVAILABLE(1003, "Сервис временно недоступен", HttpStatus.SERVICE_UNAVAILABLE),

    // 2xxx: Validation Errors
    VALIDATION_FAILED(2000, "Общая ошибка валидации", HttpStatus.BAD_REQUEST),
    INVALID_FULL_NAME(2001, "Некорректный формат ФИО", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT(2002, "Неверный формат почты", HttpStatus.BAD_REQUEST),
    INVALID_PHONE_NUMBER(2003, "Номер телефона не соответствует маске", HttpStatus.BAD_REQUEST),
    INVALID_PINFL(2004, "ПИНФЛ должен содержать ровно 14 цифр", HttpStatus.BAD_REQUEST),
    INVALID_AGE_RANGE(2005, "Возраст вне допустимого диапазона (0–150)", HttpStatus.BAD_REQUEST),
    INVALID_DATE_SEQUENCE(2006, "Дата выдачи документа позже даты истечения", HttpStatus.BAD_REQUEST),

    // 3xxx: User Business Logic
    USER_NOT_FOUND(3000, "Пользователь с таким ID не существует", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(3001, "Пользователь с таким ПИНФЛ/Email уже в базе", HttpStatus.CONFLICT),
    USER_ALREADY_DECEASED(3002, "Попытка повторно зарегистрировать смерть", HttpStatus.CONFLICT),
    INVALID_DEATH_DATE(3003, "Дата смерти указана в будущем или раньше даты рождения", HttpStatus.BAD_REQUEST),
    DOCUMENT_EXPIRED(3004, "Срок действия документа истек", HttpStatus.GONE),
    INCOMPLETE_PROFILE(3005, "Профиль не может быть активирован из-за пустых полей", HttpStatus.UNPROCESSABLE_ENTITY),

    // 4xxx: Security & Access
    UNAUTHORIZED(4000, "Требуется авторизация", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(4001, "Недостаточно прав для выполнения действия", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(4002, "Токен доступа недействителен или просрочен", HttpStatus.UNAUTHORIZED),
    SENSITIVE_DATA_ACCESS_ERROR(4003, "Попытка получить доступ к защищенным данным без ключа", HttpStatus.FORBIDDEN),

    // 5xxx: Infrastructure & External Services
    DATABASE_CONNECTION_ERROR(5000, "Ошибка связи с PostgreSQL", HttpStatus.SERVICE_UNAVAILABLE),
    REDIS_CONNECTION_ERROR(5001, "Кэш недоступен", HttpStatus.SERVICE_UNAVAILABLE),
    EXTERNAL_SERVICE_UNAVAILABLE(5002, "Внешний сервис не отвечает", HttpStatus.BAD_GATEWAY),
    FILE_STORAGE_ERROR(5003, "Ошибка при загрузке фото в облако", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    Error(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}