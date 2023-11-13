import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import java.net.URI
import java.util.*
import java.awt.Desktop
import kotlin.jvm.Throws

class RaySoLink : AnAction() {
    @Throws(Exception::class)
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        val editor: Editor? = event.getData(com.intellij.openapi.actionSystem.PlatformDataKeys.EDITOR)
        if (editor == null) {
            Messages.showMessageDialog(project, "No editor found", "Error", Messages.getErrorIcon())
            return
        }

        val selectedCode = editor.selectionModel.selectedText
        if (selectedCode == null || selectedCode.isEmpty()) {
            Messages.showMessageDialog(project, "No code selected", "Error", Messages.getErrorIcon())
            return
        }

        val base64Code = Base64.getEncoder().encodeToString(selectedCode.toByteArray())

        val url = "https://ray.so/#language=php&theme=raindrop&padding=16&background=true&code=$base64Code"

        try {
            Desktop.getDesktop().browse(URI(url))
        } catch (e: Exception) {
            Messages.showMessageDialog(project, "Failed to open URL", "Error", Messages.getErrorIcon())
        }
    }
}