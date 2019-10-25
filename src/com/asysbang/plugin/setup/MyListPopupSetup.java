package com.asysbang.plugin.setup;

import com.asysbang.plugin.bean.ModuleInfo;
import com.intellij.openapi.ui.popup.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class MyListPopupSetup implements ListPopupStep<ModuleInfo> {
    @NotNull
    @Override
    public List<ModuleInfo> getValues() {
        return null;
    }

    @Override
    public boolean isSelectable(ModuleInfo moduleInfo) {
        return false;
    }

    @Nullable
    @Override
    public Icon getIconFor(ModuleInfo moduleInfo) {
        return null;
    }

    @NotNull
    @Override
    public String getTextFor(ModuleInfo moduleInfo) {
        return null;
    }

    @Nullable
    @Override
    public ListSeparator getSeparatorAbove(ModuleInfo moduleInfo) {
        return null;
    }

    @Override
    public int getDefaultOptionIndex() {
        return 0;
    }

    @Nullable
    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public PopupStep onChosen(ModuleInfo moduleInfo, boolean b) {
        return null;
    }

    @Override
    public boolean hasSubstep(ModuleInfo moduleInfo) {
        return false;
    }

    @Override
    public void canceled() {

    }

    @Override
    public boolean isMnemonicsNavigationEnabled() {
        return false;
    }

    @Nullable
    @Override
    public MnemonicNavigationFilter<ModuleInfo> getMnemonicNavigationFilter() {
        return null;
    }

    @Override
    public boolean isSpeedSearchEnabled() {
        return false;
    }

    @Nullable
    @Override
    public SpeedSearchFilter<ModuleInfo> getSpeedSearchFilter() {
        return null;
    }

    @Override
    public boolean isAutoSelectionEnabled() {
        return false;
    }

    @Nullable
    @Override
    public Runnable getFinalRunnable() {
        return null;
    }
}
