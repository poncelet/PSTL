package ZAM
import scala.collection.mutable.ArrayBuffer

class ThreadStack {
	val stk = new ArrayBuffer[Value]
	private var sp = 0
	
	def getSp = sp
	
	def changeSp(newsp : Int) = sp = newsp
	
	def push(v : Value) =
		if(sp == stk.size) {stk += v; sp = sp + 1}
		else {stk(sp) = v; sp = sp + 1}
	
	def getVal = stk(sp-1)
	
	def get(i : Int) = stk((sp-1) - i)
	
	def update(i: Int, v : Value) = stk.update((sp-1) - i, v)
	
	def pop = {stk.remove(sp-1); sp = sp - 1}
	
	def clear = {stk.clear; sp = 0}
	
	def size = stk.size
	
	override def toString() = {var str="";stk.foreach(ent=> (str += ent + ", "));str}
}