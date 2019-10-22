package com.asysbang.plugin;

import com.asysbang.plugin.module.MoBean;
import com.asysbang.plugin.module.MoBeans;
import com.asysbang.plugin.module.ModuleHelper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * 测试 载入module信息
 */
public class GetFile extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        MoBeans modules = ModuleHelper.getInstance().getCacheBean();
        String info = "no module loaded";
        if (modules!=null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Local Version :");
            sb.append(modules.getVersion());
            sb.append("\r\n");
            for (MoBean m : modules.getModules()) {
                sb.append("Module:");
                sb.append(m.getName());
                sb.append(" , ");
                sb.append(m.getVersion());
                sb.append("\r\n");
            }
            info = sb.toString();
        }
        Messages.showMessageDialog(info,"Modules",Messages.getInformationIcon());
    }
}
