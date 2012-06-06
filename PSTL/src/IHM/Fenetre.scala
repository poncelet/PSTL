package IHM
import java.awt.event.WindowListener
import javax.swing.JFrame
import javax.swing.JMenuBar
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JSeparator
import java.awt.Dimension
import javax.swing.JPanel
import java.awt.BorderLayout
import java.awt.Graphics
import javax.imageio.ImageIO
import java.io.File
import java.awt.Color
import java.awt.FlowLayout
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.ImageIcon
import javax.swing.Icon
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.RenderingHints
import java.awt.Component
import javax.swing.JTabbedPane
import scala.collection.mutable.ArrayBuffer
import javax.swing.JLabel
import javax.swing.JCheckBoxMenuItem

class Fenetre (v : ArrayBuffer[Views], base : Connector, it : Int) extends JFrame {
  
	val conteneurP = getContentPane
	
	var MyViews : ViewsManager = null
	if(v != null) MyViews = new  ViewsManager(v, it)
	else {
	  val id = it * 100
	  MyViews = new ViewsManager(new ArrayBuffer[Views], it)
	}
	
	val CenterTab = new JTabbedPane
	
	setName(base.getThreadName(it))
	
	val mB = new JMenuBar
	
	if(it == 0) {
	
	val Fmenu = new JMenu("File")
	val submenu = new JMenu("Property")
	
	/* Menu a faire seulement pour la premiere fenetre */
	val NewMenu = new JMenuItem("New")
	NewMenu.addActionListener(new MyMenuListener("New", base))
	Fmenu.add(NewMenu)
	val OpenMenu = new JMenuItem("Open")
	OpenMenu.addActionListener(new MyMenuListener("Open", base))
	Fmenu.add(OpenMenu)
	val SaveMenu = new JMenuItem("Save")
	SaveMenu.addActionListener(new MyMenuListener("Save", base))
	Fmenu.add(SaveMenu)
	Fmenu.addSeparator
	val QuitMenu = new JMenuItem("Quit")
	QuitMenu.addActionListener(new MyMenuListener("Quit", base))
	Fmenu.add(QuitMenu)
	
	mB.add(Fmenu)
	
	val Emenu = new JMenu("Examples")
	val E1Menu = new JMenuItem("Ex1")
	E1Menu.addActionListener(new MyMenuListener("E1", base))
	Emenu.add(E1Menu)
	val E2Menu = new JMenuItem("Ex2")
	E2Menu.addActionListener(new MyMenuListener("E2", base))
	Emenu.add(E2Menu)
	val E3Menu = new JMenuItem("Ex3")
	E3Menu.addActionListener(new MyMenuListener("E3", base))
	Emenu.add(E3Menu)
	mB.add(Emenu)
	}
	  
	/*Panel de boutons*/
	val BtnPanel = Box.createHorizontalBox()
	BtnPanel.add(Box.createRigidArea(new Dimension(10, 0)))
	BtnPanel.add(new ExecBtn("exec", base, it))
	
	val MiddlPanel = new JPanel
	MiddlPanel.add(new FowardBtn("Fwd", base, it))
	MiddlPanel.add(new BackBtn("Back", base, it))
	MiddlPanel.add(new ToBrkBtn("ToBrk", base, it))
	MiddlPanel.add(new RestartBtn("Restart", base, it))
	
	BtnPanel.add(MiddlPanel, BorderLayout.CENTER)
	
	BtnPanel.add(Box.createHorizontalGlue())
	BtnPanel.add(new OptBtn("Opt", base, it))
	BtnPanel.add(Box.createRigidArea(new Dimension(10, 0)))
		
	/* BtnPanel.setBackground(new Color(301))*/
	conteneurP.add(BtnPanel, BorderLayout.NORTH)
    
