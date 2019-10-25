package com.asysbang.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class TestAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        setEnabledInModalContext(true);
    }
}
