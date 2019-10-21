package com.asysbang.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GetFile extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Path ROOT = new File(".").toPath().toAbsolutePath().getParent();
        Path ROOT_RESOURCES = Paths.get(ROOT.toString(), "src", "test", "resources");
        Path ROOT_EPF = Paths.get(ROOT_RESOURCES.toString(), "com", "dubreuia", "model");
    }
}
