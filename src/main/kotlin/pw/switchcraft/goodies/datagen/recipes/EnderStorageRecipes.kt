package pw.switchcraft.goodies.datagen.recipes

import net.minecraft.data.server.RecipeProvider
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Items.*
import pw.switchcraft.goodies.Registration.ModItems
import java.util.function.Consumer

object EnderStorageRecipes {
  internal fun generateRecipes(exporter: Consumer<RecipeJsonProvider>) {
    // Ender Storage
    ShapedRecipeJsonBuilder
      .create(ModItems.enderStorage)
      .pattern("BWB")
      .pattern("OCO")
      .pattern("BEB")
      .input('B', BLAZE_ROD)
      .input('W', WHITE_WOOL) // Specifically white, no others
      .input('O', OBSIDIAN)
      .input('C', CHEST)
      .input('E', ENDER_PEARL)
      .criterion("has_chest", RecipeProvider.conditionsFromItem(CHEST))
      .offerTo(exporter)
  }
}
