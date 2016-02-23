package at.erdlof.shadertools.shaders;

import static org.lwjgl.opengl.ARBShaderObjects.glCompileShaderARB;
import static org.lwjgl.opengl.ARBShaderObjects.glCreateShaderObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glShaderSourceARB;
import static org.lwjgl.opengl.ARBVertexShader.GL_VERTEX_SHADER_ARB;

/**
 * @author Florian Bührle
 */
public class VertexShader extends Shader {
	public VertexShader(String programCode) {
		super(programCode);
		
		setShaderID(glCreateShaderObjectARB(GL_VERTEX_SHADER_ARB));
		glShaderSourceARB(getShaderID(), getProgramCode());
		glCompileShaderARB(getShaderID());
		super.validate();
	}
}
