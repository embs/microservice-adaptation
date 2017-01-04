/**
 */
package metamodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ICommand Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metamodel.ICommandParameter#getKey <em>Key</em>}</li>
 * </ul>
 *
 * @see metamodel.MetamodelPackage#getICommandParameter()
 * @model abstract="true"
 * @generated
 */
public interface ICommandParameter {
	/**
	 * Returns the value of the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key</em>' attribute.
	 * @see #setKey(String)
	 * @see metamodel.MetamodelPackage#getICommandParameter_Key()
	 * @model id="true" required="true"
	 * @generated
	 */
	String getKey();

	/**
	 * Sets the value of the '{@link metamodel.ICommandParameter#getKey <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key</em>' attribute.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(String value);

} // ICommandParameter
