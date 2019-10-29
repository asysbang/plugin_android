package com.asysbang.plugin.actions.module;

import com.asysbang.plugin.bean.ModuleInfo;
import com.asysbang.plugin.bean.ModuleState;
import com.asysbang.plugin.module.MoBean;
import com.asysbang.plugin.module.MoBeans;
import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


/**
 * 加载所有可用的module，并显示给用户
 */
public class ModuleListAll extends AnAction implements ModuleListPanel.SelectedCallback {

    private static final String CONFIG_URL = "https://raw.githubusercontent.com/asysbang/plugin_android/master/modules/module.json";

    private ModuleState mState;

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        loadConfig();
        showPopupList(anActionEvent);
    }

    private void loadConfig() {
        VirtualFile configFile = VirtualFileManager.getInstance().refreshAndFindFileByUrl(CONFIG_URL);
        StringBuilder sb = new StringBuilder();
        try {
            InputStream inputStream = configFile.getInputStream();
            BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bis.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //parser json string
        Gson gson = new Gson();
        mState = gson.fromJson(sb.toString(), ModuleState.class);
        System.out.println("===getVersion=" + mState.getVersion());
        ModuleInfo[] modules = mState.getModules();
        for (ModuleInfo m : modules) {
            System.out.println("====" + m.getUrl());
        }
    }

    private JBPopup mModulePopup;
    private void showPopupList(AnActionEvent anActionEvent) {
        ModuleListPanel panel = new ModuleListPanel(mState,this);
        ComponentPopupBuilder popupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(panel, panel.getList());
        mModulePopup = popupBuilder.setCancelOnWindowDeactivation(true).createPopup();
        JComponent component = (JComponent) anActionEvent.getInputEvent().getSource();
        mModulePopup.showCenteredInCurrentWindow(anActionEvent.getProject());
    }

    @Override
    public void selected(ModuleInfo info) {
        mModulePopup.dispose();
    }
}
