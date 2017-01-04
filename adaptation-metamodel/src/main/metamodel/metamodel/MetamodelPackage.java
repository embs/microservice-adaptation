/**
 */
package metamodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see metamodel.MetamodelFactory
 * @model kind="package"
 * @generated
 */
public interface MetamodelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "metamodel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://gfads.cin.ufpe.br/maveric";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "metamodel";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MetamodelPackage eINSTANCE = metamodel.impl.MetamodelPackageImpl.init();

	/**
	 * The meta object id for the '{@link metamodel.impl.ICommandImpl <em>ICommand</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metamodel.impl.ICommandImpl
	 * @see metamodel.impl.MetamodelPackageImpl#getICommand()
	 * @generated
	 */
	int ICOMMAND = 0;

	/**
	 * The feature id for the '<em><b>Command Parameter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ICOMMAND__COMMAND_PARAMETER = 0;

	/**
	 * The feature id for the '<em><b>Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ICOMMAND__IMPLEMENTATION = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ICOMMAND__NAME = 2;

	/**
	 * The number of structural features of the '<em>ICommand</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ICOMMAND_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>ICommand</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ICOMMAND_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metamodel.impl.CommandContextImpl <em>Command Context</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metamodel.impl.CommandContextImpl
	 * @see metamodel.impl.MetamodelPackageImpl#getCommandContext()
	 * @generated
	 */
	int COMMAND_CONTEXT = 1;

	/**
	 * The feature id for the '<em><b>Command Parameter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND_CONTEXT__COMMAND_PARAMETER = ICOMMAND__COMMAND_PARAMETER;

	/**
	 * The feature id for the '<em><b>Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND_CONTEXT__IMPLEMENTATION = ICOMMAND__IMPLEMENTATION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND_CONTEXT__NAME = ICOMMAND__NAME;

	/**
	 * The feature id for the '<em><b>Context Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND_CONTEXT__CONTEXT_ATTRIBUTES = ICOMMAND_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Command Context</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND_CONTEXT_FEATURE_COUNT = ICOMMAND_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Command Context</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND_CONTEXT_OPERATION_COUNT = ICOMMAND_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link metamodel.impl.CommandImpl <em>Command</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metamodel.impl.CommandImpl
	 * @see metamodel.impl.MetamodelPackageImpl#getCommand()
	 * @generated
	 */
	int COMMAND = 2;

	/**
	 * The feature id for the '<em><b>Command Parameter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND__COMMAND_PARAMETER = ICOMMAND__COMMAND_PARAMETER;

	/**
	 * The feature id for the '<em><b>Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND__IMPLEMENTATION = ICOMMAND__IMPLEMENTATION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND__NAME = ICOMMAND__NAME;

	/**
	 * The number of structural features of the '<em>Command</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND_FEATURE_COUNT = ICOMMAND_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Command</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND_OPERATION_COUNT = ICOMMAND_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link metamodel.impl.ICommandParameterImpl <em>ICommand Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metamodel.impl.ICommandParameterImpl
	 * @see metamodel.impl.MetamodelPackageImpl#getICommandParameter()
	 * @generated
	 */
	int ICOMMAND_PARAMETER = 7;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ICOMMAND_PARAMETER__KEY = 0;

	/**
	 * The number of structural features of the '<em>ICommand Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ICOMMAND_PARAMETER_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>ICommand Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ICOMMAND_PARAMETER_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metamodel.impl.CommandParameterImpl <em>Command Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metamodel.impl.CommandParameterImpl
	 * @see metamodel.impl.MetamodelPackageImpl#getCommandParameter()
	 * @generated
	 */
	int COMMAND_PARAMETER = 3;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND_PARAMETER__KEY = ICOMMAND_PARAMETER__KEY;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND_PARAMETER__VALUE = ICOMMAND_PARAMETER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Command Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND_PARAMETER_FEATURE_COUNT = ICOMMAND_PARAMETER_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Command Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND_PARAMETER_OPERATION_COUNT = ICOMMAND_PARAMETER_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link metamodel.impl.ContextAttributeImpl <em>Context Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metamodel.impl.ContextAttributeImpl
	 * @see metamodel.impl.MetamodelPackageImpl#getContextAttribute()
	 * @generated
	 */
	int CONTEXT_ATTRIBUTE = 4;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTEXT_ATTRIBUTE__KEY = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTEXT_ATTRIBUTE__TYPE = 1;

	/**
	 * The feature id for the '<em><b>Temporary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTEXT_ATTRIBUTE__TEMPORARY = 2;

	/**
	 * The feature id for the '<em><b>Context Impl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTEXT_ATTRIBUTE__CONTEXT_IMPL = 3;

	/**
	 * The number of structural features of the '<em>Context Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTEXT_ATTRIBUTE_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Context Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTEXT_ATTRIBUTE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metamodel.impl.AdaptationScriptImpl <em>Adaptation Script</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metamodel.impl.AdaptationScriptImpl
	 * @see metamodel.impl.MetamodelPackageImpl#getAdaptationScript()
	 * @generated
	 */
	int ADAPTATION_SCRIPT = 5;

	/**
	 * The feature id for the '<em><b>Commands</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADAPTATION_SCRIPT__COMMANDS = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADAPTATION_SCRIPT__NAME = 1;

	/**
	 * The number of structural features of the '<em>Adaptation Script</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADAPTATION_SCRIPT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Adaptation Script</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADAPTATION_SCRIPT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metamodel.impl.CompositeCommandParameterImpl <em>Composite Command Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metamodel.impl.CompositeCommandParameterImpl
	 * @see metamodel.impl.MetamodelPackageImpl#getCompositeCommandParameter()
	 * @generated
	 */
	int COMPOSITE_COMMAND_PARAMETER = 6;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_COMMAND_PARAMETER__KEY = ICOMMAND_PARAMETER__KEY;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_COMMAND_PARAMETER__VALUE = ICOMMAND_PARAMETER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Composite Command Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_COMMAND_PARAMETER_FEATURE_COUNT = ICOMMAND_PARAMETER_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Composite Command Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_COMMAND_PARAMETER_OPERATION_COUNT = ICOMMAND_PARAMETER_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link metamodel.ContextType <em>Context Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metamodel.ContextType
	 * @see metamodel.impl.MetamodelPackageImpl#getContextType()
	 * @generated
	 */
	int CONTEXT_TYPE = 8;


	/**
	 * Returns the meta object for class '{@link metamodel.ICommand <em>ICommand</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ICommand</em>'.
	 * @see metamodel.ICommand
	 * @generated
	 */
	EClass getICommand();

	/**
	 * Returns the meta object for the containment reference '{@link metamodel.ICommand#getCommandParameter <em>Command Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Command Parameter</em>'.
	 * @see metamodel.ICommand#getCommandParameter()
	 * @see #getICommand()
	 * @generated
	 */
	EReference getICommand_CommandParameter();

	/**
	 * Returns the meta object for the attribute '{@link metamodel.ICommand#getImplementation <em>Implementation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Implementation</em>'.
	 * @see metamodel.ICommand#getImplementation()
	 * @see #getICommand()
	 * @generated
	 */
	EAttribute getICommand_Implementation();

	/**
	 * Returns the meta object for the attribute '{@link metamodel.ICommand#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see metamodel.ICommand#getName()
	 * @see #getICommand()
	 * @generated
	 */
	EAttribute getICommand_Name();

	/**
	 * Returns the meta object for class '{@link metamodel.CommandContext <em>Command Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Command Context</em>'.
	 * @see metamodel.CommandContext
	 * @generated
	 */
	EClass getCommandContext();

	/**
	 * Returns the meta object for the containment reference list '{@link metamodel.CommandContext#getContextAttributes <em>Context Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Context Attributes</em>'.
	 * @see metamodel.CommandContext#getContextAttributes()
	 * @see #getCommandContext()
	 * @generated
	 */
	EReference getCommandContext_ContextAttributes();

	/**
	 * Returns the meta object for class '{@link metamodel.Command <em>Command</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Command</em>'.
	 * @see metamodel.Command
	 * @generated
	 */
	EClass getCommand();

	/**
	 * Returns the meta object for class '{@link metamodel.CommandParameter <em>Command Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Command Parameter</em>'.
	 * @see metamodel.CommandParameter
	 * @generated
	 */
	EClass getCommandParameter();

	/**
	 * Returns the meta object for the attribute '{@link metamodel.CommandParameter#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see metamodel.CommandParameter#getValue()
	 * @see #getCommandParameter()
	 * @generated
	 */
	EAttribute getCommandParameter_Value();

	/**
	 * Returns the meta object for class '{@link metamodel.ContextAttribute <em>Context Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Context Attribute</em>'.
	 * @see metamodel.ContextAttribute
	 * @generated
	 */
	EClass getContextAttribute();

	/**
	 * Returns the meta object for the attribute '{@link metamodel.ContextAttribute#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see metamodel.ContextAttribute#getKey()
	 * @see #getContextAttribute()
	 * @generated
	 */
	EAttribute getContextAttribute_Key();

	/**
	 * Returns the meta object for the attribute '{@link metamodel.ContextAttribute#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see metamodel.ContextAttribute#getType()
	 * @see #getContextAttribute()
	 * @generated
	 */
	EAttribute getContextAttribute_Type();

	/**
	 * Returns the meta object for the attribute '{@link metamodel.ContextAttribute#isTemporary <em>Temporary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Temporary</em>'.
	 * @see metamodel.ContextAttribute#isTemporary()
	 * @see #getContextAttribute()
	 * @generated
	 */
	EAttribute getContextAttribute_Temporary();

	/**
	 * Returns the meta object for the attribute '{@link metamodel.ContextAttribute#getContextImpl <em>Context Impl</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Context Impl</em>'.
	 * @see metamodel.ContextAttribute#getContextImpl()
	 * @see #getContextAttribute()
	 * @generated
	 */
	EAttribute getContextAttribute_ContextImpl();

	/**
	 * Returns the meta object for class '{@link metamodel.AdaptationScript <em>Adaptation Script</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Adaptation Script</em>'.
	 * @see metamodel.AdaptationScript
	 * @generated
	 */
	EClass getAdaptationScript();

	/**
	 * Returns the meta object for the containment reference list '{@link metamodel.AdaptationScript#getCommands <em>Commands</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Commands</em>'.
	 * @see metamodel.AdaptationScript#getCommands()
	 * @see #getAdaptationScript()
	 * @generated
	 */
	EReference getAdaptationScript_Commands();

	/**
	 * Returns the meta object for the attribute '{@link metamodel.AdaptationScript#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see metamodel.AdaptationScript#getName()
	 * @see #getAdaptationScript()
	 * @generated
	 */
	EAttribute getAdaptationScript_Name();

	/**
	 * Returns the meta object for class '{@link metamodel.CompositeCommandParameter <em>Composite Command Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Composite Command Parameter</em>'.
	 * @see metamodel.CompositeCommandParameter
	 * @generated
	 */
	EClass getCompositeCommandParameter();

	/**
	 * Returns the meta object for the containment reference list '{@link metamodel.CompositeCommandParameter#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Value</em>'.
	 * @see metamodel.CompositeCommandParameter#getValue()
	 * @see #getCompositeCommandParameter()
	 * @generated
	 */
	EReference getCompositeCommandParameter_Value();

	/**
	 * Returns the meta object for class '{@link metamodel.ICommandParameter <em>ICommand Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ICommand Parameter</em>'.
	 * @see metamodel.ICommandParameter
	 * @generated
	 */
	EClass getICommandParameter();

	/**
	 * Returns the meta object for the attribute '{@link metamodel.ICommandParameter#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see metamodel.ICommandParameter#getKey()
	 * @see #getICommandParameter()
	 * @generated
	 */
	EAttribute getICommandParameter_Key();

	/**
	 * Returns the meta object for enum '{@link metamodel.ContextType <em>Context Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Context Type</em>'.
	 * @see metamodel.ContextType
	 * @generated
	 */
	EEnum getContextType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MetamodelFactory getMetamodelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link metamodel.impl.ICommandImpl <em>ICommand</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metamodel.impl.ICommandImpl
		 * @see metamodel.impl.MetamodelPackageImpl#getICommand()
		 * @generated
		 */
		EClass ICOMMAND = eINSTANCE.getICommand();

		/**
		 * The meta object literal for the '<em><b>Command Parameter</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ICOMMAND__COMMAND_PARAMETER = eINSTANCE.getICommand_CommandParameter();

		/**
		 * The meta object literal for the '<em><b>Implementation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ICOMMAND__IMPLEMENTATION = eINSTANCE.getICommand_Implementation();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ICOMMAND__NAME = eINSTANCE.getICommand_Name();

		/**
		 * The meta object literal for the '{@link metamodel.impl.CommandContextImpl <em>Command Context</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metamodel.impl.CommandContextImpl
		 * @see metamodel.impl.MetamodelPackageImpl#getCommandContext()
		 * @generated
		 */
		EClass COMMAND_CONTEXT = eINSTANCE.getCommandContext();

		/**
		 * The meta object literal for the '<em><b>Context Attributes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMAND_CONTEXT__CONTEXT_ATTRIBUTES = eINSTANCE.getCommandContext_ContextAttributes();

		/**
		 * The meta object literal for the '{@link metamodel.impl.CommandImpl <em>Command</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metamodel.impl.CommandImpl
		 * @see metamodel.impl.MetamodelPackageImpl#getCommand()
		 * @generated
		 */
		EClass COMMAND = eINSTANCE.getCommand();

		/**
		 * The meta object literal for the '{@link metamodel.impl.CommandParameterImpl <em>Command Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metamodel.impl.CommandParameterImpl
		 * @see metamodel.impl.MetamodelPackageImpl#getCommandParameter()
		 * @generated
		 */
		EClass COMMAND_PARAMETER = eINSTANCE.getCommandParameter();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMAND_PARAMETER__VALUE = eINSTANCE.getCommandParameter_Value();

		/**
		 * The meta object literal for the '{@link metamodel.impl.ContextAttributeImpl <em>Context Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metamodel.impl.ContextAttributeImpl
		 * @see metamodel.impl.MetamodelPackageImpl#getContextAttribute()
		 * @generated
		 */
		EClass CONTEXT_ATTRIBUTE = eINSTANCE.getContextAttribute();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTEXT_ATTRIBUTE__KEY = eINSTANCE.getContextAttribute_Key();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTEXT_ATTRIBUTE__TYPE = eINSTANCE.getContextAttribute_Type();

		/**
		 * The meta object literal for the '<em><b>Temporary</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTEXT_ATTRIBUTE__TEMPORARY = eINSTANCE.getContextAttribute_Temporary();

		/**
		 * The meta object literal for the '<em><b>Context Impl</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTEXT_ATTRIBUTE__CONTEXT_IMPL = eINSTANCE.getContextAttribute_ContextImpl();

		/**
		 * The meta object literal for the '{@link metamodel.impl.AdaptationScriptImpl <em>Adaptation Script</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metamodel.impl.AdaptationScriptImpl
		 * @see metamodel.impl.MetamodelPackageImpl#getAdaptationScript()
		 * @generated
		 */
		EClass ADAPTATION_SCRIPT = eINSTANCE.getAdaptationScript();

		/**
		 * The meta object literal for the '<em><b>Commands</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADAPTATION_SCRIPT__COMMANDS = eINSTANCE.getAdaptationScript_Commands();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ADAPTATION_SCRIPT__NAME = eINSTANCE.getAdaptationScript_Name();

		/**
		 * The meta object literal for the '{@link metamodel.impl.CompositeCommandParameterImpl <em>Composite Command Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metamodel.impl.CompositeCommandParameterImpl
		 * @see metamodel.impl.MetamodelPackageImpl#getCompositeCommandParameter()
		 * @generated
		 */
		EClass COMPOSITE_COMMAND_PARAMETER = eINSTANCE.getCompositeCommandParameter();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPOSITE_COMMAND_PARAMETER__VALUE = eINSTANCE.getCompositeCommandParameter_Value();

		/**
		 * The meta object literal for the '{@link metamodel.impl.ICommandParameterImpl <em>ICommand Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metamodel.impl.ICommandParameterImpl
		 * @see metamodel.impl.MetamodelPackageImpl#getICommandParameter()
		 * @generated
		 */
		EClass ICOMMAND_PARAMETER = eINSTANCE.getICommandParameter();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ICOMMAND_PARAMETER__KEY = eINSTANCE.getICommandParameter_Key();

		/**
		 * The meta object literal for the '{@link metamodel.ContextType <em>Context Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metamodel.ContextType
		 * @see metamodel.impl.MetamodelPackageImpl#getContextType()
		 * @generated
		 */
		EEnum CONTEXT_TYPE = eINSTANCE.getContextType();

	}

} //MetamodelPackage
