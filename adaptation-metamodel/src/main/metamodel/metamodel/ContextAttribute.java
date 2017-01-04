/**
 */
package metamodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Context Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metamodel.ContextAttribute#getKey <em>Key</em>}</li>
 *   <li>{@link metamodel.ContextAttribute#getType <em>Type</em>}</li>
 *   <li>{@link metamodel.ContextAttribute#isTemporary <em>Temporary</em>}</li>
 *   <li>{@link metamodel.ContextAttribute#getContextImpl <em>Context Impl</em>}</li>
 * </ul>
 *
 * @see metamodel.MetamodelPackage#getContextAttribute()
 * @model
 * @generated
 */
public interface ContextAttribute {
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
	 * @see metamodel.MetamodelPackage#getContextAttribute_Key()
	 * @model required="true"
	 * @generated
	 */
	String getKey();

	/**
	 * Sets the value of the '{@link metamodel.ContextAttribute#getKey <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key</em>' attribute.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link metamodel.ContextType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see metamodel.ContextType
	 * @see #setType(ContextType)
	 * @see metamodel.MetamodelPackage#getContextAttribute_Type()
	 * @model
	 * @generated
	 */
	ContextType getType();

	/**
	 * Sets the value of the '{@link metamodel.ContextAttribute#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see metamodel.ContextType
	 * @see #getType()
	 * @generated
	 */
	void setType(ContextType value);

	/**
	 * Returns the value of the '<em><b>Temporary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Temporary</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Temporary</em>' attribute.
	 * @see #setTemporary(boolean)
	 * @see metamodel.MetamodelPackage#getContextAttribute_Temporary()
	 * @model
	 * @generated
	 */
	boolean isTemporary();

	/**
	 * Sets the value of the '{@link metamodel.ContextAttribute#isTemporary <em>Temporary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Temporary</em>' attribute.
	 * @see #isTemporary()
	 * @generated
	 */
	void setTemporary(boolean value);

	/**
	 * Returns the value of the '<em><b>Context Impl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Context Impl</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Context Impl</em>' attribute.
	 * @see #setContextImpl(Object)
	 * @see metamodel.MetamodelPackage#getContextAttribute_ContextImpl()
	 * @model
	 * @generated
	 */
	Object getContextImpl();

	/**
	 * Sets the value of the '{@link metamodel.ContextAttribute#getContextImpl <em>Context Impl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context Impl</em>' attribute.
	 * @see #getContextImpl()
	 * @generated
	 */
	void setContextImpl(Object value);

} // ContextAttribute
