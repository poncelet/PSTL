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
	var current : Views = null
	setLayout(new FlowLayout(FlowLayout.LEFT))
	var SousPanel : JPanel = null
	afficher

	def afficher = {
	  SousPanel = new JPanel
	  SousPanel.setLayout(new BoxLayout(SousPanel, BoxLayout.LINE_AXIS))
	  if(choice.size == 0) {init}
	  else {
		for(i <-0  to choice.size - 1) {
		  current = choice.getviews(i)
		  
		  SousPanel.add(current)
		  if((i%2)==1) {	      
			  add(SousPanel)
			  SousPanel = new JPanel
			  SousPanel.setLayout(new BoxLayout(SousPanel, BoxLayout.LINE_AXIS))
		  	}
		}
	}
	if((choice.size %2)==1) add(SousPanel)
	}
	
	def init = {
	  add(SousPanel)
	  for(i <-0  to 4) addView((it * 100) + i)
	}
	
	def addView(it : Int) = {
	  val viewId = it % 100
	  val itId = it /100
	  viewId match {
	  		case 0 => {current = new CodeView(base, itId);SousPanel.add(current)}
		  	case 1 => {current = new  ContextView(base, itId);SousPanel.add(current)}
		  	case 2 => {current = new  EnvView(base);SousPanel.add(current)}
		  	case 3 => {current = new  ConsoleView(base);SousPanel.add(current)}
		  	case 4 => {current = new  DataView(base, itId);SousPanel.add(current)}
		  	case _ => ()
	  }
		  
	current.addMouseMotionListener(choice.Listener)
	current.addMouseListener(choice.Listener)
	current.setAlignmentX(Component.TOP_ALIGNMENT)
	
	if( (choice.size!=0) && (choice.size%2)==0 ) {
		SousPanel = new JPanel
		SousPanel.setLayout(new BoxLayout(SousPanel, BoxLayout.LINE_AXIS))
		add(SousPanel)
	}
	
	SousPanel.add(current)
	choice.add(current)
	
	}
	
	def modif = {
	  removeAll
	  afficher
	  repaint()
	  if(it!=0) {
	    choice.getviews.foreach(ent=>{ent.MajView})
	  }
	  if(base.threadMin(it)) {
	   base.MajPan
	    choice.getviews.foreach(ent=>{ent.MajView})
	  }
	}
	
	def getviews = choice.getviews
}