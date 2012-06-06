package IHM
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.event.MouseEvent

class ComponentListening(manager : ViewsManager) extends MouseListener with MouseMotionListener {
  
  var x = 0
  var y = 0
  
	override def mouseClicked(arg : MouseEvent) {
	 
	}

	override def mouseDragged(arg : MouseEvent) {
	}
	
	override def mouseMoved(arg : MouseEvent) {
	}

	override def mouseEntered(arg : MouseEvent) {
	}

	override def mouseExited(arg : MouseEvent) {
	}

	override def mousePressed(arg : MouseEvent) {
	  x = arg.getX
	  y = arg.getY
	}

	override def mouseReleased(arg : MouseEvent) {
	var X = x - arg.getX
	if(X < 0) X = (X * -1)
	
	var Y = y - arg.getY
	if(Y < 0) Y = (Y * -1)
	
	if((X > 50) || (Y > 50)) {
	
		if(X > Y) { 
			if( (x - arg.getX) > 0) manager.change(arg.getComponent.getClass.toString, -1)
			else manager.change(arg.getComponent.getClass.toString, 1)
		}
		else {
			if( (y - arg.getY) > 0) manager.change(arg.getComponent.getClass.toString, -2)
			else manager.change(arg.getComponent.getClass.toString, 2)
		}
	}
	}
}