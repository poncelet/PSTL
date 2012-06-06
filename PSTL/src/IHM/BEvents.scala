package IHM
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import java.awt.Graphics
import javax.swing.JButton
import java.awt.Component
import javax.swing.BorderFactory
import java.awt.Color

abstract class BEvents(name : String) extends JButton(name) with ActionListener {
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
    case ExecBtn(n, base, it) => {
      base.prepare(0)
    }
    case FowardBtn(n, base, it) => {
      base.step(it)
    }
    case BackBtn(n, base, it) => {
      base.backstep(it)
    }
    case ToBrkBtn(n, base, it) => {
      base.avance(it)
    }
    case RestartBtn(n, base, it) => {
      base.restart
    }
    case OptBtn(n, base, it) => printf("Option")
    case _ => printf("Inconnu")
 }
  
 /* override def paint(g :Graphics) = {
   g.draw3DRect(0,0,getSize().width-1, getSize().height-1,true)
  }*/
}

case class ExecBtn(name: String, base : Connector, it : Int) extends BEvents(name)
case class FowardBtn(name: String, base : Connector, it : Int) extends BEvents(name)
case class BackBtn(name: String, base : Connector, it : Int) extends BEvents(name)
case class ToBrkBtn(name: String, base : Connector, it : Int) extends BEvents(name)
case class RestartBtn(name: String, base : Connector, it : Int) extends BEvents(name)
case class OptBtn(name: String, base : Connector, it : Int) extends BEvents(name)