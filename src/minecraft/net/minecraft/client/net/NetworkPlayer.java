package net.minecraft.client.net;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.game.entity.AILiving;
import net.minecraft.game.entity.EntityLiving;

public class NetworkPlayer extends EntityLiving {
	public static final long serialVersionUID = 77479605454997290L;
	private List moveQueue = new LinkedList();
	private Minecraft minecraft;
	private int xp;
	private int yp;
	private int zp;
	private transient int texture = -1;
	public transient BufferedImage newTexture = null;
	public String name;
	public String displayName;
	int tickCount = 0;
	private Textures Textures;
	public AILiving allLiving;

	public NetworkPlayer(Minecraft minecraft, int id, String username, int posX, int posY, int posZ, float rotationPitch, float rotationYaw) {
		super(minecraft.theWorld);
		this.minecraft = minecraft;
		this.displayName = username;
		username = FontRenderer.removeColorCodes(username);
		this.name = username;
		this.xp = posX;
		this.yp = posY;
		this.zp = posZ;
		this.yOffset = 0.0F;
		this.entityCollisionReduction = 0.8F;
		this.setPosition((float)posX / 32.0F, (float)posY / 32.0F, (float)posZ / 32.0F);
		this.rotationYaw = rotationYaw;
		this.rotationPitch = rotationPitch;
		this.armor = this.helmet = false;
		this.renderYawOffset = 0.6875F;
		(new NetworkPlayerTextureLoader(this)).start();
		this.allowAlpha = false;
	}

	public void onLivingUpdate() {
		int i1 = 5;

		do {
			if(this.moveQueue.size() > 0) {
				this.setPos((EntityPos)this.moveQueue.remove(0));
			}
		} while(i1-- > 0 && this.moveQueue.size() > 10);

		this.onGround = true;
	}
	
	protected void updatePlayerActionState() {
		if(this.allLiving.rand.nextFloat() < 0.07F) {
			this.allLiving.moveStrafing = (this.allLiving.rand.nextFloat() - 0.5F) * this.allLiving.moveSpeed;
			this.allLiving.moveForward = this.allLiving.rand.nextFloat() * this.allLiving.moveSpeed;
		}
		this.allLiving.isJumping = this.allLiving.rand.nextFloat() < 0.01F;
		if(this.allLiving.rand.nextFloat() < 0.04F) {
			this.allLiving.randomYawVelocity = (this.allLiving.rand.nextFloat() - 0.5F) * 60.0F;
		}
		this.rotationYaw += this.allLiving.randomYawVelocity;
		this.rotationPitch = 0.0F;
		boolean z1 = this.handleWaterMovement();
		boolean z2 = this.handleLavaMovement();
		if(z1 || z2) {
			this.allLiving.isJumping = this.allLiving.rand.nextFloat() < 0.8F;
		}
	}


