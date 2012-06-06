package IHM
import javax.swing.JPanel
import javax.swing.table.AbstractTableModel
import javax.swing.JScrollPane
import javax.swing.JTable
import java.awt.Dimension
import scala.collection.mutable.ArrayBuffer

class EnvView(base : Connector) extends Views {
  val Env = base.getEnv
  var t = new MyModel(1)
  var table = new JTable(t)
  
  MajView
  
  table.setPreferredScrollableViewportSize(new Dimension(200, 50))
  table.setFillsViewportHeight(true)
  add(new JScrollPane(table))
  
  def getId = 2
  
   override def MajView = {
    
    if(Env != null) {
     val size = Env.getglob.size
     val s = t.getRowCount
     
     if(s > 0) {
       for(i<-0 to s-1) t.removeRow(0)
       t.fireTableStructureChanged
     }
     
     if(size > 0) {
       
    	Env.getglob.foreach(ent => {t.addRow(Array[Object](ent._1.toString, Normalize(ent._2)))})
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
          private val columnNames = Array("Field", "Valeur")
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