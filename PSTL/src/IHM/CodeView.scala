package IHM
import javax.swing.JPanel
import java.awt.Color
import java.awt.Dimension
import javax.swing.JTabbedPane
import javax.swing.BorderFactory

/**
 * Afficher le code courant
 * deux onglets vue Ocaml / ZAM (avec breakpoints)
 */
class CodeView(Thr : Connector) extends JPanel {
  
  val CodePane = new JTabbedPane
  CodePane.addTab("OCaml", null, new OcamlView(Thr), null)
  CodePane.addTab("Zam", null, new ZamView(Thr), null)
  
  CodePane.setBorder(BorderFactory.createRaisedBevelBorder)
  add(CodePane)
}