package at.erdlof.shadertools.shaders;

import static org.lwjgl.opengl.ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.glDeleteObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glGetInfoLogARB;
import static org.lwjgl.opengl.ARBShaderObjects.glGetObjectParameteriARB;
import static org.lwjgl.opengl.GL11.GL_FALSE;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Florian Bührle
 */
public abstract class Shader implements Validable {
	private final String programCode;
	private int shaderID;
	private boolean valid;
	
	public Shader(String programCode) {
		this.programCode = programCode;
		this.valid = false;
	}
	
	/**
	 * Gets shader code, either vertex or fragment, from a file.
	 * @param fileLocation The location of the shader code file
	 * @return Shader code
	 * @throws IOException
	 */
	public static String codeFromFile(String fileLocation) throws IOException {
		StringBuilder builder = new StringBuilder();
		String line;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileLocation)));
		
		while((line = reader.readLine()) != null) builder.append(line).append("\n");
		reader.close();
		
		return builder.toString();
	}
	
	/**
	 * Validates the shader.
	 * @throws InvalidShaderException if the shader is invalid
	 */
	protected void validate() {
		if(glGetObjectParameteriARB(getShaderID(), GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE) {
			throw new InvalidShaderException("Error while validating shader: " + getLog());
		}
		
		this.valid = true;
	}
	
	/**
	 * Gets the current state of the shader. This can be useful for debugging shaders.
	 * @return the log
	 */
	public String getLog() {
		if(shaderID == 0) return "";
		return glGetInfoLogARB(shaderID, glGetObjectParameteriARB(shaderID, GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
	
	public String getProgramCode() {
		return programCode;
	}
	
	public int getShaderID() {
		return shaderID;
	}
	
	protected void setShaderID(int shaderID) {
		this.shaderID = shaderID;
	}
	
	@Override
	public void finalize() throws Throwable {
		releaseShader();
		super.finalize();
	}
	
	/**
	 * Returns the validation state of the shader. A shader that has been created without an exception is always valid.
	 * However, if the shader is {@link #releaseShader() released}, the validation state is permanently set to false.
	 * This will make the shader useless.
	 * @return true if the shader is valid, false otherwise
	 */
	@Override
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Releases all resources taken by this shader component. DO NOT use this before releasing your shader program!
	 * This method is automatically invoked by the releaseAll() method of ShaderProgram as well as the {@link #finalize() finalize()} method of this class.
	 */
	public void releaseShader() {
		glDeleteObjectARB(shaderID);
		this.valid = false;
	}
}
