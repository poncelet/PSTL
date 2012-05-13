package IHM
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import java.awt.Graphics
import javax.swing.JButton
import java.awt.Component
import javax.swing.BorderFactory
import java.awt.Color

abstract class BEvents(name: String) extends JButton(name) with ActionListener {
  addActionListener(this)
  setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(Color.black),
      getBorder()))
  /*font
  foreground
  seticon(...jpg)
  settooltiptext */
  
  
  def actionPerformed(e : ActionEvent) {
    action(this, e); }
  
  def action(b : BEvents, e : ActionEvent) = b match {
    case ExecBtn(n) => printf("exec")
    case FowardBtn(n) => printf("Un pas en avant")
    case BackBtn(n) => printf("Un pas en arriere")
    case ToBrkBtn(n) => printf("Avancer jusqu'au prochain Break Point")
    case RestartBtn(n) => printf("Redemarrer le programme")
    case OptBtn(n) => printf("Option")
    case _ => printf("Inconnu")
 }
  
 /* override def paint(g :Graphics) = {
   g.draw3DRect(0,0,getSize().width-1, getSize().height-1,true)
  }*/
}

case class ExecBtn(name: String) extends BEvents(name)
case class FowardBtn(name: String) extends BEvents(name)
case class BackBtn(name: String) extends BEvents(name)
case class ToBrkBtn(name: String) extends BEvents(name)
case class RestartBtn(name: String) extends BEvents(name)
case class OptBtn(name: String) extends BEvents(name)