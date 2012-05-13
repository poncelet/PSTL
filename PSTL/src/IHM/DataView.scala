package IHM
import javax.swing.JPanel
import java.awt.BorderLayout
import javax.swing.JComboBox
import javax.swing.JCheckBox
import javax.swing.table.AbstractTableModel
import javax.swing.JScrollPane
import javax.swing.JTable
import java.awt.Dimension

class DataView (Thr : Connector, it : Int) extends JPanel {
	setLayout(new BorderLayout)
	add(new JCheckBox, BorderLayout.NORTH)
	add(new JComboBox, BorderLayout.NORTH)
	
	val thread = Thr.getThread(it)
	var table : JTable = null
	if(thread != null) {
		val size = thread.stack.getSp-1
		if(size > 0) {
			val t = new MyModel(thread.stack.getSp-1)
			t.setValueAt(0, 0, thread.stack.get(size).toString)
			t.setValueAt(0, 1, thread.getaccu.toString)
			for(i<- size - 1 to 0){
					t.setValueAt(i, 0, thread.stack.get(i).toString)
					t.setValueAt(i, 1, "")}
			table = new JTable(t)
			add(table)
		}
		else {
			table = new JTable(new MyModel(0))
			add(table)
		}
  }
  else {
    table = new JTable(new MyModel(0))
    add(table)
  }
  table.setPreferredScrollableViewportSize(new Dimension(250, 70))
  table.setFillsViewportHeight(true)
  /*table.getSelectionModel().addListSelectionListener(new RowListener());
  table.getColumnModel().getSelectionModel().addListSelectionListener(new ColumnListener());*/
  add(new JScrollPane(table))
	
	class MyModel(size : Int) extends AbstractTableModel {
          private val columnNames = Array("Value", "")
          private var data = new Array[Array[Object]](size)
 
        def getColumnCount = columnNames.size
 
        def getRowCount = data.size
 
        override def getColumnName(col : Int) = columnNames(col)
 
        def getValueAt(row : Int, col : Int) = data(row)(col)
        
        def setValueAt(row : Int, col : Int, v : Object) = data(row)(col) = v
       
        override def getColumnClass(c : Int) = getValueAt(0, c).getClass()
 
    }
}