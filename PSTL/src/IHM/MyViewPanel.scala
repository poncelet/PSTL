package IHM

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.Graphics
import javax.swing.BorderFactory
import java.awt.Color
import javax.swing.JPanel
import java.awt.Component

class MyViewPanel(Thr : Connector, it : Int,  choice : Array[Int]) extends JPanel {
	/* Par defaut (à modifier) une fenetre à au moins la vus du code et de la pile */
	setBorder(BorderFactory.createLineBorder(Color.GREEN))
	var pred : JPanel = null
	var current : JPanel = null
	afficher()

	def afficher() = {
		for(i <-0  to choice.size - 1) {
		  choice(i) match {
		  	case 0 => {current = new CodeView(Thr);add(current)}
		  	case 1 => {current = new  ContextView(Thr, it);add(current)}
		  	case 2 => {current = new  EnvView(Thr);add(current)}
		  	case 3 => {current = new  ConsoleView(Thr, it);add(current)}
		  	case 4 => {current = new  DataView(Thr, it);add(current)}
		  	case _ => ()
		  }
		}
	}
}