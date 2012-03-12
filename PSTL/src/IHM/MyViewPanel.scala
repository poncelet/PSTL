package IHM
import javax.swing.JPanel
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.Graphics
import javax.swing.BorderFactory
import java.awt.Color

class MyViewPanel(Thr : ZAM.ThreadService, choice : Array[Int]) extends JPanel {
	/* Par defaut (à modifier) une fenetre à au moins la vus du code et de la pile */
	setBorder(BorderFactory.createLineBorder(Color.black))
	afficher()

	def afficher() = {
		setLayout(new GridLayout(2, choice.size/2))
		for(i <-0  to choice.size - 1) choice(i) match {
		case 0 => add(new CodeVue(Thr))
		case 1 => //add(VuePile())
		}
	}
}