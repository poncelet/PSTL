package IHM
import javax.swing.JPanel
import java.awt.Color
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.BorderFactory
import java.awt.BorderLayout
import javax.swing.Box
import scala.collection.mutable.ArrayBuffer
import javax.swing.JTable
import javax.swing.table.AbstractTableModel
import javax.swing.JScrollPane
import javax.swing.table.DefaultTableModel

/**
 * Affichage du context de la thread courante idée de base (pile et accumulateur)
 */
class ContextView(base : Connector, it : Int) extends Views {
  var thread = base.getThread(it)
  var t = new MyModel(1)
  var table = new JTable(t)
  
  MajView
  
  table.setPreferredScrollableViewportSize(new Dimension(250, 70))
  table.setFillsViewportHeight(true)
  add(new JScrollPane(table))
  
 override def getId = if(it == 0) 1 else ((it * 100) + 1)
 
 override def MajView = {
   
   if(thread == null) thread = base.getThread(it)
   
   if(thread != null) {
   
     val size = thread.stack.getSp
     val s = t.getRowCount
     
     if(s > 0) {
       for(i<-0 to s-1) t.removeRow(0)
       t.fireTableStructureChanged
     }
     
     if(size > 0) {
       
    	t.addRow(Array[Object](Normalize(thread.stack.get(0)), Normalize(thread.getaccu)))
    	for(i<-1 to size-1) t.addRow(Array[Object](Normalize(thread.stack.get(i)), ""))
      }
     else {
       t.addRow(Array[Object]("", Normalize(thread.getaccu))) 
     }
   }
  }
  
  def Normalize(v : ZAM.Value) = {
    var st = ""
    if(v.isInstanceOf[ZAM.Zamblock]) {
    	   st += "T : " +  v.asInstanceOf[ZAM.Zamblock].gettag + " " + v.asInstanceOf[ZAM.Zamblock].getsize
    	 }
    else st += v.toString
    st
  }
	
	class MyModel(size : Int) extends AbstractTableModel {
          private val columnNames = Array("Stack", "Accu")
          private var data = new ArrayBuffer[Array[Object]](size)
 
        override def getColumnCount = columnNames.size
 
        override def getRowCount = if(data == null) 0 else data.size
 
        override def getColumnName(col : Int) = columnNames(col)
 
        override def getValueAt(row : Int, col : Int) = data(row)(col)
        
         def addRow(v : Array[Object]) = {
          data += v
          fireTableRowsInserted(data.size-1, data.size -1)
        }
          
         def removeRow(i : Int) = {data.remove(i)}
        
        def setValueAt(row : Int, col : Int, v : Object) = {data(row)(col) = v;fireTableCellUpdated(row, col)}
       
        override def getColumnClass(c : Int) = getValueAt(0, c).getClass
 
        override def isCellEditable(row : Int,col : Int) = true
 
    }
}
