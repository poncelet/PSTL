package IHM
import javax.swing.JButton
import javax.swing.JTabbedPane
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import javax.swing.JPanel

class ButtonTabComponent(base : Connector, it : Int,  pane : JTabbedPane, mylabel : JPanel) extends JButton("x") with ActionListener {
  var i =  0
  addActionListener(this)
    
  def init = {
    i = pane.indexOfTabComponent(mylabel);
    if( (i == -1)) throw new Exception("Erreur ButtonTabComponent init")
    if(it == 0) setToolTipText("Fermer le programme")
    else setToolTipText("Ouvrir ce thread dans une nouvelle fenetre")
  }
  
  def actionPerformed(e : ActionEvent) = {
    if(it == 0) base.Fin
    //creer fenetre
    val views = pane.getComponentAt(i).asInstanceOf[MyViewPanel].getviews
    base.maxThread(it, views)
    pane.remove(i)
    pane.repaint()
   }
}