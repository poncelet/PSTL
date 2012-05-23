package IHM

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.Graphics
import javax.swing.BorderFactory
import java.awt.Color
import javax.swing.JPanel
import java.awt.Component
import java.awt.FlowLayout
import javax.swing.BoxLayout
import scala.collection.mutable.ArrayBuffer

class MyViewPanel(base : Connector, it : Int,  choice : ViewsManager) extends JPanel {
	/* Par defaut (à modifier) une fenetre à au moins la vus du code et de la pile */
	setBorder(BorderFactory.createLineBorder(new Color(555)))
	var current : JPanel = null
	setLayout(new FlowLayout(FlowLayout.LEFT))
	var SousPanel : JPanel = null
	afficher

	def afficher = {
	  SousPanel = new JPanel
	  SousPanel.setLayout(new BoxLayout(SousPanel, BoxLayout.LINE_AXIS))
	  var i = 0
		for(i <-0  to choice.size - 1) {
		  choice.get(i) match {
		  	case 0 => {current = new CodeView(base, it);SousPanel.add(current)}
		  	case 1 => {current = new  ContextView(base, it);SousPanel.add(current)}
		  	case 2 => {current = new  EnvView(base);SousPanel.add(current)}
		  	case 3 => {current = new  ConsoleView(base);SousPanel.add(current)}
		  	case 4 => {current = new  DataView(base, it);SousPanel.add(current)}
		  	case _ => ()
		  }
		  current.addMouseMotionListener(choice.Listener)
		  current.addMouseListener(choice.Listener)
		  current.setAlignmentX(Component.TOP_ALIGNMENT)
		if((i != 0) && ((i%2)==1) ) {
		  add(SousPanel)
		  SousPanel = new JPanel
		  SousPanel.setLayout(new BoxLayout(SousPanel, BoxLayout.LINE_AXIS))
		  }
		}
		if(((choice.size - 1)%2)==0) add(SousPanel)
	}
	
	def modif = {removeAll;afficher;this.repaint()}
	
	def getviews = choice.getviews
}