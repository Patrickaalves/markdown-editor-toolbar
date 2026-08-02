package com.github.markdown.toolbar

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.command.undo.UndoManager
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import java.awt.*
import javax.swing.Icon

// Classe para desenhar os ícones visuais (B, I, TT, ~~S~~, 1., •—, etc.)
class TextIcon(
    private val text: String,
    private val isBold: Boolean = false,
    private val isItalic: Boolean = false,
    private val isStrikethrough: Boolean = false,
    private val fontSize: Int = 12
) : Icon {

    override fun getIconWidth(): Int = JBUI.scale(18)
    override fun getIconHeight(): Int = JBUI.scale(18)

    override fun paintIcon(c: Component?, g: Graphics?, x: Int, y: Int) {
        val g2 = g?.create() as? Graphics2D ?: return
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        val style = when {
            isBold && isItalic -> Font.BOLD or Font.ITALIC
            isBold -> Font.BOLD
            isItalic -> Font.ITALIC
            else -> Font.PLAIN
        }

        val scaledSize = JBUI.scale(fontSize)
        g2.font = Font(Font.SANS_SERIF, style, scaledSize)
        g2.color = c?.foreground ?: JBColor.foreground()

        val fm = g2.fontMetrics
        val textWidth = fm.stringWidth(text)
        val textHeight = fm.ascent - fm.descent

        val drawX = x + (iconWidth - textWidth) / 2
        val drawY = y + (iconHeight + textHeight) / 2 + JBUI.scale(1)

        g2.drawString(text, drawX, drawY)

        if (isStrikethrough) {
            val lineY = y + iconHeight / 2
            g2.stroke = BasicStroke(JBUI.scale(1.5f))
            g2.drawLine(x + JBUI.scale(2), lineY, x + iconWidth - JBUI.scale(2), lineY)
        }

        g2.dispose()
    }
}

object MarkdownActionGroup {

    fun createActionGroup(fileEditor: TextEditor): ActionGroup {
        val group = DefaultActionGroup()
        val editor = fileEditor.editor
        val project = editor.project ?: return group

        // 1. Linha Horizontal (Divisor)
        group.add(createAction("Linha Horizontal (---)", TextIcon("—", isBold = true, fontSize = 14)) {
            insertText(editor, "\n---\n")
        })

        group.addSeparator()

        // 2. Undo
        group.add(createAction("Desfazer (Undo)", AllIcons.Actions.Undo) {
            val undoManager = UndoManager.getInstance(project)
            if (undoManager.isUndoAvailable(fileEditor)) undoManager.undo(fileEditor)
        })

        // 3. Redo
        group.add(createAction("Refazer (Redo)", AllIcons.Actions.Redo) {
            val undoManager = UndoManager.getInstance(project)
            if (undoManager.isRedoAvailable(fileEditor)) undoManager.redo(fileEditor)
        })

        group.addSeparator()

        // 4. Negrito (B)
        group.add(createAction("Negrito (**texto**)", TextIcon("B", isBold = true, fontSize = 13)) {
            wrapSelection(editor, "**", "**")
        })

        // 5. Itálico (I)
        group.add(createAction("Itálico (*texto*)", TextIcon("I", isItalic = true, fontSize = 13)) {
            wrapSelection(editor, "*", "*")
        })

        // 6. Cabeçalho (TT)
        group.add(createAction("Título / Cabeçalho (#, ##, ###)", TextIcon("TT", isBold = true, fontSize = 11)) {
            toggleHeader(editor)
        })

        // 7. Tachado (S)
        group.add(createAction("Tachado (~~texto~~)", TextIcon("S", isBold = true, isStrikethrough = true, fontSize = 12)) {
            wrapSelection(editor, "~~", "~~")
        })

        group.addSeparator()

        // 8. Lista com Marcadores (•—)
        group.add(createAction("Lista com Marcadores (- )", TextIcon("•—", isBold = true, fontSize = 10)) {
            prefixLines(editor, "- ")
        })

        // 9. Lista Numerada (1.)
        group.add(createAction("Lista Numerada (1. )", TextIcon("1.", isBold = true, fontSize = 11)) {
            prefixLines(editor, "1. ")
        })

        // 10. Lista de Tarefas (☑)
        group.add(createAction("Lista de Tarefas (- [ ] )", TextIcon("☑", fontSize = 12)) {
            prefixLines(editor, "- [ ] ")
        })

        // 11. Citação (”)
        group.add(createAction("Citação (> )", TextIcon("”", isBold = true, fontSize = 16)) {
            prefixLines(editor, "> ")
        })

        // 12. Bloco de Código (<>)
        group.add(createAction("Código / Bloco de Código", TextIcon("<>", isBold = true, fontSize = 11)) {
            val selection = editor.selectionModel.selectedText
            if (selection != null && selection.contains("\n")) {
                wrapSelection(editor, "```\n", "\n```")
            } else {
                wrapSelection(editor, "`", "`")
            }
        })

        group.addSeparator()

        // 13. Tabela (⊞)
        group.add(createAction("Inserir Tabela", TextIcon("⊞", fontSize = 13)) {
            val tableTemplate = "\n| Cabeçalho 1 | Cabeçalho 2 |\n| --- | --- |\n| Item 1 | Item 2 |\n"
            insertText(editor, tableTemplate)
        })

        // 14. Link (🔗)
        group.add(createAction("Inserir Link ([texto](url))", TextIcon("🔗", fontSize = 11)) {
            wrapSelection(editor, "[", "](https://)")
        })

        // 15. Imagem (🖼)
        group.add(createAction("Inserir Imagem (![alt](url))", TextIcon("🖼", fontSize = 11)) {
            wrapSelection(editor, "![", "](caminho/imagem.png)")
        })

        return group
    }

