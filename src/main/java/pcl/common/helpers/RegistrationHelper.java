package pcl.common.helpers;

import java.lang.reflect.Constructor;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.apache.logging.log4j.Level;

import pcl.lc.BuildInfo;
import pcl.lc.LanteaCraft;
import pcl.lc.base.GenericBlockRenderer;
import pcl.lc.blocks.BlockLanteaDecorStair;
import pcl.lc.module.core.fluid.ItemSpecialBucket;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

public class RegistrationHelper {

	private static boolean isLateRegistrationZone = false;

	/**
	 * Marks the RegistrationHelper in the PostInit phase. If any registration
	 * occurs after the late registration flag is set, a warning will be issued.
	 */
	public static void flagLateRegistrationZone() {
		RegistrationHelper.isLateRegistrationZone = true;
	}

	/**
	 * Register a Block with a given class and unlocalized name. The block will
	 * use the default {@link ItemBlock} structure when held as an item.
	 * 
	 * @param classOf
	 *            The class of the block.
	 * @param unlocalizedName
	 *            The unlocalized name.
	 * @return The Block singleton.
	 */
	public static <T extends Block> T registerBlock(Class<? extends T> classOf, String unlocalizedName) {
		return registerBlock(classOf, ItemBlock.class, unlocalizedName);
	}

	/**
	 * Register a block with a given class, a given item class and an
	 * unlocalized name. The block will display by default in CreativeTabs.
	 * 
	 * @param classOf
	 *            The class of the block.
	 * @param itemClassOf
	 *            The class of the item.
	 * @param unlocalizedName
	 *            The unlocalized name.
	 * @return The Block singleton.
	 */
	public static <T extends Block> T registerBlock(Class<? extends T> classOf, Class<? extends ItemBlock> itemClassOf,
			String unlocalizedName) {
		return registerBlock(classOf, itemClassOf, unlocalizedName, true);
	}

