package pw.switchcraft.goodies.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.data.client.*
import net.minecraft.data.client.BlockStateModelGenerator.*
import net.minecraft.data.client.ModelIds.getBlockModelId
import net.minecraft.registry.Registries
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import pw.switchcraft.goodies.Registration.ModBlocks.enderStorage
import pw.switchcraft.goodies.ScGoodies.ModId
import pw.switchcraft.goodies.ironchest.IronChestVariant
import pw.switchcraft.goodies.misc.ConcreteExtras
import java.util.*

class BlockModelProvider(out: FabricDataOutput) : FabricModelProvider(out) {
  override fun generateBlockStateModels(gen: BlockStateModelGenerator) {
    // Register block models for each iron chest variant
    IronChestVariant.values().forEach { variant ->
      registerIronChest(gen, variant)

      registerIronShulker(gen, variant) // Undyed shulker
      DyeColor.values().forEach { registerIronShulker(gen, variant, it) }
    }

    // Ender Storage
    gen.blockStateCollector.accept(createSingletonBlockState(enderStorage, ModId("block/ender_storage")))

    // Concrete Slabs and Stairs
    ConcreteExtras.colors.values.forEach {
      registerSlab(gen, it.baseBlock, it.slabBlock, it.texture)
      registerStairs(gen, it.stairsBlock, it.texture)
    }
  }

  override fun generateItemModels(gen: ItemModelGenerator) {
  }

  private fun registerIronChest(gen: BlockStateModelGenerator, variant: IronChestVariant) {
    log.info("Registering iron chest model for variant=$variant")

    with (variant) {
      gen.registerSingleton(
        chestBlock,
        TextureMap()
          .put(TextureKey.TEXTURE, ModId("entity/chest/${chestId}"))
          .put(TextureKey.PARTICLE, chestParticle),
        ironChestModel
      )

      gen.registerParentedItemModel(chestBlock, getBlockModelId(chestBlock))
    }
  }

  private fun registerIronShulker(gen: BlockStateModelGenerator, variant: IronChestVariant, color: DyeColor? = null) {
    log.info("Registering iron shulker model for variant=$variant color=$color")

    with (variant) {
      val block = (if (color != null) dyedShulkerBlocks[color] else shulkerBlock)
        ?: throw IllegalStateException("Shulker block for variant=$this color=$color is null")

      val id = Registries.BLOCK.getId(block).path
      val texId = ModId("entity/shulker/$id")

      gen.registerSingleton(
        block,
        TextureMap()
          .put(TextureKey.TEXTURE, texId)
          .put(TextureKey.PARTICLE, texId),
        ironShulkerModel
      )

      gen.registerParentedItemModel(block, getBlockModelId(block))
    }
  }

  private fun registerSlab(gen: BlockStateModelGenerator, baseBlock: Block, slabBlock: Block, texture: Identifier) {
    val map = TextureMap()
      .put(TextureKey.BOTTOM, texture)
      .put(TextureKey.TOP, texture)
      .put(TextureKey.SIDE, texture)

    val bottom = Models.SLAB.upload(slabBlock, map, gen.modelCollector)
    val top = Models.SLAB_TOP.upload(slabBlock, map, gen.modelCollector)
    gen.blockStateCollector.accept(createSlabBlockState(slabBlock, bottom, top, getBlockModelId(baseBlock)))
    gen.registerParentedItemModel(slabBlock, bottom)
  }

  private fun registerStairs(gen: BlockStateModelGenerator, stairsBlock: Block, texture: Identifier) {
    val map = TextureMap()
      .put(TextureKey.BOTTOM, texture)
      .put(TextureKey.TOP, texture)
      .put(TextureKey.SIDE, texture)

    val inner = Models.INNER_STAIRS.upload(stairsBlock, map, gen.modelCollector)
    val stairs = Models.STAIRS.upload(stairsBlock, map, gen.modelCollector)
    val outer = Models.OUTER_STAIRS.upload(stairsBlock, map, gen.modelCollector)
    gen.blockStateCollector.accept(createStairsBlockState(stairsBlock, inner, stairs, outer))
    gen.registerParentedItemModel(stairsBlock, stairs)
  }

  companion object {
    private val log by ScGoodiesDatagen::log

    val ironChestModel = Model(Optional.of(ModId("block/iron_chest_base")), Optional.empty(),
      TextureKey.TEXTURE,
      TextureKey.PARTICLE
    )

    val ironShulkerModel = Model(Optional.of(ModId("block/iron_shulker_base")), Optional.empty(),
      TextureKey.TEXTURE,
      TextureKey.PARTICLE
    )
  }
}
