package com.asysbang.plugin.actions.module;

import com.asysbang.plugin.bean.ModuleInfo;
import com.asysbang.plugin.bean.ModuleState;
import com.intellij.ui.components.JBList;
import gherkin.lexer.Ja;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ModuleListPanel extends JPanel {

    private final MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println("===>>> : " + mList.getSelectedValue().getUrl());
            if (mCallback !=null) {
                mCallback.selected(mList.getSelectedValue());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };
    private ModuleState mState;

    private SelectedCallback mCallback;

    private JList<ModuleInfo> mList;

    public ModuleListPanel(ModuleState state , SelectedCallback callback) {
        super(new BorderLayout());
        mState = state;
        mList = new JBList<>(new MyModel(mState.getModules()));
        mList.setCellRenderer(new MyCellRenderer());
        add(mList, BorderLayout.CENTER);
        mList.addMouseListener(mouseListener);
        mCallback = callback;
    }

    public JList<ModuleInfo> getList() {
        return mList;
    }

    private class MyCellRenderer extends JPanel implements ListCellRenderer {

        JLabel name = new JLabel();
        JLabel url = new JLabel();
        JLabel version = new JLabel();
        JButton ok;
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("===>>> ActionEvent");
            }
        };

        public MyCellRenderer() {
            super(new BorderLayout());
//            setBorder(new EmptyBorder(20,20,20,20));
            add(name, BorderLayout.WEST);
            add(url, BorderLayout.CENTER);
            add(version, BorderLayout.EAST);
            ok = new JButton("下载/安装/删除/管理");
            ok.addActionListener(l);//接收不到点击事件
            add(ok, BorderLayout.SOUTH);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            ModuleInfo info = (ModuleInfo) value;
            name.setText(info.getName());
            url.setText(info.getUrl());
            version.setText(info.getVersion());
            if (isSelected) {
                this.setBackground(Color.blue);
            } else {
                this.setBackground(null);
            }
            Border titleBorder1=BorderFactory.createTitledBorder(info.getName());
            setBorder(titleBorder1);
            //当鼠标进入也会出发这个刷新
//            System.out.println("===>>> ["+index + " , "+ isSelected+ " , "+cellHasFocus+"]");
            return this;
        }
    }

    private class MyModel extends AbstractListModel<ModuleInfo> {

        private ModuleInfo[] mInfo;

        public MyModel(ModuleInfo[] info) {
            mInfo = info;
        }

        @Override
        public int getSize() {
            return mInfo.length;
        }

        @Override
        public ModuleInfo getElementAt(int index) {
            return mInfo[index];
        }
    }

    public interface SelectedCallback {
        void selected(ModuleInfo info);
    }
}

