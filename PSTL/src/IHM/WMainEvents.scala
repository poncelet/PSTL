package IHM
import java.awt.event.WindowListener
import java.awt.event.WindowEvent
import scala.collection.mutable.ArrayBuffer

class WMainEvents(v : ArrayBuffer[Views], base : Connector, it : Int) extends WindowListener {
  
  val MyWindows = new Fenetre(v, base, it)
  MyWindows.addWindowListener(this)
  
  	def windowActivated(event : WindowEvent) = {
	}
	def windowDeactivated(event : WindowEvent) = {
	  
	}
	override def windowClosing(event : WindowEvent) = {
	  if(it == 0) base.Fin
	  else {
	    base.reduce(it)
	  }
	}
	def windowClosed(event : WindowEvent) = {
	  
	}
	def windowIconified(event : WindowEvent) = {
	  
	}
	def windowDeiconified(event : WindowEvent) = {
	  
	}
	def windowOpened(event : WindowEvent) = {
	  
	}
	
	//fermer la fenetre
	def close = MyWindows.setVisible(false)
	
	def getviews = MyWindows.getviews
	
	def addTab(it : Int, v : ArrayBuffer[Views]) = MyWindows.addTab(it, v)
	
	def max(v : ArrayBuffer[Views]) = {
	  MyWindows.bindViews(v)
	  MyWindows.setVisible(true)
	}
	
	def MajView = MyWindows.MajView
	
	def MajPan = MyWindows.MajPan
}