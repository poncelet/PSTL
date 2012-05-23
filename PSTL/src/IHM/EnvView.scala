package IHM
import javax.swing.JPanel
import javax.swing.table.AbstractTableModel
import javax.swing.JScrollPane
import javax.swing.JTable
import java.awt.Dimension

class EnvView(base : Connector) extends JPanel{
  val Env = base.getEnv
  var table : JTable = null
  if(Env != null) {
    val size = Env.getglob.size
    if(size > 0) {
      val t = new MyModel(size)
      var i = 0
      Env.getglob.foreach(ent => {t.setValueAt(i,0, ent._1.toString);t.setValueAt(i,1, ent._2.toString);i = i + 1})
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
  table.setPreferredScrollableViewportSize(new Dimension(200, 50))
  table.setFillsViewportHeight(true)
  /*table.getSelectionModel().addListSelectionListener(new RowListener());
  table.getColumnModel().getSelectionModel().addListSelectionListener(new ColumnListener());*/
  add(new JScrollPane(table))
  
	class MyModel(size : Int) extends AbstractTableModel {
          private val columnNames = Array("Field", "Valeur")
          private var data = new Array[Array[Object]](size)
           for(i<-0 to size-1) {
            data(i) = new Array[Object](2)
          }
 
        def getColumnCount = columnNames.size
 
        def getRowCount = data.size
 
        override def getColumnName(col : Int) = columnNames(col)
 
        def getValueAt(row : Int, col : Int) = data(row)(col)
        
        def setValueAt(row : Int, col : Int, v : Object) = data(row)(col) = v
       
        override def getColumnClass(c : Int) = getValueAt(0, c).getClass()
 
    }
}