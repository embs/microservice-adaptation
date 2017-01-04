/**
 */
package metamodel.impl;

import java.util.Collection;

import metamodel.CommandContext;
import metamodel.ContextAttribute;
import metamodel.MetamodelPackage;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.util.EObjectEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Command Context</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metamodel.impl.CommandContextImpl#getContextAttributes <em>Context Attributes</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CommandContextImpl extends ICommandImpl implements CommandContext {
	/**
	 * The cached value of the '{@link #getContextAttributes() <em>Context Attributes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContextAttributes()
	 * @generated
	 * @ordered
	 */
	protected EList<ContextAttribute> contextAttributes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CommandContextImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.COMMAND_CONTEXT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ContextAttribute> getContextAttributes() {
		if (contextAttributes == null) {
			contextAttributes = new EObjectEList<ContextAttribute>(ContextAttribute.class, this, MetamodelPackage.COMMAND_CONTEXT__CONTEXT_ATTRIBUTES);
		}
		return contextAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetamodelPackage.COMMAND_CONTEXT__CONTEXT_ATTRIBUTES:
				return getContextAttributes();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MetamodelPackage.COMMAND_CONTEXT__CONTEXT_ATTRIBUTES:
				getContextAttributes().clear();
				getContextAttributes().addAll((Collection<? extends ContextAttribute>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case MetamodelPackage.COMMAND_CONTEXT__CONTEXT_ATTRIBUTES:
				getContextAttributes().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MetamodelPackage.COMMAND_CONTEXT__CONTEXT_ATTRIBUTES:
				return contextAttributes != null && !contextAttributes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //CommandContextImpl
