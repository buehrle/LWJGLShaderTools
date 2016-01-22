package at.erdlof.shadertools.shaders;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderProgram {
	private int shaderProgramID;
	
	public ShaderProgram(VertexShader vertexShader, FragmentShader fragmentShader) {
		shaderProgramID = ARBShaderObjects.glCreateProgramObjectARB();
		ARBShaderObjects.glAttachObjectARB(shaderProgramID, vertexShader.getShaderID());
		ARBShaderObjects.glAttachObjectARB(shaderProgramID, fragmentShader.getShaderID());
		
		ARBShaderObjects.glLinkProgramARB(shaderProgramID);
	}
	
	public void bindShader() {
		ARBShaderObjects.glUseProgramObjectARB(shaderProgramID);
		GL11.glBegin(GL11.GL_QUADS);
	}
	
	public void releaseShader() {
		GL11.glEnd();
		ARBShaderObjects.glUseProgramObjectARB(0);
	}
	
	public int registerUniform(String identifier) {
		return GL20.glGetUniformLocation(shaderProgramID, identifier);
	}
}
