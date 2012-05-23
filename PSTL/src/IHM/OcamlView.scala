package IHM
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JScrollPane

class OcamlView(base : Connector, it : Int) extends JPanel {
  
  val textArea = new JTextArea("", 30 , 35)
  if(base.getPath == "") textArea.setText("Chargez un programme .ml si vous voulez le voir ici")
  else textArea.setText(base.getOcmlCode)

  val ScrollPane = new JScrollPane(textArea)
  ScrollPane.setWheelScrollingEnabled(true)
  
  add(ScrollPane) 
//textArea.setFont(new Font("Serif", Font.ITALIC, 16));

}