package com.blueking6.springnions.init;

import com.blueking6.springnions.springnions;
import com.blueking6.springnions.blocks.Cultivator;
import com.blueking6.springnions.blocks.OnionCrate;
import com.blueking6.springnions.blocks.OnionShelf;
import com.blueking6.springnions.blocks.TofuPress;
import com.blueking6.springnions.blocks.TofuPress2;
import com.blueking6.springnions.blocks.TofuPress3;
import com.blueking6.springnions.blocks.TofuPressC;
import com.blueking6.springnions.crops.Onion;
import com.blueking6.springnions.crops.Soy;
import com.blueking6.springnions.fluids.SoyMilkBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			springnions.MOD_ID);

	public static final RegistryObject<Block> ONION_CRATE = BLOCKS.register("onion_crate",
			() -> new OnionCrate(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS)));
	public static final RegistryObject<Block> ONION_PLANT = BLOCKS.register("onion_plant",
			() -> new Onion(BlockBehaviour.Properties.copy(Blocks.CARROTS).noOcclusion()));
	public static final RegistryObject<Block> ONION_SHELF = BLOCKS.register("onion_shelf",
			() -> new OnionShelf(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).noOcclusion()));
	public static RegistryObject<LiquidBlock> SOYMILK_BLOCK = BLOCKS.register("soymilk_block",
			() -> new SoyMilkBlock(FluidInit.SOYMILK,
					Properties.of().mapColor(MapColor.WATER).replaceable().pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).noLootTable()));
	public static final RegistryObject<Block> TOFU_PRESS = BLOCKS.register("tofu_press",
			() -> new TofuPress(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).pushReaction(PushReaction.BLOCK).noOcclusion().strength(1f)));
	public static final RegistryObject<Block> TOFU_PRESS2 = BLOCKS.register("tofu_press2",
			() -> new TofuPress2(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).pushReaction(PushReaction.BLOCK).noOcclusion().strength(1.5f)));
	public static final RegistryObject<Block> TOFU_PRESS3 = BLOCKS.register("tofu_press3",
			() -> new TofuPress3(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).pushReaction(PushReaction.BLOCK).noOcclusion().strength(2f)));
	public static final RegistryObject<Block> TOFU_PRESSC = BLOCKS.register("tofu_pressc",
			() -> new TofuPressC(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).pushReaction(PushReaction.BLOCK).noOcclusion().strength(100f)));

	public static final RegistryObject<Block> SOY_PLANT = BLOCKS.register("soy_plant",
			() -> new Soy(BlockBehaviour.Properties.copy(Blocks.CARROTS).noOcclusion()));
	public static final RegistryObject<Block> CULTIVATOR = BLOCKS.register("cultivator", () -> new Cultivator(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).pushReaction(PushReaction.BLOCK).noOcclusion().strength(1f)));

	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}
}
