package at.erdlof.shadertools.shaders;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;

public class VertexShader extends Shader {
	private int vertexShaderID;
	
	public VertexShader(String code) {
		super(code);
		
		vertexShaderID = ARBShaderObjects.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
		ARBShaderObjects.glShaderSourceARB(vertexShaderID, getProgramCode());
		ARBShaderObjects.glCompileShaderARB(vertexShaderID);
	}
	
	@Override
	public int getShaderID() {
		return vertexShaderID;
	}

}
