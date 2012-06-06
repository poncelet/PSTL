package IHM
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JScrollPane
import javax.swing.JTextPane
import java.awt.Dimension
import javax.swing.text.StyleContext
import javax.swing.text.StyleConstants
import java.awt.Color

class ConsoleView (base : Connector) extends Views {
	val textArea = new JTextPane
	
	textArea.setEditable(false)
	textArea.setPreferredSize(new Dimension(350, 150))
	
	// définition des styles
	val defaut = textArea.getStyle("default")
	val style1 = textArea.addStyle("style1", defaut)
      StyleConstants.setForeground(style1, Color.RED)
      
      MajView
      
  
    val ScrollPane = new JScrollPane(textArea)
    ScrollPane.setWheelScrollingEnabled(true)
  
    add(ScrollPane) 
	
   override  def getId = 3
    
   override def MajView = {
	  
	  if(textArea.getText.size > 0) textArea.setText("")
	   
	  var out = base.getEnv.getOutStream
	  if(out == "") out += "Aucune sortie\n"
	  
	  var err = base.getEnv.getOutStream
	  if(err == "") err += "Aucune Erreur\n"
	    
	  val sDoc = textArea.getDocument
      var pos = 0
      sDoc.insertString(pos, out, defaut);pos+=out.length()
      sDoc.insertString(pos, err, style1);pos+=err.length()
      
	}
}