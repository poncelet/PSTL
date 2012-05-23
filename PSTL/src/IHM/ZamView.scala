package IHM
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JScrollPane
import java.awt.Dimension
import javax.swing.BorderFactory
import java.awt.Color
import javax.swing.JEditorPane
import javax.swing.table.AbstractTableModel
import javax.swing.JTable
import java.awt.Font
import javax.swing.BoxLayout
import java.awt.Component
import javax.swing.Box
import javax.swing.event.TableModelListener
import javax.swing.event.TableModelEvent
import javax.swing.table.TableModel
import java.awt.event.MouseListener
import java.awt.event.MouseEvent

class ZamView (base : Connector, it : Int) extends JPanel {
  setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS))
	  
  val textArea = new JTextArea("", 30 , 35)
  if(base.getPath == "") textArea.setText("Aucun programme Zam n'est chargé")
  else textArea.setText(base.getOcmlCode)
   
  textArea.setFont(new Font("Serif", Font.PLAIN, 13))
  
  val ScrollPane = new JScrollPane(textArea)
  ScrollPane.setWheelScrollingEnabled(true)
  
  val t = new MyModel(textArea.getRows)
  if(base.BreakPoints.size > 0) {
	  var j = 0
	  for(i<-1 to 30) {
		  if(i != base.BreakPoints(j)) t.setValueAt((i-1),0, false.asInstanceOf[Object])
		  else {
			  t.setValueAt((i-1),0, true.asInstanceOf[Object])
			  if(j+1 < base.BreakPoints.size) j = j + 1
		  }
       	}
  	}
  else for(i<-0 to 30-1) t.setValueAt(i,0, false.asInstanceOf[Object])
  val table = new JTable(t)
   
  table.getColumnModel.getColumn(0).setPreferredWidth(25)
  table.setRowHeight(17)
  
  table.setAlignmentX(Component.BOTTOM_ALIGNMENT)
  ScrollPane.setAlignmentX(Component.BOTTOM_ALIGNMENT)
  
 table.addMouseListener(t)
  add(table)
  add(ScrollPane) 
  textArea.setWrapStyleWord(true)
  
  class MyModel(size : Int) extends AbstractTableModel with MouseListener {
          private val columnNames = Array("Brk")
          private var data = new Array[Array[Object]](size)
          for(i<-0 to size-1) {
            data(i) = new Array[Object](1)
          }
 
        def getColumnCount = columnNames.size
 
        def getRowCount = data.size
 
        override def getColumnName(col : Int) = columnNames(col)
 
        def getValueAt(row : Int, col : Int) = data(row)(col)
        
        def setValueAt(row : Int, col : Int, v : Object) = data(row)(col) = v
       
        override def getColumnClass(c : Int) = getValueAt(0, c).getClass()
        
        def tableChanged(e : TableModelEvent) = {
        	val row = e.getFirstRow
        	val column = e.getColumn
        	val model = e.getSource.asInstanceOf[TableModel]
        	val data = model.getValueAt(row, column)
        	println("row " + row + " column " + column)
        	if(data.asInstanceOf[Boolean] == true) {
        	  model.setValueAt(false.asInstanceOf[Object], row, column)
        	}
        	else {
        	  model.setValueAt(true.asInstanceOf[Object], row, column)
        	}
        }
        
        override def mouseClicked(arg : MouseEvent) {
        	println(textArea.getCaretPosition + " et " + textArea.getSelectionStart)
        	val it = (arg.getX - table.getX) / table.getRowHeight
        	//println(table.get)
        	println(" it " + it)
        }
        
        override def mouseReleased(arg : MouseEvent) {}
        override def mouseEntered(arg : MouseEvent) {}
        override def mouseExited(arg : MouseEvent) {}
        override def mousePressed(arg : MouseEvent) {}

        
  }
}