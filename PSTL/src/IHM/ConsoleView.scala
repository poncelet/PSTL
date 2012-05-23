package IHM
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JScrollPane

class ConsoleView (base : Connector) extends JPanel{
	val textArea = new JTextArea("", 10 , 45)
	
	textArea.setEditable(false)
	textArea.setText("Console\n")
   
  val ScrollPane = new JScrollPane(textArea)
  ScrollPane.setWheelScrollingEnabled(true)
  
  add(ScrollPane) 
  textArea.setWrapStyleWord(true);
}