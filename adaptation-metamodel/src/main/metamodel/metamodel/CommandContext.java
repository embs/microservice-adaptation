/**
 */
package metamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Command Context</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metamodel.CommandContext#getContextAttributes <em>Context Attributes</em>}</li>
 * </ul>
 *
 * @see metamodel.MetamodelPackage#getCommandContext()
 * @model
 * @generated
 */
public interface CommandContext extends ICommand {
	/**
	 * Returns the value of the '<em><b>Context Attributes</b></em>' containment reference list.
	 * The list contents are of type {@link metamodel.ContextAttribute}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Context Attributes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Context Attributes</em>' containment reference list.
	 * @see metamodel.MetamodelPackage#getCommandContext_ContextAttributes()
	 * @model containment="true"
	 * @generated
	 */
	EList<ContextAttribute> getContextAttributes();

} // CommandContext
