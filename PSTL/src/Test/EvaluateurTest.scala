package Test

import ZAM.Simulator
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertTrue

class EvaluateurTest {
	
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
	def testexecAcc {
	  //Condition initial
		Simulateur.AST.add(new ZAM.Const(4))
		Simulateur.AST.add(new ZAM.Push())
		Simulateur.AST.add(new ZAM.Const(0))
		
		Simulateur.AST.add(new ZAM.Acc(0))
		Simulateur.Preparer
		Simulateur.Env.pushthread(new ZAM.ThreadState)
		
		try{
		//operation
		Simulateur.Avancer(0, 4);
			
		}catch {
		case e : Exception =>
		  //"message, true si test ok : false si test echoue"
			assertTrue("Exception levée test acc : " + e.getMessage(), false);
			return;
		}
		assertTrue("Testacc echoue", Simulateur.Env.getthread(0).getaccu.asInstanceOf[ZAM.Zamint].getval == 4);
	}
	
	@Test
	def testexecPush {
	 //Condition initial
		Simulateur.AST.add(new ZAM.Const(4))
		
		Simulateur.AST.add(new ZAM.Push())
		Simulateur.Preparer
		Simulateur.Env.pushthread(new ZAM.ThreadState)
		
		try{
		//operation
		Simulateur.Avancer(0, 2);
			
		}catch {
		case e : Exception =>
		  //"message, true si test ok : false si test echoue"
			assertTrue("Exception levée test push : " + e.getMessage(), false);
			return;
		}
		assertTrue("Testpush echoue", Simulateur.Env.getthread(0).stack.getVal.asInstanceOf[ZAM.Zamint].getval == 4);

	}
}