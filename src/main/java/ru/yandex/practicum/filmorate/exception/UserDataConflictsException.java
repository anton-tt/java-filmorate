package ru.yandex.practicum.filmorate.exception;

public class UserDataConflictsException extends RuntimeException {
    public UserDataConflictsException(final String message) {
        super(message);
    }

}