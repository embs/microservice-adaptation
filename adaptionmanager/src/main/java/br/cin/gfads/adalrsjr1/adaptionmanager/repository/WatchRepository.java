package br.cin.gfads.adalrsjr1.adaptionmanager.repository;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

public class WatchRepository implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(WatchRepository.class);

	private WatchService watcher;
	private Path path;
	private boolean stoped = false;

	private ActionsRepository repository;
	
	public WatchRepository(ActionsRepository repository) {
		this.repository = repository;
		
		watcher = newWatchService(this.repository.getPath());
		path = FileSystems.getDefault().getPath(this.repository.getPath());	

		try {
			path.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private WatchService newWatchService(String path) {
		WatchService watcher = null;
		try {
			watcher = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return watcher;
	}

	public void stop() {
		stoped = true;
	}

	@Override
	public void run() {
		while(!stoped && !Thread.currentThread().isInterrupted()) {
			// wait for key to be signaled
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			for (WatchEvent<?> event: key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();

				// This key is registered only
				// for ENTRY_CREATE events,
				// but an OVERFLOW event can
				// occur regardless if events
				// are lost or discarded.
				if (kind == OVERFLOW) {
					continue;
				}

				// The filename is the
				// context of the event.
				WatchEvent<Path> ev = (WatchEvent<Path>)event;
				Path filename = ev.context();
				
				Path child = path.resolve(filename);
				String filetype = Files.getFileExtension(child.toAbsolutePath().toString());
				if(!filetype.equals("xmi")) {
					log.info("scripts folder was updated with the non-script file: {}", child.getFileName());;
					continue;
				}
				if(kind == ENTRY_CREATE) {
					log.info("loading script {}", filename.getFileName());
					repository.loadScript("scripts/" + filename.getFileName());
				}
				else if(kind == ENTRY_DELETE) {
					log.info("deleting script {}", filename.getFileName());
					repository.removeScript(filename.toAbsolutePath().toString());
				}
				else if(kind == ENTRY_MODIFY && ev.count() == 1) {
					continue;
				}
			}

			// Reset the key -- this step is critical if you want to
			// receive further watch events.  If the key is no longer valid,
			// the directory is inaccessible so exit the loop.
			boolean valid = key.reset();
			if (!valid) {
				break;
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		File f = new File("scripts");
		ActionsRepository repository = new ActionsRepository("scripts");
		WatchRepository rep = new WatchRepository(repository);
		Thread t = new Thread(rep);
		t.start();
	}

}
