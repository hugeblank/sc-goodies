package io.sc3.goodies.ironchest

import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import io.sc3.goodies.ScGoodies.ModId
import io.sc3.goodies.ironshulker.IronShulkerBlock
import io.sc3.goodies.ironshulker.IronShulkerBlockEntity
import io.sc3.goodies.ironshulker.IronShulkerItem
import io.sc3.goodies.ironshulker.IronShulkerScreenHandler

enum class IronChestVariant(
  val humanName: String,
  val chestId: String,
  val shulkerId: String,
  val chestParticle: Identifier,
  val rows: Int,
  val columns: Int = 9
) {
  IRON("Iron", "iron_chest", "shulker_box_iron", Identifier("block/iron_block"), 6),
  GOLD("Gold", "gold_chest", "shulker_box_gold", Identifier("block/gold_block"), 9),
  DIAMOND("Diamond", "diamond_chest", "shulker_box_diamond", Identifier("block/diamond_block"), 9, 12);

  val size = rows * columns
  val screenTex = ModId("textures/gui/container/iron_chest_${columns}x${rows}.png")

  // Chests
  val chestBlock by lazy { Registries.BLOCK.get(ModId(chestId)) as IronChestBlock }

  val chestBlockEntityType by lazy {
    @Suppress("UNCHECKED_CAST")
    Registries.BLOCK_ENTITY_TYPE.get(ModId(chestId)) as BlockEntityType<IronChestBlockEntity>
  }

  val chestScreenHandlerType: ScreenHandlerType<IronChestScreenHandler> by lazy {
    ScreenHandlerType { id, inv -> IronChestScreenHandler(this, id, inv) }
  }

  // Shulkers
  val shulkerBlock by lazy { Registries.BLOCK.get(ModId(shulkerId)) as IronShulkerBlock }
  val dyedShulkerBlocks: Map<DyeColor, IronShulkerBlock> by lazy {
    DyeColor.values().associateWith { color ->
      Registries.BLOCK.get(ModId(shulkerId + "_" + color.getName())) as IronShulkerBlock
    }
  }
  val dyedShulkerItems: Map<DyeColor, IronShulkerItem> by lazy {
    DyeColor.values().associateWith { color ->
      Registries.ITEM.get(ModId(shulkerId + "_" + color.getName())) as IronShulkerItem
    }
  }

  val shulkerBlockEntityType by lazy {
    @Suppress("UNCHECKED_CAST")
    Registries.BLOCK_ENTITY_TYPE.get(ModId(shulkerId)) as BlockEntityType<IronShulkerBlockEntity>
  }

  val shulkerScreenHandlerType: ScreenHandlerType<IronShulkerScreenHandler> by lazy {
    ScreenHandlerType { id, inv -> IronShulkerScreenHandler(this, id, inv) }
  }
}
