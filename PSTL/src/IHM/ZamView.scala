package IHM
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JScrollPane
import java.awt.Dimension
import javax.swing.BorderFactory
import java.awt.Color
import javax.swing.JEditorPane

class ZamView (Thr : Connector) extends JPanel {
  val textArea = new JTextArea("", 25 , 35)
  if(Thr.getPath == "") textArea.setText("Aucun programme Zam n'est chargé")
  else textArea.setText(Thr.getOcmlCode)
   
  val ScrollPane = new JScrollPane(textArea)
  ScrollPane.setWheelScrollingEnabled(true)
  
  add(ScrollPane) 
  textArea.setWrapStyleWord(true);
  
}