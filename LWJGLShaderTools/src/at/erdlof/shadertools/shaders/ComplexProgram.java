package at.erdlof.shadertools.shaders;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 * This class manages the rendering process of one sequence, if more than one programs should be applied. (Multiple shader passes)
 * @author Florian Bührle
 * @deprecated Currently not working :x
 */
public class ComplexProgram {
	private ShaderProgram[] programs;
	
	/**
	 * Creates a new ComplexProgram.
	 * @param programs The shader programs to be applied. You have to pass at least 2.
	 * @throws RuntimeException If < 2 programs are passed.
	 * @throws InvalidProgramException If one or more of the passed programs are invalid.
	 * @throws NullPointerException If one or more of the passed programs are null.
	 */
	public ComplexProgram(ShaderProgram... programs) {
		//if (programs.length < 2) throw new RuntimeException("You have to pass at least 2 programs.");
		
		for (ShaderProgram program : programs) {
			if (!program.isValid()) throw new InvalidProgramException("All passed programs must be valid!");
		}
		
		this.programs = programs;
	}
	
	/**
	 * Draw a specific sequence using all shader programs. Example:
	 * <p><code>
	 * complexProgram.use(() -> { <br>
	 * <span style="color: gray; font-style: italic">-- Stuff to be drawn --</span><br>
	 * }); </code>
	 * <p>
	 * Do not use shaders or any other modifications of the rendering in this sequence.
	 * @param sequence The rendering commands to be executed passing multiple programs.
	 * @throws RuntimeException If the initialization of the FBO fails.
	 */
	public void use(ComplexSequence sequence) {
		FrameBuffer basic = new FrameBuffer(Display.getWidth(), Display.getHeight());
		
		basic.bind();
		sequence.renderSequence();
		basic.unbind();
		basic.release();
		
		int lastTex = basic.getTextureID();

		for (ShaderProgram program : programs) {
			FrameBuffer itBuffer = new FrameBuffer(Display.getWidth(), Display.getHeight());
			
			itBuffer.bind();
			program.bind();
			
			renderTexture(lastTex);
			
			program.unbind();
			itBuffer.unbind();
			
			glDeleteTextures(lastTex);
			itBuffer.release();
			
			lastTex = itBuffer.getTextureID();
		}
		
		renderTexture(lastTex);
		glDeleteTextures(lastTex);
	}
	
	private void renderTexture(int texture) {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
		
		glTexCoord2f(0, 0); glVertex3f(0, 0, 0);
		glTexCoord2f(0, 1); glVertex3f(0, Display.getHeight(), 0);
		glTexCoord2f(1, 1); glVertex3f(Display.getWidth(), Display.getHeight(), 0);
		glTexCoord2f(1, 0); glVertex3f(Display.getWidth(), 0, 0);
	}
}
