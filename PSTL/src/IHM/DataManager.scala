package IHM
import java.awt.event.ItemListener
import java.awt.event.ItemEvent
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import javax.swing.JComboBox

class DataManager(comp : DataView) extends ItemListener with ActionListener {

  var select = ""
    
  def itemStateChanged(e : ItemEvent) = { 
    if (e.getStateChange() == ItemEvent.DESELECTED) comp.getTab.setVisible(false)
    else comp.getTab.setVisible(true)
    comp.repaint()
  }
  
   def actionPerformed(e : ActionEvent) = {
        val cb = e.getSource.asInstanceOf[JComboBox]
        val selection = cb.getSelectedItem
        if(selection != null) {
        	select = selection.toString
        	comp.MajView
        }
    }
   
   def getSelect = select
   
}