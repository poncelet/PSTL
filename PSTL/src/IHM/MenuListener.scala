package IHM
import java.awt.event.ActionListener
import java.awt.event.ActionEvent

class MyMenuListener(t: String) extends ActionListener {
  
def actionPerformed(e : ActionEvent) = t match {
	case "New" => printf("Nouveau fic")
	case "Open" => printf("Ouverture fic")
	case "Save" => printf("Sauver Fic")
	case "Quit" => printf("Quitter")
    case _ => printf("Inconnu")
   }
}