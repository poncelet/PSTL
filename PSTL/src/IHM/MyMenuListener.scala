package IHM
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import javax.swing.JFileChooser
import java.io.File

class MyMenuListener(name: String, base : Connector) extends ActionListener {
  val choose = new JFileChooser
  
def actionPerformed(e : ActionEvent) = name match {
	case "New" => {	printf("Nouveau fic")
					val returnVal = choose.showOpenDialog(MyFileChooser)

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						val file = choose.getSelectedFile()
						println("ok charger" + file.toString() )
					}
				}
	case "Open" => { printf("Ouverture fic")
					val returnVal = choose.showOpenDialog(MyFileChooser)
					
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						val file = choose.getSelectedFile();
						println("ok ouvrir" + file.toString() )
					}
	}
	case "Save" => printf("Sauver Fic")
	case "Quit" => printf("Quitter")
	case "E1" => base.Example1
	case "E2" => base.Example2
	case "E3" => base.Example3
    case _ => printf("Choix Inconnu")
   }
}