	/* Centre */
	if(it == 0) {
		for(i<-0 to base.getnbthread - 1) {
			if(base.threadMin(i)) {
				val ViewPanel = new MyViewPanel(base, it, MyViews)
				MyViews.bindPan(ViewPanel)
				CenterTab.addTab("", null, ViewPanel, "State of " + base.getThreadName(i))
				val TabPanel = new JPanel
				TabPanel.add(new JLabel(base.getThreadName(i)))
				CenterTab.setTabComponentAt(i, TabPanel)
				val end = new ButtonTabComponent(base, it, CenterTab, TabPanel)
				TabPanel.add(end)
				end.init
			}
		}
		conteneurP.add(CenterTab, BorderLayout.CENTER)
	}
	else {
	  val ViewPanel = new MyViewPanel(base, it, MyViews)
	  MyViews.bindPan(ViewPanel)
	  conteneurP.add(ViewPanel, BorderLayout.CENTER)
	}
	
	val Vmenu = new JMenu("View")
	
	var cbMenuItem : JCheckBoxMenuItem = null
	var idThread = 0
	for(i<-0 to 4) {
	  i match {
	    case 0 => cbMenuItem = new JCheckBoxMenuItem("Code View");
	    case 1 => cbMenuItem = new JCheckBoxMenuItem("Context View");
	    case 2 => cbMenuItem = new JCheckBoxMenuItem("Global View");
	    case 3 => cbMenuItem = new JCheckBoxMenuItem("Consol View");
	    case 4 => cbMenuItem = new JCheckBoxMenuItem("Data View");
	  }
	  MyViews.bindBox(cbMenuItem)
	  if( (i == 2) || (i == 3)) idThread = 0
	  else idThread = it
	  if(MyViews.contains((idThread*100)+i)) cbMenuItem.setSelected(true)
	  cbMenuItem.addItemListener(MyViews)
	  Vmenu.add(cbMenuItem)
	}
	
	mB.add(Vmenu)
	setJMenuBar(mB)
	 
	setMinimumSize(new Dimension(800, 900))
	setVisible(true)
	
	def getviews = MyViews.getviews
	
	def addTab(it : Int, v : ArrayBuffer[Views]) {
		val ViewPanel = new MyViewPanel(base, it, new ViewsManager(v, it)) //Pas de modification menu
		CenterTab.addTab("", null, ViewPanel, "State of " + base.getThreadName(it))
		val TabPanel = new JPanel
		TabPanel.add(new JLabel(base.getThreadName(it)))
		val end = new ButtonTabComponent(base, it, CenterTab, TabPanel)
		TabPanel.add(end)
		val i = CenterTab.getTabCount - 1
		CenterTab.setTabComponentAt(i, TabPanel)
		end.init
		CenterTab.getTabComponentAt(i).repaint()
		setVisible(true)
	}
	
	def bindViews(v : ArrayBuffer[Views]) = {
	  MyViews = new  ViewsManager(v, it)
	  conteneurP.removeAll
	  val ViewPanel = new MyViewPanel(base, it, MyViews)
	  MyViews.bindPan(ViewPanel)
	  conteneurP.add(BtnPanel, BorderLayout.NORTH)
	  conteneurP.add(ViewPanel, BorderLayout.CENTER)
	}
	
	//mettre les views en array[views]
	def MajView = {
	  MyViews.getviews.foreach(ent=>{ent.MajView})
	}
	
	def MajPan = {
	  CenterTab.updateUI
	}
    
}

/** Create an ARGB BufferedImage 
BufferedImage img = ImageIO.read(imageSrc);
int w = img.getWidth(null);
int h = img.getHeight(null);
BufferedImage bi = new
    BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
Graphics g = bi.getGraphics();
g.drawImage(img, 0, 0, null);


  Create a rescale filter op that makes the image
  50% opaque.
 
float[] scales = { 1f, 1f, 1f, 0.5f };
float[] offsets = new float[4];
RescaleOp rop = new RescaleOp(scales, offsets, null);

 Draw the image, applying the filter 
g2d.drawImage(bi, rop, 0, 0); **/
