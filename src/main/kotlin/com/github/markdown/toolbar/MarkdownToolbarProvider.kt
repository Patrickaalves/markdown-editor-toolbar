package com.github.markdown.toolbar

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationProvider
import java.awt.BorderLayout
import java.util.function.Function
import javax.swing.JComponent
import javax.swing.JPanel

class MarkdownToolbarProvider : EditorNotificationProvider {

    override fun collectNotificationData(
        project: Project,
        file: VirtualFile
    ): Function<in FileEditor, out JComponent?>? {

        if (file.extension?.lowercase() != "md") {
            return null
        }

        return Function { fileEditor ->
            if (fileEditor !is TextEditor) return@Function null

            val panel = JPanel(BorderLayout())
            val actionGroup = MarkdownActionGroup.createActionGroup(fileEditor)

            val toolbar = ActionManager.getInstance().createActionToolbar(
                "MarkdownToolbar",
                actionGroup,
                true
            )
            toolbar.targetComponent = fileEditor.editor.contentComponent

            panel.add(toolbar.component, BorderLayout.CENTER)
            panel
        }
    }
}