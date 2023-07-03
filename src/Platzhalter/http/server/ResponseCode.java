package Platzhalter.http.server;

public enum ResponseCode {
    SUCCESS(200), NO_CONTENT(204), FORBIDDEN(403), NOT_FOUND(404), INTERNAL_ERROR(500);

    private final int value;

    ResponseCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
