/**
 */
package metamodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ICommand</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metamodel.ICommand#getCommandParameter <em>Command Parameter</em>}</li>
 *   <li>{@link metamodel.ICommand#getImplementation <em>Implementation</em>}</li>
 *   <li>{@link metamodel.ICommand#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see metamodel.MetamodelPackage#getICommand()
 * @model abstract="true"
 * @generated
 */
public interface ICommand {
	/**
	 * Returns the value of the '<em><b>Command Parameter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Command Parameter</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Command Parameter</em>' containment reference.
	 * @see #setCommandParameter(ICommandParameter)
	 * @see metamodel.MetamodelPackage#getICommand_CommandParameter()
	 * @model containment="true"
	 * @generated
	 */
	ICommandParameter getCommandParameter();

	/**
	 * Sets the value of the '{@link metamodel.ICommand#getCommandParameter <em>Command Parameter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Command Parameter</em>' containment reference.
	 * @see #getCommandParameter()
	 * @generated
	 */
	void setCommandParameter(ICommandParameter value);

	/**
	 * Returns the value of the '<em><b>Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implementation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Implementation</em>' attribute.
	 * @see #setImplementation(String)
	 * @see metamodel.MetamodelPackage#getICommand_Implementation()
	 * @model required="true"
	 * @generated
	 */
	String getImplementation();

	/**
	 * Sets the value of the '{@link metamodel.ICommand#getImplementation <em>Implementation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Implementation</em>' attribute.
	 * @see #getImplementation()
	 * @generated
	 */
	void setImplementation(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see metamodel.MetamodelPackage#getICommand_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link metamodel.ICommand#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // ICommand
