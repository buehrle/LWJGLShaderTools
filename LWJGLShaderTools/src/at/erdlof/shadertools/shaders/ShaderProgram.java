package at.erdlof.shadertools.shaders;

import static org.lwjgl.opengl.ARBShaderObjects.glAttachObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glCreateProgramObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glDeleteObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glDetachObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glGetUniformLocationARB;
import static org.lwjgl.opengl.ARBShaderObjects.glLinkProgramARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUniform1fARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUniform1iARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUniform2fARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUniform2iARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUniform3fARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUniform3iARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUniform4fARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUniform4iARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUseProgramObjectARB;
import static org.lwjgl.opengl.ARBVertexShader.glGetAttribLocationARB;

import java.util.HashMap;

/**
 * A shader program to manage a vertex and a fragment shader.
 * <p>
 * Usage:
 * <br>
 * {@link #bind() Bind} the shader before rendering the object(s) on which the shader should be applied.
 * {@link #unbind() Unbind} it afterwards. Only one shader program can be used simultaneously.
 * @author Florian Bührle
 */
public class ShaderProgram implements Validable {
	private final int shaderProgramID;
	private final VertexShader vertexShader;
	private final FragmentShader fragmentShader;
	private HashMap<String, Integer> registeredUniforms;
	private boolean valid;
	
	/**
	 * @param vertexShader the vertex shader to use
	 * @param fragmentShader the fragment shader to use
	 * @throws InvalidShaderException if one or both of the passed shaders are invalid
	 */
	public ShaderProgram(VertexShader vertexShader, FragmentShader fragmentShader) {
		if (!vertexShader.isValid() || !fragmentShader.isValid()) {
			throw new InvalidShaderException("One or both of the passed shaders are invalid.");
		}
		
		shaderProgramID = glCreateProgramObjectARB();
		this.vertexShader = vertexShader;
		this.fragmentShader = fragmentShader;
		
		registeredUniforms = new HashMap<String, Integer>();
		
		glAttachObjectARB(shaderProgramID, this.vertexShader.getShaderID());
		glAttachObjectARB(shaderProgramID, this.fragmentShader.getShaderID());
		
		glLinkProgramARB(shaderProgramID);
		
		this.valid = true;
	}
	
//	/**
//	 * Checks if the used OpenGL layer supports shaders.
//	 * @return true if shaders are supported.
//	 */
//	public static boolean isSupported() {
//		ContextCapabilities c = GLContext.getCapabilities();
//		
//		return c.GL_ARB_shader_objects && c.GL_ARB_vertex_shader && c.GL_ARB_fragment_shader;
//	}
	
	/**
	 * Binds this shader. This means, that every render action after
	 * using this method will be performed with passing this shader program.
	 * @throws InvalidProgramException if the program has been released.
	 */
	public void bind() {
		if (!valid) throw new InvalidProgramException("The program has been released and cannot longer be used.");
		glUseProgramObjectARB(shaderProgramID);
	}
	
	/**
	 * Invokes {@link #unbindAll() unbindAll()}
	 */
	public void unbind() {
		unbindAll();
	}
	
	/**
	 * Unbinds all shaders. This means, that every render action after
	 * using this method will be performed without passing any shaders.
	 */
	public static void unbindAll() {
		glUseProgramObjectARB(0);
	}
	
	/**
	 * Unbinds the shader and releases all resources. This makes the program useless.
	 * Both vertex and fragment shaders will be destroyed and cannot longer be used.
	 */
	public void releaseAll() {
		releaseProgram();
		vertexShader.releaseShader();
		fragmentShader.releaseShader();
	}
	
	/**
	 * Unbinds the shader and releases all resources, except the vertex and the fragment shader. This makes the program useless.
	 * Both vertex and fragment shaders are not affected and can be reused to create another shader program.
	 * This method is automatically invoked by the {@link #finalize() finalize()} method of this class.
	 */
	public void releaseProgram() {
		unbind();
		
		glDetachObjectARB(shaderProgramID, vertexShader.getShaderID());
		glDetachObjectARB(shaderProgramID, fragmentShader.getShaderID());
		glDeleteObjectARB(shaderProgramID);
		
		this.valid = false;
	}
	
	@Override
	public void finalize() throws Throwable {
		releaseProgram();
		super.finalize();
	}
	
