package com.asysbang.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.util.AbstractProgressIndicatorBase;
import com.intellij.openapi.progress.util.ProgressIndicatorBase;
import org.jetbrains.annotations.NotNull;

/**
 * progress bar demo
 */
public class ProgressBarDemo  extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        ProgressIndicator progressIndicator =  new ProgressIndicatorBase();
        MyProgressIndicator wrapper = new MyProgressIndicator();

        System.out.println("===>>> progressIndicator : " + progressIndicator);
//        ProgressManager.getInstance().runProcess(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("===>>> runProcess " );
//            }
//        }, wrapper);

//        ProgressManager.getInstance().runProcessWithProgressSynchronously(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },"aaa",true,anActionEvent.getProject());

        ProgressManager.getInstance().run(new Task.Modal(anActionEvent.getProject(), "Progress", true) {
            public void run(ProgressIndicator indicator) {
                indicator.setText("This is how you update the indicator");
                try {
                    for (int i=0;i<100;i++) {
                        Thread.sleep(200);
                        indicator.setFraction(0.01*i);
                    }
                } catch (InterruptedException e) {}

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {}
            }
        });

    }

    private class MyProgressIndicator extends AbstractProgressIndicatorBase{

    }
}


