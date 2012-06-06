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
import javax.swing.JTextPane
import javax.swing.text.StyleConstants
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import scala.collection.mutable.ArrayBuffer
import javax.swing.Scrollable
import java.awt.Rectangle
import javax.swing.SwingConstants
import javax.swing.JScrollBar
import javax.swing.ScrollPaneConstants

class ZamView (base : Connector, it : Int) extends JPanel {
  setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS))
  
  val PosCode = new ArrayBuffer[Int]
  
  var pc = 0
  var oldpc = 0
  var firstpc = 0
  var line = ""
  var before = ""
  var Tsize = 0
    
  var modif = true
  
  val textArea = new JTextPane
	
	// définition des styles
	textArea.setFont(new Font("Serif", Font.PLAIN, 13))
	
	textArea.setMaximumSize(new Dimension(330, 32))
  	textArea.setPreferredSize(new Dimension(330, 32))
	
	val defaut = textArea.getStyle("default")
	val style1 = textArea.addStyle("style1", defaut)
      StyleConstants.setForeground(style1, Color.blue)
      StyleConstants.setBackground(style1, Color.RED)
      
  val ScrollPane = new JScrollPane(textArea)
  val JScrollV = new MyScrollBar(this)
  JScrollV.setUnitIncrement(17)
  JScrollV.setBlockIncrement(17*5)
  JScrollV.setValueIsAdjusting(false)
  ScrollPane.setVerticalScrollBar(JScrollV)
  ScrollPane.setMaximumSize(new Dimension(330, 17*32))
  ScrollPane.setPreferredSize(new Dimension(330, 17*32))
  
  ScrollPane.setWheelScrollingEnabled(true)
    
  MajView
  
  val t = new MyModel(textArea.getPreferredSize.getHeight.asInstanceOf[Int])
  majBrk
  val table = new JTable(t)
   
  table.getColumnModel.getColumn(0).setPreferredWidth(25)
  table.setRowHeight(17)
  
  table.setAlignmentX(Component.TOP_ALIGNMENT)
  ScrollPane.setAlignmentX(Component.TOP_ALIGNMENT)
  
 table.addMouseListener(t)
  add(table)
  add(ScrollPane)
  
  def majPC = {
    if(base.IsReady) {
    	oldpc = pc
    	pc = base.getThread(it).getpc
    	
    	before = line
    // déscendre le scroll et modifier firstpc
    
    var sizebuf = 40
    if( (PosCode(pc) + sizebuf) > Tsize) sizebuf = Tsize - PosCode(pc) 
    line = textArea.getDocument.getText(PosCode(pc), sizebuf) // 40 -> MAXINSTRSIZE
    val i = line.indexOf("\n")
    line = line.substring(0, i+1) // '\n'
    
     val sDoc = textArea.getDocument
	  if(before != "") {
	    sDoc.remove(PosCode(oldpc), before.size)
	    sDoc.insertString(PosCode(oldpc), before, defaut)
	  }
      sDoc.remove(PosCode(pc), line.size)
      sDoc.insertString(PosCode(pc), line, style1)
      sDoc.insertString(pc + line.size, "", defaut)
    }
  }
  
  def MajView = {
  if(base.getPath == "") textArea.setText("Aucun programme Zam n'est chargé\nok\n")
  else {
    if(modif) {
    try {
      textArea.setText("")
      
    	val stream = new FileInputStream(base.getPath)
    	val buf = new Array[Byte](40)
    	var n = 1
    	
    	val sDoc = textArea.getDocument
    	var pos = 0
    	PosCode += pos
    	line = ""
    	  var c = 'a'
    	 
    	while (n != -1) {
    	  
    		n = stream.read(buf)
    		for(i<-0 to n-1) {
    		  c = buf(i).asInstanceOf[Char]
    		  if( (c == '\n') && (line != "")) {
    		    line += c
    		    sDoc.insertString(pos, line, defaut)
    		    pos += line.size
    		    PosCode += pos
    		    line = ""
    		  }
    		  else {
    		    line += c
    		  }
    		}
    	}
    	stream.close
    }
    catch { 
      case e : IOException => {println("Fichier introuvable")}
    }
     
    modif = false
    }
  }
  
    Tsize = textArea.getText.length
    majPC
  }
  
  def majBrk = {
    if(ScrollPane != null) firstpc = ScrollPane.getVerticalScrollBar.getValue / 17
    if(base.BreakPoints.size > 0) {
	  var j = 0
	  while ( (j < base.BreakPoints.size-1) && (firstpc > base.BreakPoints(j))) { j = j + 1 }
	  for(i<-firstpc to (firstpc + 31)) {
		if( i != base.BreakPoints(j)) t.setValueAt((i-firstpc),0, false.asInstanceOf[Object])
		  		else {
		  				t.setValueAt((i-firstpc),0, true.asInstanceOf[Object])
		  				if(j+1 < base.BreakPoints.size) j = j + 1
		  		}
       	}
  	}
    else for(i<-0 to 32-1) t.setValueAt(i,0, false.asInstanceOf[Object])
  }
  
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
        
        def setValueAt(row : Int, col : Int, v : Object) = {data(row)(col) = v;fireTableCellUpdated(row, col)}
       
        override def getColumnClass(c : Int) = getValueAt(0, c).getClass()
        
        override def isCellEditable(row : Int,col : Int) = true
    
        override def mouseClicked(arg : MouseEvent) {
        	val it = (arg.getY - table.getY) / table.getRowHeight
        	if(getValueAt(it, 0).asInstanceOf[Boolean] == true) {
        	   setValueAt(it, 0, false.asInstanceOf[Object])
        	   base.remBrk(it+firstpc)
        	}
        	else {
        	  setValueAt(it, 0, true.asInstanceOf[Object])
        	  base.addBrk(it + firstpc)
        	}
        }
        
        override def mouseReleased(arg : MouseEvent) {}
        override def mouseEntered(arg : MouseEvent) {}
        override def mouseExited(arg : MouseEvent) {}
        override def mousePressed(arg : MouseEvent) {}

        
  }
}