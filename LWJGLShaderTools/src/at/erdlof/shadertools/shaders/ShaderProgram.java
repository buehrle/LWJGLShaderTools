package at.erdlof.shadertools.shaders;

import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.ARBVertexShader.*;

import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;

/**
 * @author Florian Bührle
 */
public class ShaderProgram {
	private final int shaderProgramID;
	private final int vertexShaderID;
	private final int fragmentShaderID;

	public ShaderProgram(VertexShader vertexShader, FragmentShader fragmentShader) {
		shaderProgramID = glCreateProgramObjectARB();
		vertexShaderID = vertexShader.getShaderID();
		fragmentShaderID = fragmentShader.getShaderID();
		
		glAttachObjectARB(shaderProgramID, vertexShaderID);
		glAttachObjectARB(shaderProgramID, fragmentShaderID);
		
		glLinkProgramARB(shaderProgramID);
	}
	
	/**
	 * Checks if the used OpenGL layer supports shaders.
	 * @return true if shaders are supported.
	 */
	public static boolean isSupported() {
		ContextCapabilities c = GLContext.getCapabilities();
		
		return c.GL_ARB_shader_objects && c.GL_ARB_vertex_shader && c.GL_ARB_fragment_shader;
	}
	
	public void bindShader() {
		glUseProgramObjectARB(shaderProgramID);
	}
	
	public void unbindShader() {
		unbindAllShaders();
	}
	
	public static void unbindAllShaders() {
		glUseProgramObjectARB(0);
	}
	
	/**
	 * Unbinds the shader and releases all resources. Can only be used once and makes the shader useless.
	 * This method is automatically invoked by the finalize() method of this object.
	 */
	public void releaseShader() {
		unbindShader();
		
		glDetachObjectARB(shaderProgramID, vertexShaderID);
		glDeleteObjectARB(vertexShaderID);
		
		glDetachObjectARB(shaderProgramID, fragmentShaderID);
		glDeleteObjectARB(fragmentShaderID);
		
		glDeleteObjectARB(shaderProgramID);
	}
	
	@Override
	public void finalize() throws Throwable {
		releaseShader();
		super.finalize();
	}
	
	/**
	 * @param identifier the identifier of the uniform variable
	 * @return the ID of the uniform variable to store data, usable with the ARBShaderObjects uniform methods
	 */
	public int registerUniform(String identifier) {
		return glGetUniformLocationARB(shaderProgramID, identifier);
	}
	
	public int registerAttribute(String identifier) {
		return glGetAttribLocationARB(shaderProgramID, identifier);
	}
}
