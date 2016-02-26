package at.erdlof.shadertools.shaders;

import static org.lwjgl.opengl.EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glDeleteFramebuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferTexture2DEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenFramebuffersEXT;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT_BIT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_BASE_LEVEL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GLContext;

/**
 * A basic wrapper for OpenGL-FBOs.
 * @author Florian Bührle
 * @deprecated Not finished, heavy bugs.
 */
public class FrameBuffer {
	private int id;
	private int texture;
	private int width, height;
	
	/**
	 * Creates a new FBO.
	 * @throws RuntimeException If the system doesn't support FBOs. See {@link #isSupported() here}.
	 */
	public FrameBuffer(int width, int height, ByteBuffer pixels) {
		if (!isSupported()) throw new RuntimeException("This system doesn't support FBOs.");
		
		this.width = width;
		this.height = height;
		
		//Generate the texture this FBO draws on
		texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

		//Generate the FBO
		id = glGenFramebuffersEXT();
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, id);
		//Bind the texture to the FBO
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, texture, 0);
		
		//Unbind the FBO for later use
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
	}
	
	public FrameBuffer(int width, int height) {
		this(width, height, null);
	}
	
	/**
	 * Binds this FBO, sets a viewport and clears the FBO. You can render everything
	 * you want after that call and it will be stored in the {@link #getTextureID() texture}.
	 */
	public void bind() {
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, id);
		
		glPushAttrib(GL_VIEWPORT_BIT);
		glViewport(0, 0, width, height);
		glClear(GL_COLOR_BUFFER_BIT);
	}
	
	public void unbind() {
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		glPopAttrib();
	}
	
	public void release() {
		glDeleteFramebuffersEXT(id);
		// TODO release texture
	}
	
	/**
	 * @return the texture ID
	 */
	public int getTextureID() {
		return texture;
	}
	
	/**
	 * @return the ID of the FBO
	 */
	public int getFBOid() {
		return id;
	}
	
	/**
	 * Checks if the systems supports FBOs, which are needed for multiple shader passes.
	 * @return true if FBOs are supported, false otherwise
	 */
	public static boolean isSupported() {
		return GLContext.getCapabilities().GL_EXT_framebuffer_object;
	}
}
