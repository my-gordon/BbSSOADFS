package polyu.learn.bbssoadfs.operation.sis;

public class SISException extends Exception{

	private static final long serialVersionUID = -3925191516853819944L;

	public SISException(String message) {
        super(message);
    }

    public SISException(Throwable cause) {
        super(cause);
    }

    public SISException(String message, Throwable cause) {
        super(message, cause);
    }
}
