package at.erdlof.shadertools.shaders;

public abstract class Shader {
	private String programCode;
	
	public Shader(String code) {
		programCode = code;
	}
	
	protected String getProgramCode() {
		return programCode;
	}
	
	public abstract int getShaderID();
}