	/**
	 * Returns the validation state of the program. A program that has been created without an exception is always valid.
	 * However, if the program is {@link #releaseProgram() released}, the validation state is permanently set to false.
	 * @return true if the program is valid, false otherwise
	 */
	@Override
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Adds a uniform variable to the shader.
	 * @param identifier the identifier of the uniform variable
	 * 
	 */
	public void registerUniform(String identifier) {
		if (!registeredUniforms.containsKey(identifier)) {
			int uniformID = glGetUniformLocationARB(shaderProgramID, identifier);
			
			registeredUniforms.put(identifier, uniformID);
		}
	}
	
	/**
	 * Sets the uniform variable to a simple float. You have to {@link #registerUniform(String) register} the uniform variable first.
	 * @param identifier The name of the variable
	 * @param v The data
	 */
	public void setUniform(String identifier, float v) {
		if (registeredUniforms.containsKey(identifier)) {
			int uniformID = registeredUniforms.get(identifier);
			
			glUniform1fARB(uniformID, v);
		}
	}
	
	/**
	 * Sets the uniform variable to a simple integer. You have to {@link #registerUniform(String) register} the uniform variable first.
	 * @param identifier The name of the variable
	 * @param v The data
	 */
	public void setUniform(String identifier, int v) {
		if (registeredUniforms.containsKey(identifier)) {
			int uniformID = registeredUniforms.get(identifier);
			
			glUniform1iARB(uniformID, v);
		}
	}
	
	/**
	 * Sets the uniform variable to a 2-dimensional float vector. You have to {@link #registerUniform(String) register} the uniform variable first.
	 * @param identifier The name of the variable
	 * @param x The x-component of the vector
	 * @param y The y-component of the vector
	 */
	public void setUniform(String identifier, float x, float y) {
		if (registeredUniforms.containsKey(identifier)) {
			int uniformID = registeredUniforms.get(identifier);
			
			glUniform2fARB(uniformID, x, y);
		}
	}
	
	/**
	 * Sets the uniform variable to a 2-dimensional integer vector. You have to {@link #registerUniform(String) register} the uniform variable first.
	 * @param identifier The name of the variable
	 * @param x The x-component of the vector
	 * @param y The y-component of the vector
	 */
	public void setUniform(String identifier, int x, int y) {
		if (registeredUniforms.containsKey(identifier)) {
			int uniformID = registeredUniforms.get(identifier);
			
			glUniform2iARB(uniformID, x, y);
		}
	}
	
	/**
	 * Sets the uniform variable to a 3-dimensional float vector. You have to {@link #registerUniform(String) register} the uniform variable first.
	 * @param identifier The name of the variable
	 * @param x The x-component of the vector
	 * @param y The y-component of the vector
	 * @param z The z-component of the vector
	 */
	public void setUniform(String identifier, float x, float y, float z) {
		if (registeredUniforms.containsKey(identifier)) {
			int uniformID = registeredUniforms.get(identifier);
			
			glUniform3fARB(uniformID, x, y, z);
		}
	}
	
	/**
	 * Sets the uniform variable to a 3-dimensional integer vector. You have to {@link #registerUniform(String) register} the uniform variable first.
	 * @param identifier The name of the variable
	 * @param x The x-component of the vector
	 * @param y The y-component of the vector
	 * @param z The z-component of the vector
	 */
	public void setUniform(String identifier, int x, int y, int z) {
		if (registeredUniforms.containsKey(identifier)) {
			int uniformID = registeredUniforms.get(identifier);
			
			glUniform3iARB(uniformID, x, y, z);
		}
	}
	
	/**
	 * Sets the uniform variable to a 4-dimensional float vector. You have to {@link #registerUniform(String) register} the uniform variable first.
	 * @param identifier The name of the variable
	 * @param x The x-component of the vector
	 * @param y The y-component of the vector
	 * @param z The z-component of the vector
	 * @param w The w-component of the vector
	 */
	public void setUniform(String identifier, float x, float y, float z, float w) {
		if (registeredUniforms.containsKey(identifier)) {
			int uniformID = registeredUniforms.get(identifier);
			
			glUniform4fARB(uniformID, x, y, z, w);
		}
	}
	
	/**
	 * Sets the uniform variable to a 4-dimensional integer vector. You have to {@link #registerUniform(String) register} the uniform variable first.
	 * @param identifier The name of the variable
	 * @param x The x-component of the vector
	 * @param y The y-component of the vector
	 * @param z The z-component of the vector
	 * @param w The w-component of the vector
	 */
	public void setUniform(String identifier, int x, int y, int z, int w) {
		if (registeredUniforms.containsKey(identifier)) {
			int uniformID = registeredUniforms.get(identifier);
			
			glUniform4iARB(uniformID, x, y, z, w);
		}
	}
	
	public int registerAttribute(String identifier) {
		return glGetAttribLocationARB(shaderProgramID, identifier);
	}
}
