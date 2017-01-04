/**
 */
package metamodel.impl;

import metamodel.ContextAttribute;
import metamodel.ContextType;
import metamodel.MetamodelPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Context Attribute</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metamodel.impl.ContextAttributeImpl#getKey <em>Key</em>}</li>
 *   <li>{@link metamodel.impl.ContextAttributeImpl#getType <em>Type</em>}</li>
 *   <li>{@link metamodel.impl.ContextAttributeImpl#isTemporary <em>Temporary</em>}</li>
 *   <li>{@link metamodel.impl.ContextAttributeImpl#getContextImpl <em>Context Impl</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ContextAttributeImpl extends MinimalEObjectImpl.Container implements ContextAttribute {
	/**
	 * The default value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected static final String KEY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected String key = KEY_EDEFAULT;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final ContextType TYPE_EDEFAULT = ContextType.INTEGER;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected ContextType type = TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #isTemporary() <em>Temporary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTemporary()
	 * @generated
	 * @ordered
	 */
	protected static final boolean TEMPORARY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isTemporary() <em>Temporary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTemporary()
	 * @generated
	 * @ordered
	 */
	protected boolean temporary = TEMPORARY_EDEFAULT;

	/**
	 * The default value of the '{@link #getContextImpl() <em>Context Impl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContextImpl()
	 * @generated
	 * @ordered
	 */
	protected static final Object CONTEXT_IMPL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getContextImpl() <em>Context Impl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContextImpl()
	 * @generated
	 * @ordered
	 */
	protected Object contextImpl = CONTEXT_IMPL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContextAttributeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.CONTEXT_ATTRIBUTE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getKey() {
		return key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKey(String newKey) {
		String oldKey = key;
		key = newKey;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.CONTEXT_ATTRIBUTE__KEY, oldKey, key));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContextType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(ContextType newType) {
		ContextType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.CONTEXT_ATTRIBUTE__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isTemporary() {
		return temporary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTemporary(boolean newTemporary) {
		boolean oldTemporary = temporary;
		temporary = newTemporary;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.CONTEXT_ATTRIBUTE__TEMPORARY, oldTemporary, temporary));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getContextImpl() {
		return contextImpl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContextImpl(Object newContextImpl) {
		Object oldContextImpl = contextImpl;
		contextImpl = newContextImpl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.CONTEXT_ATTRIBUTE__CONTEXT_IMPL, oldContextImpl, contextImpl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetamodelPackage.CONTEXT_ATTRIBUTE__KEY:
				return getKey();
			case MetamodelPackage.CONTEXT_ATTRIBUTE__TYPE:
				return getType();
			case MetamodelPackage.CONTEXT_ATTRIBUTE__TEMPORARY:
				return isTemporary();
			case MetamodelPackage.CONTEXT_ATTRIBUTE__CONTEXT_IMPL:
				return getContextImpl();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MetamodelPackage.CONTEXT_ATTRIBUTE__KEY:
				setKey((String)newValue);
				return;
			case MetamodelPackage.CONTEXT_ATTRIBUTE__TYPE:
				setType((ContextType)newValue);
				return;
			case MetamodelPackage.CONTEXT_ATTRIBUTE__TEMPORARY:
				setTemporary((Boolean)newValue);
				return;
			case MetamodelPackage.CONTEXT_ATTRIBUTE__CONTEXT_IMPL:
				setContextImpl(newValue);
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
			case MetamodelPackage.CONTEXT_ATTRIBUTE__KEY:
				setKey(KEY_EDEFAULT);
				return;
			case MetamodelPackage.CONTEXT_ATTRIBUTE__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case MetamodelPackage.CONTEXT_ATTRIBUTE__TEMPORARY:
				setTemporary(TEMPORARY_EDEFAULT);
				return;
			case MetamodelPackage.CONTEXT_ATTRIBUTE__CONTEXT_IMPL:
				setContextImpl(CONTEXT_IMPL_EDEFAULT);
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
			case MetamodelPackage.CONTEXT_ATTRIBUTE__KEY:
				return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
			case MetamodelPackage.CONTEXT_ATTRIBUTE__TYPE:
				return type != TYPE_EDEFAULT;
			case MetamodelPackage.CONTEXT_ATTRIBUTE__TEMPORARY:
				return temporary != TEMPORARY_EDEFAULT;
			case MetamodelPackage.CONTEXT_ATTRIBUTE__CONTEXT_IMPL:
				return CONTEXT_IMPL_EDEFAULT == null ? contextImpl != null : !CONTEXT_IMPL_EDEFAULT.equals(contextImpl);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (key: ");
		result.append(key);
		result.append(", type: ");
		result.append(type);
		result.append(", temporary: ");
		result.append(temporary);
		result.append(", contextImpl: ");
		result.append(contextImpl);
		result.append(')');
		return result.toString();
	}

} //ContextAttributeImpl