	/**
	 * Register a block with a given class, a given item class, an unlocalized
	 * name and a display preference in CreativeTabs.
	 * 
	 * @param classOf
	 *            The class of the block.
	 * @param itemClassOf
	 *            The class of the item.
	 * @param unlocalizedName
	 *            The unlocalized name.
	 * @param inCreativeTabs
	 *            Show the item in the CreativeTabs instance.
	 * @return The Block singleton.
	 */
	public static <T extends Block> T registerBlock(Class<? extends T> classOf, Class<? extends ItemBlock> itemClassOf,
			String unlocalizedName, boolean inCreativeTabs) {
		LanteaCraft.getLogger().log(Level.INFO, String.format("Attempting to register block %s", unlocalizedName));
		if (isLateRegistrationZone)
			LanteaCraft.getLogger().log(Level.WARN, "Warning, registration of this block is later than was expected!");
		try {
			Constructor<? extends T> ctor = classOf.getConstructor();
			T theMysteryBlock = ctor.newInstance();
			theMysteryBlock.setBlockName(unlocalizedName);
			if (inCreativeTabs)
				theMysteryBlock.setCreativeTab(LanteaCraft.getCreativeTab());
			GameRegistry.registerBlock(theMysteryBlock, itemClassOf, unlocalizedName);
			return theMysteryBlock;
		} catch (Throwable e) {
			LanteaCraft.getLogger().log(Level.FATAL, "Failed to register block, an exception occured.", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Registers an item with a given class and an unlocalized name.
	 * 
	 * @param classOf
	 *            The class of the item.
	 * @param unlocalizedName
	 *            The unlocalized name.
	 * @return The Item singleton.
	 */
	public static <T extends Item> T registerItem(Class<? extends T> classOf, String unlocalizedName) {
		LanteaCraft.getLogger().log(Level.DEBUG, "Attempting to register item " + unlocalizedName);
		if (isLateRegistrationZone)
			LanteaCraft.getLogger().log(Level.WARN, "Warning, registration of this item is later than was expected!");
		try {
			Constructor<? extends T> ctor = classOf.getConstructor();
			T theMysteryItem = ctor.newInstance();
			theMysteryItem.setUnlocalizedName(unlocalizedName).setCreativeTab(LanteaCraft.getCreativeTab());
			GameRegistry.registerItem(theMysteryItem, unlocalizedName);
			return theMysteryItem;
		} catch (Exception e) {
			LanteaCraft.getLogger().log(Level.FATAL, "Failed to register item, an exception occured.", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Registers a special bucket.
	 * 
	 * @param hostOf
	 *            The host fluid.
	 * @param unlocalizedName
	 *            The unlocalized name for the bucket.
	 * @param bucketTextureName
	 *            The texture target for the bucket.
	 * @return The ItemSpecialBucket singleton for this host fluid.
	 */
	public static ItemSpecialBucket registerSpecialBucket(Block hostOf, String unlocalizedName, String bucketTextureName) {
		LanteaCraft.getLogger().log(Level.DEBUG, "Attempting to register SpecialBucket " + unlocalizedName);
		if (isLateRegistrationZone)
			LanteaCraft.getLogger().log(Level.WARN,
					"Warning, registration of this SpecialBucket is later than was expected!");
		ItemSpecialBucket bucket = new ItemSpecialBucket(hostOf);
		bucket.setUnlocalizedName(unlocalizedName).setCreativeTab(LanteaCraft.getCreativeTab());
		bucket.setTargetTexture(bucketTextureName);
		GameRegistry.registerItem(bucket, unlocalizedName);
		return bucket;
	}

	/**
	 * Registers a trade handler for a type of villager.
	 * 
	 * @param villagerID
	 *            The villager type ID.
	 * @param handler
	 *            The handler to register.
	 */
	public static void addTradeHandler(int villagerID, IVillageTradeHandler handler) {
		LanteaCraft.getLogger().log(Level.DEBUG, "Registering trade handler for villager ID " + villagerID);
		if (isLateRegistrationZone)
			LanteaCraft.getLogger().log(Level.WARN,
					"Warning, registration of this trade handler is later than was expected!");
		VillagerRegistry.instance().registerVillageTradeHandler(villagerID, handler);
	}

	/**
	 * Registers an ore with the ore dictionary.
	 * 
	 * @param name
	 *            The ore name.
	 * @param item
	 *            The ItemStack.
	 */
	public static void registerOre(String name, ItemStack item) {
		LanteaCraft.getLogger().log(Level.DEBUG, "Registering ore with name " + name);
		if (isLateRegistrationZone)
			LanteaCraft.getLogger().log(Level.WARN, "Warning, registration of this ore is later than was expected!");
		OreDictionary.registerOre(String.format("%s:%s", BuildInfo.modID, name), item);
	}

	/**
	 * Creates a new shaped recipe.
	 * 
	 * @param product
	 *            The product ItemStack.
	 * @param params
	 *            The crafting arrangement.
	 */
	public static void newRecipe(ItemStack product, Object... params) {
		LanteaCraft.getLogger().log(Level.DEBUG, "Registering new generic recipe");
		if (isLateRegistrationZone)
			LanteaCraft.getLogger().log(Level.WARN, "Warning, registration of this recipe is later than was expected!");
		GameRegistry.addRecipe(new ShapedOreRecipe(product, params));
	}

	/**
	 * Creates a new shapeless recipe.
	 * 
	 * @param product
	 *            The product ItemStack.
	 * @param params
	 *            The crafting components.
	 */
	public static void newShapelessRecipe(ItemStack product, Object... params) {
		LanteaCraft.getLogger().log(Level.DEBUG, "Registering new generic shapeless recipe");
		if (isLateRegistrationZone)
			LanteaCraft.getLogger().log(Level.WARN,
					"Warning, registration of this shapeless recipe is later than was expected!");
		GameRegistry.addRecipe(new ShapelessOreRecipe(product, params));
	}

	/**
	 * Registers a new smelting recipe.
	 * 
	 * @param in
	 *            The input stack
	 * @param out
	 *            The output result
	 * @param xp
	 *            Quantity of XP earnt
	 */
	public static void newSmeltingRecipe(ItemStack in, ItemStack out, float xp) {
		LanteaCraft.getLogger().log(Level.DEBUG, "Registering new smelting recipe");
		if (isLateRegistrationZone)
			LanteaCraft.getLogger().log(Level.WARN,
					"Warning, registration of this smelting is later than was expected!");
		FurnaceRecipes.smelting().func_151394_a(in, out, xp);
	}

	/**
	 * Registers a new chest item handler.
	 * 
	 * @param stack
	 *            The ItemStack to add.
	 * @param minQty
	 *            The minimum random item quantity.
	 * @param maxQty
	 *            The maximum random item quantity.
	 * @param weight
	 *            The weighting of the random number generation for this random.
	 * @param category
	 *            The categories of chests this rule applies to.
	 */
	public static void addRandomChestItem(ItemStack stack, int minQty, int maxQty, int weight, String... category) {
		if (isLateRegistrationZone)
			LanteaCraft.getLogger().log(Level.WARN,
					"Warning, registration of this random chest behaviour is later than was expected!");
		WeightedRandomChestContent item = new WeightedRandomChestContent(stack, minQty, maxQty, weight);
		for (String element : category) {
			LanteaCraft.getLogger().log(Level.DEBUG, "Adding new WeightedRandomChestContent for element " + element);
			ChestGenHooks.addItem(element, item);
		}
	}

	/**
	 * Register a block renderer
	 * 
	 * @param renderer
	 *            A block renderer
	 */
	public static void registerRenderer(GenericBlockRenderer renderer) {
		if (isLateRegistrationZone)
			LanteaCraft.getLogger().log(Level.WARN,
					"Warning, registration of this block renderer is later than was expected!");
		renderer.renderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(renderer);
	}

	/**
	 * Registers a tile entity renderer
	 * 
	 * @param teClass
	 *            The tile entity class
	 * @param renderer
	 *            The renderer object
	 */
	public static void addTileEntityRenderer(Class<? extends TileEntity> teClass, TileEntitySpecialRenderer renderer) {
		if (isLateRegistrationZone)
			LanteaCraft.getLogger().log(Level.WARN,
					"Warning, registration of this tile-entity renderer is later than was expected!");
		ClientRegistry.bindTileEntitySpecialRenderer(teClass, renderer);
	}

	/**
	 * Registers a block as a LanteaCraft stair-type decal.
	 * 
	 * @param unlocalizedName
	 *            The unlocalized name of the target block. Has to already have
	 *            been reigstered with FML.
	 * @param targetMetadata
	 *            The target metadata type, if not default. (0)
	 * @return The decal stair instance.
	 */
	@Deprecated
	public static BlockLanteaDecorStair registerStairDecal(String unlocalizedName, int targetMetadata) {
		LanteaCraft.getLogger()
				.log(Level.INFO, String.format("Attempting to register stair decal %s", unlocalizedName));
		if (isLateRegistrationZone)
			LanteaCraft.getLogger().log(Level.WARN,
					"Warning, registration of this stair decal is later than was expected!");
		try {
			BlockLanteaDecorStair stair = new BlockLanteaDecorStair(targetMetadata);
			stair.setBlockName(unlocalizedName).setCreativeTab(LanteaCraft.getCreativeTab());
			GameRegistry.registerBlock(stair, unlocalizedName);
			return stair;
		} catch (Exception e) {
			LanteaCraft.getLogger().log(Level.FATAL, "Failed to register stair decal, an exception occured.", e);
			throw new RuntimeException(e);
		}
	}
}
