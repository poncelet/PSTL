package IHM
import scala.collection.mutable.ArrayBuffer
import java.awt.event.ActionListener
import java.awt.event.ItemEvent
import java.awt.event.ActionEvent
import java.awt.event.ItemListener
import javax.swing.JCheckBoxMenuItem

class ViewsManager(Views : ArrayBuffer[Int]) extends ItemListener {
  
  val Listener = new ComponentListening(this)
  val Boxes = new Array[JCheckBoxMenuItem](5)
  private var bind = 0
  var Pan : MyViewPanel = null
  
  
  def add(v : Int) = Views += v
  
  def sub(v : Int) = {
    var i = 0
    var it = 0
    Views.foreach(ent =>{
      if(Views(i) == v) it = i
      i = i + 1})
      Views.remove(it)
    }
  
  def contains(v : Int) = Views.contains(v)
  
  def size = Views.size
  
  def get(i : Int) = Views(i)
  
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
   
    if (e.getStateChange() == ItemEvent.DESELECTED) sub(itview)
    else add(itview)
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
    var i = 0
    var it = 0
    Views.foreach(ent =>{
      if(Views(i) == v) it = i
      i = i + 1})
   if( ((it + change) >= 0) && ((it + change) < (Views.size - 1))) {
     Views(it) = Views(it + change)
     Views(it + change) = v
     Pan.modif
     Pan.setVisible(true)
   }
  }

}