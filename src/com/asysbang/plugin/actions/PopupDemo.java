package com.asysbang.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopupDemo extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
//        Project project = anActionEvent.getProject();
//        JBPopupFactory.getInstance().createListPopup(new BaseListPopupStep<String>("Modules","A","B","C","D","E","F"){
//            @Nullable
//            @Override
//            public PopupStep onChosen(String selectedValue, boolean finalChoice) {
//                System.out.println("===>>> onChosen : "+selectedValue);
//                return super.onChosen(selectedValue, finalChoice);
//            }
//        }).showCenteredInCurrentWindow(project);
        showComponentPopup(anActionEvent);
    }


    private void showComponentPopup(AnActionEvent anActionEvent) {
        LoginPanel panel = new LoginPanel();
        ComponentPopupBuilder popupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(panel, panel.getBtn());
        JBPopup popup = popupBuilder.setCancelOnWindowDeactivation(true).createPopup();
        JComponent component = (JComponent) anActionEvent.getInputEvent().getSource();
        popup.showCenteredInCurrentWindow(anActionEvent.getProject());
    }

    private class LoginPanel extends JPanel {

        private final ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("===>>> login btn clicked");
            }
        };
        private JButton mBtn;

        public LoginPanel() {
            super(new BorderLayout());
            mBtn = new JButton("abc");
            mBtn.addActionListener(listener);
            add(mBtn, BorderLayout.CENTER);
        }

        public JButton getBtn() {
            return mBtn;
        }
    }
}
