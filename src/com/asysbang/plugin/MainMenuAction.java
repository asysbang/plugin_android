package com.asysbang.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;


/**
 * 测试 下载一个module
 */
public class MainMenuAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        Messages.showMessageDialog("1111","1111",Messages.getInformationIcon());
    }
}
