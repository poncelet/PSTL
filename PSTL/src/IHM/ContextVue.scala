package IHM
import javax.swing.JPanel
import java.awt.Color
import java.awt.Dimension

/**
 * Affichage du context de la thread courante idée de base (pile et accumulateur)
 */
class ContextVue(Thr : ZAM.ThreadService) extends JPanel {
	setBackground(Color.yellow)
	setSize(new Dimension(50 ,50))
}
