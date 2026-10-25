package com.atom.mekn.common.mekn.overwrite;

import com.atom.mekn.common.utils.Modutils;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

@NothingNullByDefault
public enum MekNLang implements ILangEntry {
    // 机器名称
    NUCLEAR_ENGINEERING_TABLE("block", "nuclear_engineering_table"),
    SUPER_CENTRIFUGE("block", "super_centrifuge"),
    ISOTOPIC_ENCAPSULATION_PRESS("block", "isotopic_encapsulation_press"), // 压制机全名
    NITROGEN_EXTRACTOR_BASIC("block", "basic_nitrogen_extractor"),

    // 机器描述
    DESCRIPTION_NUCLEAR_ENGINEERING_TABLE("description", "nuclear_engineering_table"),
    DESCRIPTION_SUPER_CENTRIFUGE("description", "super_centrifuge"),
    DESCRIPTION_ISOTOPIC_ENCAPSULATION_PRESS("description", "isotopic_encapsulation_press"),
    DESCRIPTION_NITROGEN_EXTRACTOR("description", "nitrogen_extractor"),

    // 界面相关 (GUI)
    GUI_NUCLEAR_ENGINEERING_TABLE("gui", "nuclear_engineering_table"),
    GUI_INGREDIENTS_REQUIRED("gui", "ingredients_required"),
    GUI_ISOTOPIC_ENCAPSULATION_PRESS("gui", "isotopic_encapsulation_press"),

    // 进度与状态 (通用)
    GUI_PROGRESS("gui", "progress"),
    GUI_ENERGY_USAGE("gui", "energy_usage"),

    // 状态或警告消息
    STATION_INSUFFICIENT_NITROGEN("message", "insufficient_nitrogen"),
    STATION_INSUFFICIENT_OIL("message", "insufficient_oil"),
    STATION_NO_CAPSULE("message", "no_capsule"); // 比如压制时缺少胶囊容器

    private final String key;

    MekNLang(String path, String key) {
        this.key = Util.makeDescriptionId(path, Modutils.rl(key));
    }

    @Override
    public String getTranslationKey() {
        return key;
    }
}