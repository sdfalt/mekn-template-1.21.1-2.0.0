package com.atom.mekn.common.compat.jei;

import com.atom.mekn.MekN;
import com.atom.mekn.common.compat.category.IsotopicPressRecipeCategory; // ✅ 记得导入
import com.atom.mekn.common.compat.category.NitrogenExtractorCategory;
import com.atom.mekn.common.compat.category.NuclearRecipeCategory;
import com.atom.mekn.common.compat.category.NuclearTableStructureCategory;
import com.atom.mekn.common.compat.category.SuperCentrifugeRecipeCategory;
import com.atom.mekn.common.mekn.recipes.RecipeTypesRegister;
import com.atom.mekn.common.mekn.recipes.recipe.BasicNuclearRecipe;
import com.atom.mekn.common.mekn.recipes.recipe.IsotopicPressRecipe;
import com.atom.mekn.common.mekn.recipes.recipe.SuperCentrifugeRecipe;
import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@SuppressWarnings("unchecked")
@JeiPlugin
public class MekNJeiPlugin implements IModPlugin {

    private static RecipeType<RecipeHolder<BasicNuclearRecipe>> NUCLEAR_JEI_TYPE;
    private static RecipeType<RecipeHolder<IsotopicPressRecipe>> ISO_JEI_TYPE; //  新增
    private static RecipeType<RecipeHolder<SuperCentrifugeRecipe>> CENTRIFUGE_JEI_TYPE;

    private NuclearRecipeCategory nuclearCategory;
    private IsotopicPressRecipeCategory isotopicCategory; //  新增
    private SuperCentrifugeRecipeCategory centrifugeCategory;
    private NitrogenExtractorCategory extractorCategory;
    private NuclearTableStructureCategory structureCategory;

    private static RecipeType<RecipeHolder<BasicNuclearRecipe>> getNuclearJeiType() {
        if (NUCLEAR_JEI_TYPE == null) {
            NUCLEAR_JEI_TYPE = RecipeType.createFromVanilla(RecipeTypesRegister.NUCLEAR_ENGINEERING.get());
        }
        return NUCLEAR_JEI_TYPE;
    }

    private static RecipeType<RecipeHolder<IsotopicPressRecipe>> getIsoJeiType() {
        if (ISO_JEI_TYPE == null) {
            ISO_JEI_TYPE = RecipeType.createFromVanilla(RecipeTypesRegister.ISOTOPIC_PRESS_TYPE.get());
        }
        return ISO_JEI_TYPE;
    }

    private static RecipeType<RecipeHolder<SuperCentrifugeRecipe>> getCentrifugeJeiType() {
        if (CENTRIFUGE_JEI_TYPE == null) {
            CENTRIFUGE_JEI_TYPE = RecipeType.createFromVanilla(RecipeTypesRegister.SUPER_CENTRIFUGE.get());
        }
        return CENTRIFUGE_JEI_TYPE;
    }


    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath("mekn", "jei_plugin");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        nuclearCategory = new NuclearRecipeCategory(registration.getJeiHelpers().getGuiHelper());
        isotopicCategory = new IsotopicPressRecipeCategory(registration.getJeiHelpers().getGuiHelper());
        centrifugeCategory = new SuperCentrifugeRecipeCategory(registration.getJeiHelpers().getGuiHelper());
        extractorCategory = new NitrogenExtractorCategory(registration.getJeiHelpers().getGuiHelper());
        structureCategory = new NuclearTableStructureCategory(registration.getJeiHelpers().getGuiHelper());
        registration.addRecipeCategories(nuclearCategory, isotopicCategory , centrifugeCategory, extractorCategory, structureCategory);
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        // 核工程台多方块结构：JEI 自定义页面，不依赖服务端配方同步
        registration.addRecipes(NuclearTableStructureCategory.TYPE,
                List.of(new NuclearTableStructureCategory.Wrapper(0),
                        new NuclearTableStructureCategory.Wrapper(1),
                        new NuclearTableStructureCategory.Wrapper(2),
                        new NuclearTableStructureCategory.Wrapper(3),
                        new NuclearTableStructureCategory.Wrapper(4)));

        // JEI 是客户端 mod：配方必须从客户端同步下来的 RecipeManager 取。
        // 不能用 ServerLifecycleHooks.getCurrentServer()——连接独立服务器时它是 null，
        // 只有单人游戏的集成服务器才会让它非空。
        var connection = Minecraft.getInstance().getConnection();
        if (connection == null) {
            MekN.LOGGER.error("【JEI Plugin】尚未连接服务器，无法加载配方！");
            return;
        }
        var recipeManager = connection.getRecipeManager();

        // === 核工程 ===
        var nuclearHolders = recipeManager.getAllRecipesFor(RecipeTypesRegister.NUCLEAR_ENGINEERING.get());
        MekN.LOGGER.info("【JEI Plugin】核工程捞到了 {} 个配方！", nuclearHolders.size());
        registration.addRecipes(getNuclearJeiType(), nuclearHolders);

        // ✅ 同位素中子压力器
        var isoHolders = recipeManager.getAllRecipesFor(RecipeTypesRegister.ISOTOPIC_PRESS_TYPE.get());
        MekN.LOGGER.info("【JEI Plugin】离心机捞到了 {} 个配方！", isoHolders.size());
        registration.addRecipes(getIsoJeiType(), isoHolders);

        // ✅ === 超级离心机 ===
        var centrifugeHolders = recipeManager.getAllRecipesFor(RecipeTypesRegister.SUPER_CENTRIFUGE.get());
        MekN.LOGGER.info("【JEI Plugin】超级离心机捞到了 {} 个配方！", centrifugeHolders.size());
        registration.addRecipes(getCentrifugeJeiType(), centrifugeHolders);

        // ✅ 3. 注册氮气提取机的“假配方” (告诉 JEI 这个页面里有一项内容)
        registration.addRecipes(NitrogenExtractorCategory.TYPE, List.of(new NitrogenExtractorCategory.Wrapper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        // 机器方块 → JEI 分类，按 U 查机器时才能看到对应配方
        registration.addRecipeCatalyst(
                new ItemStack(BlcokRegister.NUCLEAR_ENGINEERING_TABLE.asItem()),
                getNuclearJeiType()
        );
        registration.addRecipeCatalyst(
                new ItemStack(BlcokRegister.SUPER_CENTRIFUGE.asItem()),
                getCentrifugeJeiType()
        );
        registration.addRecipeCatalyst(
                new ItemStack(BlcokRegister.ISOTOPIC_ENCAPSULATION_PRESS.asItem()),
                getIsoJeiType()
        );
        registration.addRecipeCatalyst(
                new ItemStack(BlcokRegister.NITROGEN_EXTRACTOR.asItem()),
                NitrogenExtractorCategory.TYPE
        );
        registration.addRecipeCatalyst(
                new ItemStack(BlcokRegister.NUCLEAR_ENGINEERING_TABLE.asItem()),
                NuclearTableStructureCategory.TYPE
        );
    }

}
