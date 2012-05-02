package IHM
import javax.swing.JPanel
import java.awt.Color
import java.awt.Dimension

/**
 * Afficher le code courant
 * deux onglets vue Ocaml / ZAM (avec breakpoints)
 */
class CodeVue(Thr : ZAM.ThreadService) extends JPanel {
	setBackground(Color.white)
	setSize(new Dimension(50 ,50))
}