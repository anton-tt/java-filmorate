package ru.yandex.practicum.filmorate.exception;

public class FilmDataConflictsException extends RuntimeException {
    public FilmDataConflictsException(final String message) {
        super(message);
    }

}