    private fun createAction(title: String, icon: Icon, action: () -> Unit): AnAction {
        return object : AnAction(title, title, icon) {
            override fun actionPerformed(e: AnActionEvent) {
                val project = e.project ?: return
                WriteCommandAction.runWriteCommandAction(project) { action() }
            }
        }
    }

    private fun insertText(editor: Editor, text: String) {
        val offset = editor.caretModel.offset
        editor.document.insertString(offset, text)
        editor.caretModel.moveToOffset(offset + text.length)
    }

    private fun wrapSelection(editor: Editor, prefix: String, suffix: String) {
        val selectionModel = editor.selectionModel
        if (selectionModel.hasSelection()) {
            val start = selectionModel.selectionStart
            val end = selectionModel.selectionEnd
            val selectedText = selectionModel.selectedText ?: ""
            editor.document.replaceString(start, end, "$prefix$selectedText$suffix")
        } else {
            val offset = editor.caretModel.offset
            editor.document.insertString(offset, "$prefix$suffix")
            editor.caretModel.moveToOffset(offset + prefix.length)
        }
    }

    private fun toggleHeader(editor: Editor) {
        val document = editor.document
        val caret = editor.caretModel
        val lineNumber = document.getLineNumber(caret.offset)
        val lineStart = document.getLineStartOffset(lineNumber)
        val lineEnd = document.getLineEndOffset(lineNumber)
        val lineText = document.getText(com.intellij.openapi.util.TextRange(lineStart, lineEnd))

        val newText = when {
            lineText.startsWith("### ") -> lineText.removePrefix("### ")
            lineText.startsWith("## ") -> "### " + lineText.removePrefix("## ")
            lineText.startsWith("# ") -> "## " + lineText.removePrefix("# ")
            else -> "# " + lineText
        }
        document.replaceString(lineStart, lineEnd, newText)
    }

    private fun prefixLines(editor: Editor, prefix: String) {
        val document = editor.document
        val selectionModel = editor.selectionModel
        val startLine = document.getLineNumber(if (selectionModel.hasSelection()) selectionModel.selectionStart else editor.caretModel.offset)
        val endLine = document.getLineNumber(if (selectionModel.hasSelection()) selectionModel.selectionEnd else editor.caretModel.offset)

        for (line in startLine..endLine) {
            val lineStart = document.getLineStartOffset(line)
            document.insertString(lineStart, prefix)
        }
    }
}