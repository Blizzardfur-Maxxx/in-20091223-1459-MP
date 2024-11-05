package net.minecraft.client.model;

public final class ModelRenderer {
	private PositionTextureVertex[] corners;
	private TexturedQuad[] faces;
	private int textureOffsetX;
	private int textureOffsetY;
	public boolean mirror = false;

	public ModelRenderer(int i1, int i2) {
		this.textureOffsetX = i1;
		this.textureOffsetY = i2;
	}

	public final void addBox(float f1, float f2, float f3, int i4, int i5, int i6, float f7) {
		this.corners = new PositionTextureVertex[8];
		this.faces = new TexturedQuad[6];
		float f8 = f1 + (float)i4;
		float f9 = f2 + (float)i5;
		float f10 = f3 + (float)i6;
		f1 -= f7;
		f2 -= f7;
		f3 -= f7;
		f8 += f7;
		f9 += f7;
		f10 += f7;
		if(this.mirror) {
			f7 = f8;
			f8 = f1;
			f1 = f7;
		}

		PositionTextureVertex positionTextureVertex20 = new PositionTextureVertex(f1, f2, f3, 0.0F, 0.0F);
		PositionTextureVertex positionTextureVertex11 = new PositionTextureVertex(f8, f2, f3, 0.0F, 8.0F);
		PositionTextureVertex positionTextureVertex12 = new PositionTextureVertex(f8, f9, f3, 8.0F, 8.0F);
		PositionTextureVertex positionTextureVertex18 = new PositionTextureVertex(f1, f9, f3, 8.0F, 0.0F);
		PositionTextureVertex positionTextureVertex13 = new PositionTextureVertex(f1, f2, f10, 0.0F, 0.0F);
		PositionTextureVertex positionTextureVertex15 = new PositionTextureVertex(f8, f2, f10, 0.0F, 8.0F);
		PositionTextureVertex positionTextureVertex21 = new PositionTextureVertex(f8, f9, f10, 8.0F, 8.0F);
		PositionTextureVertex positionTextureVertex14 = new PositionTextureVertex(f1, f9, f10, 8.0F, 0.0F);
		this.corners[0] = positionTextureVertex20;
		this.corners[1] = positionTextureVertex11;
		this.corners[2] = positionTextureVertex12;
		this.corners[3] = positionTextureVertex18;
		this.corners[4] = positionTextureVertex13;
		this.corners[5] = positionTextureVertex15;
		this.corners[6] = positionTextureVertex21;
		this.corners[7] = positionTextureVertex14;
		this.faces[0] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex15, positionTextureVertex11, positionTextureVertex12, positionTextureVertex21}, this.textureOffsetX + i6 + i4, this.textureOffsetY + i6, this.textureOffsetX + i6 + i4 + i6, this.textureOffsetY + i6 + i5);
		this.faces[1] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex20, positionTextureVertex13, positionTextureVertex14, positionTextureVertex18}, this.textureOffsetX, this.textureOffsetY + i6, this.textureOffsetX + i6, this.textureOffsetY + i6 + i5);
		this.faces[2] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex15, positionTextureVertex13, positionTextureVertex20, positionTextureVertex11}, this.textureOffsetX + i6, this.textureOffsetY, this.textureOffsetX + i6 + i4, this.textureOffsetY + i6);
		this.faces[3] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex12, positionTextureVertex18, positionTextureVertex14, positionTextureVertex21}, this.textureOffsetX + i6 + i4, this.textureOffsetY, this.textureOffsetX + i6 + i4 + i4, this.textureOffsetY + i6);
		this.faces[4] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex11, positionTextureVertex20, positionTextureVertex18, positionTextureVertex12}, this.textureOffsetX + i6, this.textureOffsetY + i6, this.textureOffsetX + i6 + i4, this.textureOffsetY + i6 + i5);
		this.faces[5] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex13, positionTextureVertex15, positionTextureVertex21, positionTextureVertex14}, this.textureOffsetX + i6 + i4 + i6, this.textureOffsetY + i6, this.textureOffsetX + i6 + i4 + i6 + i4, this.textureOffsetY + i6 + i5);
		if(this.mirror) {
			for(int i16 = 0; i16 < this.faces.length; ++i16) {
				TexturedQuad texturedQuad17;
				PositionTextureVertex[] positionTextureVertex19 = new PositionTextureVertex[(texturedQuad17 = this.faces[i16]).vertexPositions.length];

				for(i4 = 0; i4 < texturedQuad17.vertexPositions.length; ++i4) {
					positionTextureVertex19[i4] = texturedQuad17.vertexPositions[texturedQuad17.vertexPositions.length - i4 - 1];
				}

				texturedQuad17.vertexPositions = positionTextureVertex19;
			}
		}

	}
}