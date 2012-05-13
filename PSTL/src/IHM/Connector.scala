package IHM
import scala.collection.mutable.ArrayBuffer

class Connector {
	val Controler = new control.Controler
	val threadFen = new ArrayBuffer[WMainEvents]
	
	val threadMin = new ArrayBuffer[Boolean]
	val threadName = new ArrayBuffer[String]
	
	threadName += "main"
	threadMin += true
	
	threadFen += new WMainEvents(this, 0)
	
	def getThreadName(it : Int) = threadName(it)
	  
	def getnbthread = threadName.size
	
	def getPath = Controler.getPath
	
	def getOcmlCode = Controler.getOcmlCode
	
	def maxThread(name : String) = ()
	
	def getThread(it : Int) = Controler.getThread(it)
	
	def getEnv = Controler.getEnv
}