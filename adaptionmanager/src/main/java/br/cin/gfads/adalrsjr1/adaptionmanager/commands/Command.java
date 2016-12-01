package br.cin.gfads.adalrsjr1.adaptionmanager.commands;

import com.spotify.docker.client.exceptions.DockerException;

import metamodel.ICommand;

public interface Command {
	Object execute(ICommand metaCommand) throws Exception;
}
