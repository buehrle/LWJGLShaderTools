package at.erdlof.shadertools.shaders;

public class InvalidShaderException extends RuntimeException {
	private static final long serialVersionUID = -5763888197093737102L;

	public InvalidShaderException(String message) {
		super(message);
	}
}
