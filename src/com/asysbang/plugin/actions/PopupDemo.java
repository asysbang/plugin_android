package com.asysbang.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PopupDemo extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        JBPopupFactory.getInstance().createListPopup(new BaseListPopupStep<String>("Modules","A","B","C","D","E","F"){
            @Nullable
            @Override
            public PopupStep onChosen(String selectedValue, boolean finalChoice) {
                System.out.println("===>>> onChosen : "+selectedValue);
                return super.onChosen(selectedValue, finalChoice);
            }
        }).showCenteredInCurrentWindow(project);
    }
}
