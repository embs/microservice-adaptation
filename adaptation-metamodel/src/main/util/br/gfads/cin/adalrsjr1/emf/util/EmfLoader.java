package br.gfads.cin.adalrsjr1.emf.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import metamodel.AdaptationScript;
import metamodel.MetamodelPackage;
import metamodel.impl.MetamodelFactoryImpl;

public class EmfLoader {

	private static ResourceSet getXmiResourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();
		
		Resource.Factory.Registry factoryRegistry = resourceSet.getResourceFactoryRegistry();
		resourceSet.getPackageRegistry().put(MetamodelPackage.eNS_URI, MetamodelPackage.eINSTANCE);
		
		factoryRegistry.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		factoryRegistry.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		
		MetamodelFactoryImpl.init();
		
		return resourceSet;
	}
	
	private static Resource loadXmiUrl(String path) {
		return getXmiResourceSet().getResource(URI.createFileURI(path), true);
	}
	
	public static Resource fromXmi(String path) {
		return loadXmiUrl(path);
	}
	
	public static AdaptationScript getRootFromXmi(String path) {
		Resource resource = fromXmi(path);
		return (AdaptationScript) resource.getContents().get(0);
	}
	
	public static Resource fromCdo(String path) {
		return null;
	}
	
}
