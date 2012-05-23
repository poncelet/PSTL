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
class CodeView(base : Connector, it : Int) extends JPanel {
  
  val CodePane = new JTabbedPane
  CodePane.addTab("OCaml", null, new OcamlView(base, it), null)
  CodePane.addTab("Zam", null, new ZamView(base, it), null)
  
  CodePane.setBorder(BorderFactory.createRaisedBevelBorder)
  add(CodePane)
}