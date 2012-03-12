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

class Fenetre (Thr : ZAM.ThreadService) extends JFrame {
  
    var WinSize = majsize
    println( WinSize.height + " and" +WinSize.width)
	val conteneurP = getContentPane
	
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
    
		/* Image fond */
    	val FondPanel = new JPanel() {
	  setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS))
	  
		/*Panel de boutons*/
		val BtnPanel = new JPanel()
		BtnPanel.setLayout(new BoxLayout(BtnPanel, BoxLayout.LINE_AXIS))
		BtnPanel.add(Box.createRigidArea(new Dimension(10, 0)))
		BtnPanel.add(new ExecBtn("exec"))
		BtnPanel.add(new FowardBtn("Fwd"))
		BtnPanel.add(new BackBtn("Back"))
		BtnPanel.add(new ToBrkBtn("ToBrk"))
		BtnPanel.add(new RestartBtn("Restart"))
		BtnPanel.add(Box.createHorizontalGlue())
		BtnPanel.add(new OptBtn("Opt"))
		
		
		/* BtnPanel.setBackground(new Color(301))*/
		add(BtnPanel)
		add(Box.createRigidArea(new Dimension(0, 10)))
    
		/* Vus */
		val ViewPanel = new MyViewPanel(Thr, Array(0,1))
		add(ViewPanel)
		
		/* fond */
		setOpaque(true)
		
			override def paint(g :Graphics) = {
		  	WinSize = majsize
		  	if(WinSize.height != 0) { setSize(WinSize)
		  	  println("maintenant : " + WinSize.height + " and " + WinSize.width)
		  	}
			val image = ImageIO.read(new File("../Images/Fond.jpg"))
			g.drawImage(image, 0, 0, null)
			super.paintChildren(g)
			}
	  
		}
	
    conteneurP.add(FondPanel,  BorderLayout.LINE_START)
    
	/*setFont()*/
	/*setIconImage(new Image)*/
	setMinimumSize(new Dimension(200, 200))
	pack
	setVisible(true)
    
    def majsize = super.getSize()
}