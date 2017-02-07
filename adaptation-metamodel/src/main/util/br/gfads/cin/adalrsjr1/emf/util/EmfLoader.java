package br.gfads.cin.adalrsjr1.emf.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
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
	
	public static Resource fromString(String xmiString) {
		ResourceSet rs = getXmiResourceSet();
		Resource resource = rs.createResource(URI.createURI("*.xmi"));
		try {
			resource.load(new URIConverter.ReadableInputStream(xmiString, "UTF-8"), null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return resource;
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
	
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(new File("model-instance/AdaptationScript.xmi"), "UTF-8");
		StringBuffer sb = new StringBuffer();
		while(sc.hasNext()) {
			sb.append(sc.nextLine());
		}
		
//		ResourceSet rs = getXmiResourceSet();
//		Resource resource = rs.createResource(URI.createURI("*.xmi"));
//		resource.load(new URIConverter.ReadableInputStream(sb.toString(), "UTF-8"), null);
		
		Resource resource = fromString(sb.toString());
		System.err.println(resource.getContents().get(0));
		resource = fromXmi("model-instance/AdaptationScript.xmi");
		
		System.err.println(resource.getContents().get(0));
	}
}
