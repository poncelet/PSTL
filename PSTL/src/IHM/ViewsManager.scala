package IHM
import scala.collection.mutable.ArrayBuffer
import java.awt.event.ActionListener
import java.awt.event.ItemEvent
import java.awt.event.ActionEvent
import java.awt.event.ItemListener
import javax.swing.JCheckBoxMenuItem
import javax.swing.JPanel

class ViewsManager(Views : ArrayBuffer[Views], it : Int) extends ItemListener {
  
  val Listener = new ComponentListening(this)
  val Boxes = new Array[JCheckBoxMenuItem](5)
  private var bind = 0
  var Pan : MyViewPanel = null

  def add(v : Views) = Views += v
  
  def sub(v : Int) = {
    var i = 0
    var it = 0
    Views.foreach(ent =>{
      if(Views(i).getId == v) it = i
      i = i + 1})
      Views.remove(it)
    }
  
  def contains(id : Int) = {
    var b = false
    Views.foreach(ent=>{if(ent.getId == id) b = true})
    b
  }
  
  def size = Views.size
  
  def getViewId(i : Int) : Int = {Views(i).getId % 100}
  
  def getThreadId(i : Int) = {Views(i).getId / 100}
		  
  def bindBox(cbMenuItem : JCheckBoxMenuItem) = {
    Boxes(bind) = cbMenuItem
    bind = bind + 1
  }
  
  def bindPan(p : MyViewPanel) = Pan = p
  
  def itemStateChanged(e : ItemEvent) {
   val source  = e.getItemSelectable
   var itview = 0
    if (source == Boxes(0)) {
        itview = 0
    } else   if (source == Boxes(1)) {
        itview = 1
    } else   if (source == Boxes(2)) {
        itview = 2
    } else   if (source == Boxes(3)) {
        itview = 3
    } else   if (source == Boxes(4)) {
        itview = 4
    }
   
    if (e.getStateChange() == ItemEvent.DESELECTED) sub(itview + (it * 100))
    else Pan.addView(itview + (it * 100))
    //reafficher
    Pan.modif
}
  
  def getviews = Views
  
  def change(str : String, change : Int) = {
    var v = 0
    if(str.equals("class IHM.CodeView")) {
      v = 0
    }
     if(str.equals("class IHM.ContextView")) {
      v = 1
    }
      if(str.equals("class IHM.EnvView")) {
      v = 2
    }
       if(str.equals("class IHM.ConsoleView")) {
      v = 3
    }
        if(str.equals("class IHM.DataView")) {
      v = 4
    }
    v = v + (100 * it)
    var i = 0
    var j = 0
    Views.foreach(ent =>{
      if(Views(i).getId == v) j = i
      i = i + 1})
   if( ((j + change) >= 0) && ((j + change) <= (Views.size - 1))) {
     val tmp = Views(j)
     Views(j) = Views(j + change)
     Views(j + change) = tmp
     Pan.modif
   }
  }

}