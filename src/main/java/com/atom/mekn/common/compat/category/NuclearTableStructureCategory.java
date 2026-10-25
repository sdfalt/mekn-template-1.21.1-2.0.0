package com.atom.mekn.common.compat.category;

import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import com.mojang.serialization.Codec;
import mekanism.client.recipe_viewer.jei.BaseRecipeCategory;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.resource.PrimaryResource;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * JEI 页面：核工程台的多方块结构。
 *
 * <p>一共 5 页，一页一层；每层用真实的方块/物品图标摆成 3×3，
 * 不再用 A/B/C 字母代替。第 3 层中心是工作台，摆好后用铀锭右键它激活。</p>
 */
public class NuclearTableStructureCategory extends BaseRecipeCategory<NuclearTableStructureCategory.Wrapper> {

    public static final RecipeType<Wrapper> TYPE = new RecipeType<>(
            ResourceLocation.fromNamespaceAndPath("mekn", "nuclear_table_structure"),
            Wrapper.class
    );

    private static final ItemStack AIR = ItemStack.EMPTY;
    /** 5 层 × 3×3，索引 = layer * 9 + row * 3 + col。 */
    private static final ItemStack[][] LAYERS = new ItemStack[5][9];

    static {
        for (ItemStack[] layer : LAYERS) {
            Arrays.fill(layer, AIR);
        }
        for (int i = 0; i < 9; i++) {
            LAYERS[0][i] = leadBlock();
            LAYERS[4][i] = leadBlock();
        }
        for (int layer = 1; layer <= 3; layer++) {
            for (int corner : new int[]{0, 2, 6, 8}) {
                LAYERS[layer][corner] = steelCasing();
            }
        }
        LAYERS[1][4] = advancedInductionCell();
        LAYERS[2][4] = new ItemStack(Blocks.CRAFTING_TABLE.asItem());
        LAYERS[3][4] = ultimateChemicalTank();
    }

    private final IDrawable icon;

    public NuclearTableStructureCategory(IGuiHelper helper) {
        super(helper, TYPE, Component.literal("核工程台多方块结构"),
                helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                        new ItemStack(BlcokRegister.NUCLEAR_ENGINEERING_TABLE.asItem())),
                0, 0, 150, 140);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(BlcokRegister.NUCLEAR_ENGINEERING_TABLE.asItem()));
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName(@NotNull Wrapper recipe) {
        return ResourceLocation.fromNamespaceAndPath("mekn",
                "nuclear_table_structure/layer_" + (recipe.layer() + 1));
    }

    @Override
    public @NotNull Codec<Wrapper> getCodec(@NotNull ICodecHelper codecHelper, @NotNull IRecipeManager recipeManager) {
        throw new UnsupportedOperationException("Nuclear table structure is a JEI-only page.");
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull Wrapper recipe, @NotNull IFocusGroup focuses) {
        int gridX = 48;
        int gridY = 22;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                ItemStack stack = LAYERS[recipe.layer()][row * 3 + col];
                if (!stack.isEmpty()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, gridX + col * 18, gridY + row * 18)
                            .addItemStack(stack);
                }
            }
        }
        // 每页都带输出，保证按 R 查核工程台时 5 页都能被索引到
        builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 4)
                .addItemStack(new ItemStack(BlcokRegister.NUCLEAR_ENGINEERING_TABLE.asItem()));
    }

    @Override
    public void createRecipeExtras(@NotNull IRecipeExtrasBuilder builder, @NotNull Wrapper recipe, @NotNull IFocusGroup focuses) {
        super.createRecipeExtras(builder, recipe, focuses);

        builder.addDrawable(new IDrawable() {
            @Override
            public int getWidth() {
                return 150;
            }

            @Override
            public int getHeight() {
                return 140;
            }

            @Override
            public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
                var font = Minecraft.getInstance().font;
                int layer = recipe.layer();
                int gridX = xOffset + 48;
                int gridY = yOffset + 22;

                // 3×3 网格底：空格画“空”，所有格子画边框
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        int x = gridX + col * 18;
                        int y = gridY + row * 18;
                        if (LAYERS[layer][row * 3 + col].isEmpty()) {
                            guiGraphics.fill(x + 1, y + 1, x + 17, y + 17, 0x22333333);
                            guiGraphics.drawString(font, "空", x + 6, y + 5, 0x999999, false);
                        }
                        guiGraphics.renderOutline(x, y, 18, 18, 0x55333333);
                    }
                }

                // 层标题
                String title = switch (layer) {
                    case 0 -> "第1层（底层）";
                    case 4 -> "第5层（顶层）";
                    default -> "第" + (layer + 1) + "层";
                };
                guiGraphics.drawString(font, title, xOffset + 5, yOffset + 6, 0x404040, false);

                // 输出标签
                guiGraphics.drawString(font, "产出", xOffset + 125, yOffset + 24, 0x606060, false);

                // 本层说明
                String desc = switch (layer) {
                    case 0 -> "底层：9 个铅块";
                    case 1 -> "四角：钢壳  中心：高级感应元件";
                    case 2 -> "四角：钢壳  中心：橡木工作台";
                    case 3 -> "四角：钢壳  中心：终级储罐";
                    default -> "顶层：9 个铅块";
                };
                guiGraphics.drawString(font, desc, xOffset + 5, yOffset + 86, 0x606060, false);

                // 激活提示
                guiGraphics.drawString(font, "摆好后用铀锭右键", xOffset + 5, yOffset + 102, 0x606060, false);
                guiGraphics.drawString(font, "第3层中心的工作台激活", xOffset + 5, yOffset + 114, 0x606060, false);
            }
        });
    }

    private static ItemStack leadBlock() {
        return new ItemStack(MekanismBlocks.PROCESSED_RESOURCE_BLOCKS.get(PrimaryResource.LEAD).asItem());
    }

    private static ItemStack steelCasing() {
        return new ItemStack(MekanismBlocks.STEEL_CASING.asItem());
    }

    private static ItemStack advancedInductionCell() {
        return new ItemStack(MekanismBlocks.ADVANCED_INDUCTION_CELL.asItem());
    }

    private static ItemStack ultimateChemicalTank() {
        return new ItemStack(MekanismBlocks.ULTIMATE_CHEMICAL_TANK.asItem());
    }

    public static class Wrapper {
        private final int layer;

        public Wrapper(int layer) {
            this.layer = Math.max(0, Math.min(4, layer));
        }

        public int layer() {
            return layer;
        }
    }
}
