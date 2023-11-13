package so.ray.linker

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDocumentManager
import io.ktor.util.*
import java.awt.Desktop
import java.net.URI
import java.util.*


class RaySoLink : AnAction() {

    fun getFileLanguage(fileType: FileType): String {
        val name = fileType.name
        //if ide does not have correct file type for ray.so can add an exception in here
        return when(name) {
            else -> name.toLowerCasePreservingASCIIRules()
        }
    }

    @Throws(Exception::class)
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        val editor: Editor? = event.getData(com.intellij.openapi.actionSystem.PlatformDataKeys.EDITOR)
        if (editor == null) {
            Messages.showMessageDialog(project, "No editor found", "Error", Messages.getErrorIcon())
            return
        }

        val selectedCode = editor.selectionModel.selectedText.toString()
        if (selectedCode.isEmpty()) {
            Messages.showMessageDialog(project, "No code selected", "Error", Messages.getErrorIcon())
            return
        }

        val base64Code = Base64.getEncoder().encodeToString(selectedCode.toByteArray())

        val psiFile = PsiDocumentManager.getInstance(project!!).getPsiFile(editor.document)

        var fileType: FileType? = null
        if (psiFile != null) {
            fileType = psiFile.fileType
        }

        if (fileType == null) {
            Messages.showMessageDialog(project, "No file type found", "Error", Messages.getErrorIcon())
            return
        }

        val originalUrl = "https://ray.so/#language=${getFileLanguage(fileType)}&theme=raindrop&padding=16&background=true&code=$base64Code"

        val url = originalUrl.replace("+", "-")

        try {
            Desktop.getDesktop().browse(URI(url))
        } catch (e: Exception) {
            Messages.showMessageDialog(project, "Failed to open URL", "Error", Messages.getErrorIcon())
        }
    }
}