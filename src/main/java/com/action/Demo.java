package com.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.PairFunction;
import com.intellij.util.ui.ColorIcon;

import javax.swing.*;

/**
 * @author ：河神
 * @date ：Created in 2021/1/20 2:10 下午
 */
public class Demo extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        //获取当前在操作的工程上下文
        Project project = e.getData(PlatformDataKeys.PROJECT);

        //获取当前操作的类文件
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        //获取当前类文件的路径
        String classPath = psiFile.getVirtualFile().getPath();
        String title = "Hello World!";

        PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);
        Editor editor = e.getData(CommonDataKeys.HOST_EDITOR);

        //显示对话框
        Icon icon0 = ColorIcon.ICON_0;
        String s = Messages.showInputDialog(project, "索引名称:", "请输入索引名称", icon0);
        System.out.println("showInputDialog:"+s);

        int query = Messages.showCheckboxMessageDialog("", "",
                new String[]{"query", "aggs","base"},
                "query",
                true,
                1,
                1,
                ColorIcon.ICON_0,
                (k1, k2) -> {
                    return k1;
                });
        System.out.println(query);





    }
}
