package com.asysbang.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.SystemIndependent;

import java.io.File;
import java.io.IOException;


public class CreateFileAction extends AnAction {

    String filePath = "unknown";

    private Runnable createFileRunnable = new Runnable() {
        @Override
        public void run() {
            File newFile = new File("./Neeeee.java");
            if(!newFile.exists()) {
                try {
                    newFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                filePath = newFile.getAbsolutePath();
            }
        }
    };

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project project= anActionEvent.getProject();
        String basePath = project.getBasePath();
        System.out.println(basePath);
        ApplicationManager.getApplication().runWriteAction(createFileRunnable);
        Messages.showMessageDialog(filePath,"FilePath",Messages.getInformationIcon());
    }
}
