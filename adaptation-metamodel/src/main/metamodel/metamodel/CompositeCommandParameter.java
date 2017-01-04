/**
 */
package metamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Composite Command Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metamodel.CompositeCommandParameter#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see metamodel.MetamodelPackage#getCompositeCommandParameter()
 * @model
 * @generated
 */
public interface CompositeCommandParameter extends ICommandParameter {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference list.
	 * The list contents are of type {@link metamodel.ICommandParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' containment reference list.
	 * @see metamodel.MetamodelPackage#getCompositeCommandParameter_Value()
	 * @model containment="true"
	 * @generated
	 */
	EList<ICommandParameter> getValue();

} // CompositeCommandParameter
