package com.hs.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.util.Icons;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author ：河神
 * @date ：Created in 2021/6/25 11:47 下午
 */
public class SelectWindow {

    private JPanel panel1;
    private JTabbedPane tabbedPane1;

    private JTextArea textContent;
    private JButton formatButton;
    private JButton unFormatButton;
    private JButton transferredMeaning;
    private JButton unTransferredMeaning;
    private JButton copyButton;

    private Gson unFormatGson = new Gson();
    private Gson formatGson = new GsonBuilder().setPrettyPrinting().create();;


    private Project project;
    private ToolWindow toolWindow;

    /**
     * 0:未格式化
     * 1:格式化
     */
    private int status = 0;

    public SelectWindow(Project project,ToolWindow toolWindow) {
        this.project = project;
        this.toolWindow = toolWindow;

        formatButton.addActionListener(e -> format(true));
        unFormatButton.addActionListener(e-> unFormat());
        copyButton.addActionListener(e-> copyAll());
        transferredMeaning.addActionListener(e-> transferredMeaning());
        unTransferredMeaning.addActionListener(e-> unTransferredMeaning());

        if (!textContent.getBackground().equals(Color.WHITE)){
            textContent.setForeground(Color.WHITE);
        }

        textContent.addPropertyChangeListener("background",e->{
            if (!textContent.getBackground().equals(Color.WHITE)){
                textContent.setForeground(Color.WHITE);
            }else{
                textContent.setForeground(Color.BLACK);
            }
        });

        textContent.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                int clickCount = e.getClickCount();
                if (clickCount==2){
                    if (status==0){
                        format(true);
                    }else{
                        unFormat();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

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
        });

    }

    public JPanel getContent() {
        return panel1;
    }


    public void copyAll(){
        textContent.selectAll();
        textContent.copy();
    }


    public void transferredMeaning(){
        String text = textContent.getText();
        JsonObject jsonObject = unFormatGson.fromJson(text, JsonObject.class);
        String toJson = unFormatGson.toJson(jsonObject);
        String escapeJava = StringEscapeUtils.escapeJava(toJson);
        textContent.setText(escapeJava);
    }

    public void unTransferredMeaning(){
        String text = textContent.getText();
        String unescapeJava = StringEscapeUtils.unescapeJava(text);
        textContent.setText(unescapeJava);
    }

    public void format(boolean infoFlag) {
        String text = textContent.getText();
        if (StringUtils.isBlank(text)){
            return;
        }
        boolean flag = true;
        try {
            JsonObject jsonObject = formatGson.fromJson(text, JsonObject.class);
            String toJson = formatGson.toJson(jsonObject);
            textContent.setText(toJson);
            status = 1;
        }catch (JsonSyntaxException jsonSyntaxException){
            flag = false;
        }

        if (!flag&&text.contains("\\")){
            try {
                String unescapeJava = StringEscapeUtils.unescapeJava(text);
                JsonObject jsonObject = formatGson.fromJson(unescapeJava, JsonObject.class);
                String toJson = formatGson.toJson(jsonObject);
                textContent.setText(toJson);
                status = 1;
                flag = true;
            }catch (JsonSyntaxException jsonSyntaxException){
                flag = false;
            }
        }

        if (!flag&&infoFlag){
            Messages.showMessageDialog( project,"JSON格式错误", "JSON格式化", Icons.DELETE_ICON);
        }
    }

    private void unFormat(){
        try {
            String text = textContent.getText();
            JsonObject jsonObject = unFormatGson.fromJson(text, JsonObject.class);
            String toJson = unFormatGson.toJson(jsonObject);
            textContent.setText(toJson);
            status = 0;
        }catch (JsonSyntaxException jsonSyntaxException){
            Messages.showMessageDialog(project,  "JSON压缩失败","JSON格式化", Icons.DELETE_ICON);
        }
    }

}
