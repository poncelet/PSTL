package IHM
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JScrollPane

class ConsoleView (Thr : Connector, it : Int) extends JPanel{
	val textArea = new JTextArea("", 5 , 30)
	
	textArea.setEditable(false)
	textArea.setText("Console")
   
  val ScrollPane = new JScrollPane(textArea)
  ScrollPane.setWheelScrollingEnabled(true)
  
  add(ScrollPane) 
  textArea.setWrapStyleWord(true);
}