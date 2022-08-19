package mod.dooggoo.createatomic.register;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;

import mod.dooggoo.createatomic.CreateAtomic;
import mod.dooggoo.createatomic.blocks.*;
import mod.dooggoo.createatomic.blocks.rbmk.RbmkBase;
import mod.dooggoo.createatomic.blocks.rbmk.RbmkFuelRod;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.Tags;

public class ModBlocks {

        private static final CreateRegistrate registrate = CreateAtomic.registrate()
                .creativeModeTab(() -> ModCreativeModeTab.MAIN_TAB);

        public static final BlockEntry<TestBlock> TEST_BLOCK = registrate.block("test_block", TestBlock::new)
                .initialProperties(SharedProperties::softMetal)
                .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                .register();

        public static final BlockEntry<Block> URANIUM_ORE = registrate.block("uranium_ore", Block::new)
                .initialProperties(() -> Blocks.DEEPSLATE_DIAMOND_ORE)
                .properties(p -> p.sound(SoundType.DEEPSLATE))
                .properties(p -> p.requiresCorrectToolForDrops())
                .tag(Tags.Blocks.ORES)
                .tag(BlockTags.NEEDS_IRON_TOOL)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .simpleItem()
                .register();

        public static final BlockEntry<Block> URANIUM_BLOCK = registrate.block("uranium_block", Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.sound(SoundType.METAL))
                .properties(p -> p.requiresCorrectToolForDrops())
                .tag(Tags.Blocks.STORAGE_BLOCKS)
                .tag(BlockTags.NEEDS_IRON_TOOL)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .simpleItem()
                .register();

        public static final BlockEntry<Block> RAW_URANIUM_BLOCK = registrate.block("raw_uranium_block", Block::new)
                .initialProperties(() -> Blocks.RAW_IRON_BLOCK)
                .properties(p -> p.sound(SoundType.METAL))
                .properties(p -> p.requiresCorrectToolForDrops())
                .tag(Tags.Blocks.STORAGE_BLOCKS)
                .tag(BlockTags.NEEDS_IRON_TOOL)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .simpleItem()
                .register();

        public static final BlockEntry<RbmkBase> RBMK_BASE = registrate.block("rbmk_base", RbmkBase::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                .properties(p -> p.strength(3f, 4f))
                .properties(p -> p.requiresCorrectToolForDrops())
                .tag(BlockTags.NEEDS_IRON_TOOL)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .simpleItem()
                .register();

        public static final BlockEntry<Block> RBMK_CONTROL_ROD = registrate.block("rbmk_control_rod", Block::new)
                .initialProperties(() -> ModBlocks.RBMK_BASE.get())
                .tag(BlockTags.NEEDS_IRON_TOOL)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .simpleItem()
                .register();

        public static final BlockEntry<RbmkFuelRod> RBMK_FUEL_ROD = registrate.block("rbmk_fuel_rod", RbmkFuelRod::new)
                .initialProperties(() -> ModBlocks.RBMK_BASE.get())
                .tag(BlockTags.NEEDS_IRON_TOOL)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .simpleItem()
                .register();

        public static final BlockEntry<Block> RBMK_STEAM_CHANNEL = registrate.block("rbmk_steam_channel", Block::new)
                .initialProperties(() -> ModBlocks.RBMK_BASE.get())
                .tag(BlockTags.NEEDS_IRON_TOOL)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .simpleItem()
                .register();

        public static final BlockEntry<Block>RBMK_MODERATOR = registrate.block("rbmk_moderator", Block::new)
                .initialProperties(() -> ModBlocks.RBMK_BASE.get())
                .tag(BlockTags.NEEDS_IRON_TOOL)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .simpleItem()
                .register();

        public static final BlockEntry<Block> RBMK_REFLECTOR = registrate.block("rbmk_reflector", Block::new)
                .initialProperties(() -> ModBlocks.RBMK_BASE.get())
                .tag(BlockTags.NEEDS_IRON_TOOL)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .simpleItem()
                .register();


        public static void register() {}
}
