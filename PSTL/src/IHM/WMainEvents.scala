package IHM
import java.awt.event.WindowListener
import java.awt.event.WindowEvent
import scala.collection.mutable.ArrayBuffer

class WMainEvents(v : ArrayBuffer[Int], base : Connector, it : Int) extends WindowListener {
  
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
	
	def addTab(it : Int, v : ArrayBuffer[Int]) = MyWindows.addTab(it, v)
	
	def max(v : ArrayBuffer[Int]) = {
	  MyWindows.bindViews(v)
	  MyWindows.setVisible(true)
	}
	
}