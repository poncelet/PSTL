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

class Fenetre (Thr : Connector, it : Int) extends JFrame {
  
	val conteneurP = getContentPane
	
	setName(Thr.getThreadName(it))
	
	if(it == 0) {
	
	val mB = new JMenuBar
	val menu = new JMenu("File")
	val submenu = new JMenu("Property")
	
	/* Menu a faire seulement pour la premiere fenetre */
	val NewMenu = new JMenuItem("New")
	NewMenu.addActionListener(new MyMenuListener("New"))
	menu.add(NewMenu)
	val OpenMenu = new JMenuItem("Open")
	OpenMenu.addActionListener(new MyMenuListener("Open"))
	menu.add(OpenMenu)
	val SaveMenu = new JMenuItem("Save")
	SaveMenu.addActionListener(new MyMenuListener("Save"))
	menu.add(SaveMenu)
	menu.add(new JSeparator)
	val QuitMenu = new JMenuItem("Quit")
	QuitMenu.addActionListener(new MyMenuListener("Quit"))
	menu.add(QuitMenu)
	mB.add(menu)
	setJMenuBar(mB)
	}
	  
	/*Panel de boutons*/
	val BtnPanel = Box.createHorizontalBox()
	BtnPanel.add(Box.createRigidArea(new Dimension(10, 0)))
	BtnPanel.add(new ExecBtn("exec"))
	
	val MiddlPanel = new JPanel
	MiddlPanel.add(new FowardBtn("Fwd"))
	MiddlPanel.add(new BackBtn("Back"))
	MiddlPanel.add(new ToBrkBtn("ToBrk"))
	MiddlPanel.add(new RestartBtn("Restart"))
	
	BtnPanel.add(MiddlPanel, BorderLayout.CENTER)
	
	BtnPanel.add(Box.createHorizontalGlue())
	BtnPanel.add(new OptBtn("Opt"))
	BtnPanel.add(Box.createRigidArea(new Dimension(10, 0)))
		
	/* BtnPanel.setBackground(new Color(301))*/
	conteneurP.add(BtnPanel, BorderLayout.NORTH)
    
	/* Centre */
	if(it == 0) {
		val CenterTab = new JTabbedPane
		for(i<-0 to Thr.getnbthread - 1) {
			if(Thr.threadMin(i)) {
				val ViewPanel = new MyViewPanel(Thr, it, Array(0,1,2,3,4))
				CenterTab.addTab("", null, ViewPanel, "Etat de " + Thr.getThreadName(i))
				val TabPanel = new JPanel
				TabPanel.add(new JLabel(Thr.getThreadName(i)))
				val end = new ButtonTabComponent(Thr, CenterTab)
				TabPanel.add(end)
				CenterTab.setTabComponentAt(i, TabPanel)
				end.init
			}
		}
		conteneurP.add(CenterTab, BorderLayout.CENTER)
	}
	else {
	  val ViewPanel = new MyViewPanel(Thr, it, Array(0,1,2,3,4))
	  conteneurP.add(ViewPanel, BorderLayout.CENTER)
	}
		
	override def paint(g :Graphics) = {
		val image = ImageIO.read(new File("../Images/Fond.jpg"))
		g.drawImage(image, 0, 0, null)
		paintComponents(g)
	}
	 
	/*setFont()*/
	/*setIconImage(new Image)*/
	setMinimumSize(new Dimension(600, 200))
	setVisible(true)
    
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
