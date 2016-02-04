package at.erdlof.shadertools.shaders;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.ARBShaderObjects.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Florian Bührle
 */
public abstract class Shader {
	private String programCode;

	public Shader(String programCode) {
		this.programCode = programCode;
	}
	
	public static String codeFromFile(String fileLocation) throws IOException {
		StringBuilder builder = new StringBuilder();
		String line;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileLocation)));
		
		while((line = reader.readLine()) != null) builder.append(line).append("\n");
		reader.close();
		
		return builder.toString();
	}
	
	protected void validate() {
		if(glGetObjectParameteriARB(getShaderID(), GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE) {
			throw new RuntimeException("Error while creating shader: " + getLog(getShaderID()));
		}
	}
	
	private String getLog(int shaderID) {
		if(shaderID == 0) return "";
		return glGetInfoLogARB(shaderID, glGetObjectParameteriARB(shaderID, GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
	
	protected String getProgramCode() {
		return programCode;
	}
	
	public abstract int getShaderID();
}
