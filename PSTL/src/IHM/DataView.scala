package IHM
import javax.swing.JPanel
import java.awt.BorderLayout
import javax.swing.JCheckBox
import javax.swing.table.AbstractTableModel
import javax.swing.JScrollPane
import javax.swing.JTable
import java.awt.Dimension
import scala.collection.mutable.ArrayBuffer
import javax.swing.JComboBox

class DataView (base : Connector, it : Int) extends Views {
	setLayout(new BorderLayout)
	var thread = base.getThread(it)
	val Manager = new DataManager(this)
	val NPanel = new JPanel
	
	val Check = new JCheckBox
	Check.setSelected(true)
	Check.addItemListener(Manager)
	NPanel.add(Check)
	
	var Vars : ArrayBuffer[String] = null
	if(thread != null) Vars = base.getTables(it)
	val JCombo = new JComboBox
	JCombo.setEditable(false)
	
	JCombo.addActionListener(Manager)
	NPanel.add(JCombo)
	add(NPanel, BorderLayout.NORTH)
	
	var t = new MyModel(1)
	var table = new JTable(t)
	if(Vars == null) { Vars = new ArrayBuffer[String]; Vars += "Aucun Tableau"}
		
	Vars.foreach(ent => JCombo.addItem(ent))
	MajView
	
  table.setPreferredScrollableViewportSize(new Dimension(250, 70))
  table.setFillsViewportHeight(true)
  add(new JScrollPane(table))
  
  def getTab = table
  
  override def getId = if(it == 0) 4 else ((it * 100) + 4)
  
   override def MajView = {
   
    if(thread == null) thread = base.getThread(it)
    
   if( (thread != null) && (table.isVisible)) {
   
    val choice = Manager.getSelect
    var c = ""
    if(choice.size > 6) c = choice.substring(0, 7)
    else c =  choice
    
     Vars.clear
     Vars = base.getTables(it)
     JCombo.removeAllItems
     Vars.foreach(ent => {JCombo.addItem(ent)})
     if(Vars.contains(choice)) JCombo.setSelectedItem(choice)
     else {	JCombo.setSelectedIndex(0)
    	 	c = "Stack"
     		}
     var tab : ArrayBuffer[ZAM.Value] = null

    c match {
      case "Stack" => {
    	 tab = thread.stack.stk
      }
      case "Stack :" => {
    	val i = Manager.getSelect(8).asDigit
    	tab = thread.stack.get(i).asInstanceOf[ZAM.Zamblock].getval.tab
      }
      case "Accu" => {
    	 tab = thread.getaccu.asInstanceOf[ZAM.Zamblock].getval.tab
      }
      case "Env" => {
    	 tab = thread.getTabenv.tab
      }
      case "Glob" => {
    	tab = new ArrayBuffer[ZAM.Value]
    	base.getEnv.getglob.foreach(ent  => {tab += ent._2})
      }
      case "Glob : " => {
    	val i = Manager.getSelect(7).asDigit
    	tab = base.getEnv.getglob.apply(i).asInstanceOf[ZAM.Zamblock].getval.tab
      }
      case _ => ()
    }
     
     if(tab != null) {
    	  val size = tab.size
    	  val s = t.getRowCount
    	  if(s > 0) {
    		  for(i<-0 to s-1) t.removeRow(0)
    		  t.fireTableStructureChanged
    	  }
     
    	  if(size > 0) {
       
    		  var i = 0
    		  while(i < size) {
    			  if(i+1 < size) {
    				  t.addRow(Array[Object](Normalize(tab(i)), Normalize(tab(i+1))))
    				  i = i + 2
    			  }
    			  else {
    			    t.addRow(Array[Object](Normalize(tab(i)), ""))
    			    i = i + 1
    			  }
    		  }
    	  }
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
          private val columnNames = Array("Value", "")
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