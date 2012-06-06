package IHM
import javax.swing.JScrollBar

class MyScrollBar(obs : ZamView ) extends JScrollBar {

  override def setValue(v : Int) = {
    super.setValue(v)
    obs.majBrk
  }
  
  override def setValues(newValue : Int, newExtent : Int, newMin : Int, newMax : Int) = {
    super.setValues(newValue, newExtent, newMin, newMax)
    obs.majBrk
  }
}