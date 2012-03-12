package IHM
import java.awt.event.WindowListener
import java.awt.event.WindowEvent

class WMainEvents(Thr : ZAM.ThreadService) extends WindowListener {
  
  val MyWindows = new Fenetre(Thr)
  MyWindows.addWindowListener(this)
  
  	def windowActivated(event : WindowEvent) = {
	}
	def windowDeactivated(event : WindowEvent) = {
	  
	}
	override def windowClosing(event : WindowEvent) = {
	  println("Byebye")
	  System.exit(0)
	}
	def windowClosed(event : WindowEvent) = {
	  
	}
	def windowIconified(event : WindowEvent) = {
	  
	}
	def windowDeiconified(event : WindowEvent) = {
	  
	}
	def windowOpened(event : WindowEvent) = {
	  
	}
	def getheight = MyWindows.getHeight()
	def getwidth = MyWindows.getWidth()
	
}