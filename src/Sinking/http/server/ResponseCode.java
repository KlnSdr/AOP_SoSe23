package Sinking.http.server;

public enum ResponseCode {
    SUCCESS(200), CREATED(201), NO_CONTENT(204), BAD_REQUEST(400), PAYMENT_REQUIRED(402), FORBIDDEN(403), NOT_FOUND(404), IM_A_TEAPOT(418), UNPROCESSABLE_ENTITY(422), INTERNAL_ERROR(500);

    private final int value;

    ResponseCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
