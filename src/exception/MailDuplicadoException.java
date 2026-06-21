package exception;

public class MailDuplicadoException extends Exception {
    public MailDuplicadoException(String mensaje) {
        super(mensaje);
    }
}