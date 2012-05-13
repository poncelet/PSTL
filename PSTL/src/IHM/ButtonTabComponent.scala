package IHM
import javax.swing.JButton
import javax.swing.JTabbedPane
import java.awt.event.ActionListener
import java.awt.event.ActionEvent

class ButtonTabComponent(Thr : Connector, pane : JTabbedPane) extends JButton("x") with ActionListener {
  var i =  0
  var name = ""
  addActionListener(this)
    
  def init = {
    pane.indexOfTabComponent(ButtonTabComponent.this);
    if( (i == -1)) throw new Exception("Erreur ButtonTabComponent init")
    name = pane.getTitleAt(i)
    if(name.equals("main")) setToolTipText("Fermer le programme")
    else setToolTipText("Ouvrir ce thread dans une nouvelle fenetre")
  }
  
  def actionPerformed(e : ActionEvent) = {
    if(name.equals("main")) println("Fin!"); exit(0)
    //creer fenetre
    Thr.maxThread(name)
    pane.remove(i)
   }
}