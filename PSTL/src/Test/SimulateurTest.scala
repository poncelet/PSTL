package Test

import ZAM.Simulator
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertTrue

class SimulateurTest {
  private var Simulateur : Simulator = null
	
	protected def setSimulator(s : Simulator) = Simulateur = s
	protected def getSimulator = Simulateur
	
	@Before
	def beforeTests = {
    Simulateur = new Simulator
  }
	
	@After
	def afterTests = Simulateur = null

	@Test
	def testAvancer {
	  //Condition initial
		//AUCUNE
		
		try{
		//operation
		Simulateur.Avancer(3, 2);
			
		}catch {
		case e : Exception =>
		  //"message, true si test ok : false si test echoue"
			assertTrue("", false);
			return;
		}
		assertTrue("", true);
	}
	
	@Test
	def testRevenir {
	 //Condition initial
		//AUCUNE
		
		try{
		//operation
		Simulateur.Revenir(3, 2);
			
		}catch {
		case e : Exception =>
		  //"message, true si test ok : false si test echoue"
			assertTrue("", false);
			return;
		}
		assertTrue("", true);
	}
}