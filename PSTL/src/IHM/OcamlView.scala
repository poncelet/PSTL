package IHM
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JScrollPane

class OcamlView(Thr : Connector) extends JPanel {
  
  val textArea = new JTextArea("", 25 , 35)
  if(Thr.getPath == "") textArea.setText("Chargez un programme .ml si vous voulez le voir ici")
  else textArea.setText(Thr.getOcmlCode)

  val ScrollPane = new JScrollPane(textArea)
  ScrollPane.setWheelScrollingEnabled(true)
  
  add(ScrollPane) 
//textArea.setFont(new Font("Serif", Font.ITALIC, 16));

}