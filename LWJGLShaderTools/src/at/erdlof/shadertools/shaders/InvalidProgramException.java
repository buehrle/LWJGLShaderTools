package at.erdlof.shadertools.shaders;

public class InvalidProgramException extends RuntimeException {
	private static final long serialVersionUID = -7047340793431827384L;

	public InvalidProgramException(String message) {
		super(message);
	}
}
