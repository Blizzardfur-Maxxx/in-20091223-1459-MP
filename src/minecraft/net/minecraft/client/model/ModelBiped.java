package net.minecraft.client.model;

public final class ModelBiped extends ModelBase {
	private ModelRenderer bipedHead;
	private ModelRenderer bipedHeadWear;
	private ModelRenderer bipedBody;
	private ModelRenderer bipedRightArm;
	private ModelRenderer bipedLeftArm2;
	private ModelRenderer bipedRightLeg;
	private ModelRenderer bipedLeftLeg;

	public ModelBiped() {
		this(0.0F);
	}

	public ModelBiped(float f1) {
		this.bipedHead = new ModelRenderer(0, 0);
		this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
		this.bipedHeadWear = new ModelRenderer(32, 0);
		this.bipedHeadWear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F + 0.5F);
		this.bipedBody = new ModelRenderer(16, 16);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		this.bipedRightArm = new ModelRenderer(40, 16);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftArm2 = new ModelRenderer(40, 16);
		this.bipedLeftArm2.mirror = true;
		this.bipedLeftArm2.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedRightLeg = new ModelRenderer(0, 16);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(0, 16);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
	}
}