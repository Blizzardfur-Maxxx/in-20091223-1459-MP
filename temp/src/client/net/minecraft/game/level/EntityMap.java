package net.minecraft.game.level;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;

public final class EntityMap {
	public int xSlot;
	public int ySlot;
	public int zSlot;
	EntityMapSlot slot0 = new EntityMapSlot(this, (Unused)null);
	EntityMapSlot slot1 = new EntityMapSlot(this, (Unused)null);
	public List[] entityGrid;
	public List entities = new ArrayList();
	private List entitiesExcludingEntity = new ArrayList();

	public EntityMap(int i1, int i2, int i3) {
		this.xSlot = i1 / 16;
		this.ySlot = i2 / 16;
		this.zSlot = i3 / 16;
		if(this.xSlot == 0) {
			this.xSlot = 1;
		}

		if(this.ySlot == 0) {
			this.ySlot = 1;
		}

		if(this.zSlot == 0) {
			this.zSlot = 1;
		}

		this.entityGrid = new ArrayList[this.xSlot * this.ySlot * this.zSlot];

		for(i1 = 0; i1 < this.xSlot; ++i1) {
			for(i2 = 0; i2 < this.ySlot; ++i2) {
				for(i3 = 0; i3 < this.zSlot; ++i3) {
					this.entityGrid[(i3 * this.ySlot + i2) * this.xSlot + i1] = new ArrayList();
				}
			}
		}

	}

	public final List getEntitiesWithinAABBExcludingEntity(Entity entity1, AxisAlignedBB axisAlignedBB2) {
		this.entitiesExcludingEntity.clear();
		List list8 = this.entitiesExcludingEntity;
		float f7 = axisAlignedBB2.z1;
		float f6 = axisAlignedBB2.y1;
		float f5 = axisAlignedBB2.x1;
		float f4 = axisAlignedBB2.z0;
		float f3 = axisAlignedBB2.y0;
		float f25 = axisAlignedBB2.x0;
		entity1 = entity1;
		EntityMap entityMap24 = this;
		EntityMapSlot entityMapSlot9 = this.slot0.init(f25, f3, f4);
		EntityMapSlot entityMapSlot10 = this.slot1.init(f5, f6, f7);
		List list14 = null;

		for(int i11 = entityMapSlot9.xSlot - 1; i11 <= entityMapSlot10.xSlot + 1; ++i11) {
			for(int i12 = entityMapSlot9.ySlot - 1; i12 <= entityMapSlot10.ySlot + 1; ++i12) {
				for(int i13 = entityMapSlot9.zSlot - 1; i13 <= entityMapSlot10.zSlot + 1; ++i13) {
					if(i11 >= 0 && i12 >= 0 && i13 >= 0 && i11 < entityMap24.xSlot && i12 < entityMap24.ySlot && i13 < entityMap24.zSlot) {
						list14 = entityMap24.entityGrid[(i13 * entityMap24.ySlot + i12) * entityMap24.xSlot + i11];

						for(int i15 = 0; i15 < list14.size(); ++i15) {
							Entity entity16;
							if((entity16 = (Entity)list14.get(i15)) != entity1) {
								AxisAlignedBB axisAlignedBB17 = null;
								axisAlignedBB17 = entity16.boundingBox;
								if(f5 > axisAlignedBB17.x0 && f25 < axisAlignedBB17.x1 ? (f6 > axisAlignedBB17.y0 && f3 < axisAlignedBB17.y1 ? f7 > axisAlignedBB17.z0 && f4 < axisAlignedBB17.z1 : false) : false) {
									list8.add(entity16);
								}
							}
						}
					}
				}
			}
		}

		return list8;
	}
}