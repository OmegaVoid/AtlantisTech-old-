package lc.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.world.World;
import lc.api.components.ComponentType;
import lc.api.defs.Definition;
import lc.api.world.OreType;
import lc.common.base.LCBlock;
import lc.common.configuration.xml.ComponentConfig;
import lc.common.resource.ResourceAccess;
import lc.items.ItemBlockBrazier;
import lc.tiles.TileBrazier;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

/**
 * Brazier block implementation
 * 
 * @author AfterLifeLochie
 *
 */
@Definition(name = "blockBrazier", type = ComponentType.DECOR, blockClass = BlockBrazier.class, itemBlockClass = ItemBlockBrazier.class, tileClass = TileBrazier.class)
public class BlockBrazier extends LCBlock {
	private TextureAtlasSprite missing;

	/** Default constructor */
	public BlockBrazier() {
		super(Material.GROUND);
		setLightLevel(1.0F);
		setProvidesTypes(true);
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		missing = register.registerIcon(ResourceAccess.formatResourceName("${ASSET_KEY}:missing"));
	}

	@Override
	public TextureAtlasSprite getIcon(int side, int data) {
		if (data > OreType.values().length)
			return missing;
		return OreType.values()[data].getItemAsBlockTexture();
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < OreType.values().length; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public void configure(ComponentConfig c) {
		// TODO Auto-generated method stub

	}

	@Override
	public AxisAlignedBB Block.getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return new AxisAlignedBB(new BlockPos(x,y,z), new BlockPos(x+1,y+1.4,z+1));
		Block.
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		return getCollisionBoundingBoxFromPool(world, x, y, z);
	}

}
