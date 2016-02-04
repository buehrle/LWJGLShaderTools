package at.erdlof.shadertools.shaders;

import static org.lwjgl.opengl.ARBFragmentShader.*;
import static org.lwjgl.opengl.ARBShaderObjects.*;

/**
 * @author Florian Bührle
 */
public class FragmentShader extends Shader {
	private int fragmentShaderID;
	
	public FragmentShader(String programCode) {
		super(programCode);
		
		fragmentShaderID = glCreateShaderObjectARB(GL_FRAGMENT_SHADER_ARB);
		glShaderSourceARB(fragmentShaderID, getProgramCode());
		glCompileShaderARB(fragmentShaderID);
		super.validate();
	}
	
	@Override
	public int getShaderID() {
		return fragmentShaderID;
	}

}
