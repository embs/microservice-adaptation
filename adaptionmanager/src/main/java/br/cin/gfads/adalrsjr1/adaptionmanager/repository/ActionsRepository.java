package br.cin.gfads.adalrsjr1.adaptionmanager.repository;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.google.common.io.Files;

import br.cin.gfads.adalrsjr1.adaptionmanager.AdaptationManager;
import br.cin.gfads.adalrsjr1.adaptionmanager.AdaptationManagerConfiguration;
import br.cin.gfads.adalrsjr1.adaptionmanager.commands.Script;
import br.gfads.cin.adalrsjr1.emf.util.EmfLoader;
import metamodel.AdaptationScript;

public class ActionsRepository implements AutoCloseable {

	private static final Logger log = LoggerFactory.getLogger(ActionsRepository.class);
	
	private Map<String, Script> actions;
	private File file;
	
	private WatchRepository watcher;
	private Thread thread;
	
	public ActionsRepository() {
		this(AdaptationManager.CONFIG.scriptsRepository);
	}
	
	public ActionsRepository(String path) {
		this.file = new File(path);
		actions = new HashMap<>();
		watcher = new WatchRepository(this);
		thread = new Thread(watcher, "watcher-actions-repository");
		thread.start();
	}
	
	public void loadScript(String scriptPathXmi) {
		Stopwatch watch = Stopwatch.createStarted();
		String filetype = Files.getFileExtension(scriptPathXmi);
		if(filetype.equals("xmi")) {
			loadNewScript(scriptPathXmi);
		}
		log.info("script loaded in {}", watch.stop());
	}

	public void load() {
		Stopwatch watch = Stopwatch.createStarted();
		File files[] = file.listFiles();
		for(File file : files) {
			String filetype = Files.getFileExtension(file.getAbsolutePath());
			if(filetype.equals("xmi")) {
				loadNewScript(file.getAbsolutePath());
			}
		}
		log.info("actions repository loaded in {}", watch.stop());
	}
	
	public String getPath() {
		return file.getAbsolutePath();
	}
	
	public void loadNewScript(String path) {
		AdaptationScript metaScript = EmfLoader.getRootFromXmi(path);
		actions.put(metaScript.getName(), new Script(metaScript));
	}
	
	public void removeScript(String path) {
		log.warn("method to remove a script [{}] not implemented yet", path);
	}
	
	public Script getAdaptationScript(String name) {
		return actions.get(name);
	}
	
	public void stop() {
		watcher.stop();
	}
	
	@Override
	public void close() throws Exception {
		stop();
	}
	
	public static void main(String args[]) {
		try(ActionsRepository repository = new ActionsRepository()) {
			repository.load();
			System.out.println(repository.getAdaptationScript("testScript"));
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
		
	}

	
	
}
