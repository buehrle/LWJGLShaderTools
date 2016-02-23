package at.erdlof.shadertools.shaders;

import static org.lwjgl.opengl.ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.glCompileShaderARB;
import static org.lwjgl.opengl.ARBShaderObjects.glCreateShaderObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glShaderSourceARB;

/**
 * @author Florian Bührle
 */
public class FragmentShader extends Shader {
	public FragmentShader(String programCode) {
		super(programCode);
		
		setShaderID(glCreateShaderObjectARB(GL_FRAGMENT_SHADER_ARB));
		glShaderSourceARB(getShaderID(), getProgramCode());
		glCompileShaderARB(getShaderID());
		super.validate();
	}
}
