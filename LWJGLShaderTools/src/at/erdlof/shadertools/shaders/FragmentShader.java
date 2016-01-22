package at.erdlof.shadertools.shaders;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;

public class FragmentShader extends Shader {
	private int fragmentShaderID;
	
	public FragmentShader(String code) {
		super(code);
		
		fragmentShaderID = ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		ARBShaderObjects.glShaderSourceARB(fragmentShaderID, getProgramCode());
		ARBShaderObjects.glCompileShaderARB(fragmentShaderID);
	}
	
	@Override
	public int getShaderID() {
		return fragmentShaderID;
	}

}
