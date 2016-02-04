package at.erdlof.shadertools.shaders;

import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.ARBVertexShader.*;

/**
 * @author Florian Bührle
 */
public class VertexShader extends Shader {
	private int vertexShaderID;
	
	public VertexShader(String programCode) {
		super(programCode);
		
		vertexShaderID = glCreateShaderObjectARB(GL_VERTEX_SHADER_ARB);
		glShaderSourceARB(vertexShaderID, getProgramCode());
		glCompileShaderARB(vertexShaderID);
		super.validate();
	}
	
	@Override
	public int getShaderID() {
		return vertexShaderID;
	}

}
