package ZAM

/**
 * Enumération des types de Bloc Value existant
 */
object BlockT extends Enumeration {
	type blockT = Value
	val normal_t, foward_t, infix_t, object_t, closure_t, lazy_t,
	abstract_t, string_t, double_t, doublearray_t, custom_t
	= Value
}