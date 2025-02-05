package io.sc3.goodies.datagen

import io.sc3.goodies.Registration.ModBlocks
import io.sc3.goodies.ironchest.IronChestVariant
import io.sc3.goodies.misc.ConcreteExtras
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import java.util.concurrent.CompletableFuture

class BlockTagProvider(
  out: FabricDataOutput,
  future: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricTagProvider<Block>(out, RegistryKeys.BLOCK, future) {
  override fun configure(arg: RegistryWrapper.WrapperLookup) {
    getOrCreateTagBuilder(BlockTags.DIRT)
      .add(ModBlocks.pinkGrass, ModBlocks.autumnGrass, ModBlocks.blueGrass)
    getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE)
      .add(ModBlocks.pinkGrass, ModBlocks.autumnGrass, ModBlocks.blueGrass)

    val pickaxeBlocks = mutableListOf<Block>(ModBlocks.enderStorage)

    IronChestVariant.values().forEach {
      pickaxeBlocks.add(it.chestBlock)
      pickaxeBlocks.add(it.shulkerBlock)
      pickaxeBlocks.addAll(it.dyedShulkerBlocks.values)
    }

    ConcreteExtras.colors.values.forEach {
      pickaxeBlocks.add(it.slabBlock)
      pickaxeBlocks.add(it.stairsBlock)
    }

    getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
      .add(*pickaxeBlocks.toTypedArray())
  }
}
