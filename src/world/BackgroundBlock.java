package world;

import java.nio.ByteBuffer;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class BackgroundBlock {

    public int texture;
    private int depth;
    private int fbo;
    public int x, y;
    public static final int SIZE = 1000; //Maximum size is 11391, idk why - opengl pls

    public BackgroundBlock(int x, int y) {
        this.x = x;
        this.y = y;
        glEnable(GL_TEXTURE_2D); // Enable texturing so we can bind our frame buffer texture
        initFrameBuffer();
    }

    void draw() {
        glBindTexture(GL_TEXTURE_2D, texture);

        glBegin(GL_QUADS);
        {
            glTexCoord2d(0, 0);
            glVertex2d(x - SIZE, y - SIZE);
            glTexCoord2d(0, 1);
            glVertex2d(x - SIZE, y + SIZE);
            glTexCoord2d(1, 1);
            glVertex2d(x + SIZE, y + SIZE);
            glTexCoord2d(1, 0);
            glVertex2d(x + SIZE, y - SIZE);
        }
        glEnd();
    }

    void drawStart() {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo); // Bind our frame buffer for rendering
        glPushAttrib(GL_VIEWPORT_BIT | GL_ENABLE_BIT); // Push our glEnable and glViewport states
        glViewport(0, 0, 2 * SIZE, 2 * SIZE); // Set the size of the frame buffer view port

        //glLoadIdentity();  // Reset the modelview matrix
        glScaled(1. / SIZE, 1. / SIZE, 1. / SIZE);
        glTranslated(-x, -y, 0);
    }

    void drawEnd() {
        glTranslated(x, y, 0);
        glScaled(SIZE, SIZE, SIZE);

        glPopAttrib(); // Restore our glEnable and glViewport states
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0); // Unbind our texture
    }

    private void initFrameBuffer() {
        initFrameBufferDepthBuffer(); // Initialize our frame buffer depth buffer

        initFrameBufferTexture(); // Initialize our frame buffer texture

        fbo = glGenFramebuffersEXT(); // Generate one frame buffer and store the ID in fbo
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo); // Bind our frame buffer

        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, texture, 0); // Attach the texture fbo_texture to the color buffer in our frame buffer

        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depth); // Attach the depth buffer fbo_depth to our frame buffer

        int status = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT); // Check that status of our generated frame buffer

        if (status != GL_FRAMEBUFFER_COMPLETE_EXT) // If the frame buffer does not report back as complete
        {
            System.out.println("Couldn't create frame buffer"); // Output an error to the console
            //System.exit(0); // Exit the application
        }

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0); // Unbind our frame buffer
    }

    private void initFrameBufferDepthBuffer() {
        depth = glGenRenderbuffersEXT(); // Generate one render buffer and store the ID in fbo_depth
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depth); // Bind the fbo_depth render buffer

        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT, 2 * SIZE, 2 * SIZE); // Set the render buffer storage to be a depth component, with a width and height of the window

        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depth); // Set the render buffer of this buffer to the depth buffer

        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, 0); // Unbind the render buffer
    }

    private void initFrameBufferTexture() {
        texture = glGenTextures(); // Generate one texture
        glBindTexture(GL_TEXTURE_2D, texture); // Bind the texture fbo_texture

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 2 * SIZE, 2 * SIZE, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null); // Create a standard texture with the width and height of our window

        // Setup the basic texture parameters
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        // Unbind the texture
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
