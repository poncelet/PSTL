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
class CodeView(base : Connector, it : Int) extends Views {
  
  val CodePane = new JTabbedPane
  val OcmlV = new OcamlView(base, it)
  val ZamV = new ZamView(base, it)
  
  CodePane.addTab("OCaml", null, OcmlV, null)
  CodePane.addTab("Zam", null, ZamV , null)
  
  CodePane.setBorder(BorderFactory.createRaisedBevelBorder)
  add(CodePane)
  
  override def getId = (it * 100)
  
  override def MajView = {OcmlV.MajView;ZamV.MajView}
  
}