	public void bindTexture(Textures Textures) {
		this.Textures = Textures;
		if(this.newTexture != null) {
			BufferedImage bufferedImage2 = this.newTexture;
			int[] i3 = new int[512];
			bufferedImage2.getRGB(32, 0, 32, 16, i3, 0, 32);
			int i5 = 0;

			boolean z10001;
			while(true) {
				if(i5 >= i3.length) {
					z10001 = false;
					break;
				}

				if(i3[i5] >>> 24 < 128) {
					z10001 = true;
					break;
				}

				++i5;
			}

			this.hasHair = z10001;
			this.texture = Textures.loadTexture(this.newTexture);
			this.newTexture = null;
		}

		if(this.texture < 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, Textures.loadTexture("/char.png"));
		} else {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture);
		}
	}

	public void renderHover(Textures Textures1, float f2) {
		FontRenderer font3 = this.minecraft.fontRenderer;
		GL11.glPushMatrix();
		GL11.glTranslatef(this.prevPosX + (this.posX - this.prevPosX) * f2, this.prevPosY + (this.posY - this.prevPosY) * f2 + 0.8F + this.renderYawOffset, this.prevPosZ + (this.posZ - this.prevPosZ) * f2);
		GL11.glRotatef(-this.minecraft.thePlayer.rotationPitch, 0.0F, 1.0F, 0.0F);
		f2 = 0.05F;
		GL11.glScalef(0.05F, -f2, f2);
		GL11.glTranslatef((float)(-font3.getWidth(this.displayName)) / 2.0F, 0.0F, 0.0F);
		GL11.glNormal3f(1.0F, -1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LIGHT0);
		if(this.name.equalsIgnoreCase("Notch")) {
			font3.drawString(this.displayName, 0, 0, 16776960);
		} else {
			font3.drawString(this.displayName, 0, 0, 0xFFFFFF);
		}

		GL11.glDepthFunc(GL11.GL_GREATER);
		GL11.glDepthMask(false);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		font3.drawString(this.displayName, 0, 0, 0xFFFFFF);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glTranslatef(1.0F, 1.0F, -0.05F);
		font3.drawString(this.name, 0, 0, 5263440);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	public void queue(byte xa, byte ya, byte za, float xr, float yr) {
		float f6 = xr - this.rotationPitch;

		float f7;
		for(f7 = yr - this.rotationYaw; f6 >= 180.0F; f6 -= 360.0F) {
		}

		while(f6 < -180.0F) {
			f6 += 360.0F;
		}

		while(f7 >= 180.0F) {
			f7 -= 360.0F;
		}

		while(f7 < -180.0F) {
			f7 += 360.0F;
		}

		f6 = this.rotationPitch + f6 * 0.5F;
		f7 = this.rotationYaw + f7 * 0.5F;
		this.moveQueue.add(new EntityPos(((float)this.xp + (float)xa / 2.0F) / 32.0F, ((float)this.yp + (float)ya / 2.0F) / 32.0F, ((float)this.zp + (float)za / 2.0F) / 32.0F, f6, f7));
		this.xp += xa;
		this.yp += ya;
		this.zp += za;
		this.moveQueue.add(new EntityPos((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F, xr, yr));
	}

	public void teleport(short xa, short ya, short za, float xr, float yr) {
		float f6 = xr - this.rotationPitch;

		float f7;
		for(f7 = yr - this.rotationYaw; f6 >= 180.0F; f6 -= 360.0F) {
		}

		while(f6 < -180.0F) {
			f6 += 360.0F;
		}

		while(f7 >= 180.0F) {
			f7 -= 360.0F;
		}

		while(f7 < -180.0F) {
			f7 += 360.0F;
		}

		f6 = this.rotationPitch + f6 * 0.5F;
		f7 = this.rotationYaw + f7 * 0.5F;
		this.moveQueue.add(new EntityPos((float)(this.xp + xa) / 64.0F, (float)(this.yp + ya) / 64.0F, (float)(this.zp + za) / 64.0F, f6, f7));
		this.xp = xa;
		this.yp = ya;
		this.zp = za;
		this.moveQueue.add(new EntityPos((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F, xr, yr));
	}

	public void queue(byte posX, byte posY, byte posZ) {
		this.moveQueue.add(new EntityPos(((float)this.xp + (float)posX / 2.0F) / 32.0F, ((float)this.yp + (float)posY / 2.0F) / 32.0F, ((float)this.zp + (float)posZ / 2.0F) / 32.0F));
		this.xp += posX;
		this.yp += posY;
		this.zp += posZ;
		this.moveQueue.add(new EntityPos((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F));
	}

	public void queue(float xr, float yr) {
		float f3 = xr - this.rotationPitch;

		float f4;
		for(f4 = yr - this.rotationYaw; f3 >= 180.0F; f3 -= 360.0F) {
		}

		while(f3 < -180.0F) {
			f3 += 360.0F;
		}

		while(f4 >= 180.0F) {
			f4 -= 360.0F;
		}

		while(f4 < -180.0F) {
			f4 += 360.0F;
		}

		f3 = this.rotationPitch + f3 * 0.5F;
		f4 = this.rotationYaw + f4 * 0.5F;
		this.moveQueue.add(new EntityPos(f3, f4));
		this.moveQueue.add(new EntityPos(xr, yr));
	}

	public void clear() {
		if(this.texture >= 0 && this.Textures != null) {
			Textures Textures10000 = this.Textures;
			int i1 = this.texture;
			Textures Textures2 = this.Textures;
			Textures10000.pixelsMap.remove(i1);
			Textures2.ib.clear();
			Textures2.ib.put(i1);
			Textures2.ib.flip();
			GL11.glDeleteTextures(Textures2.ib);
		}

	}
}