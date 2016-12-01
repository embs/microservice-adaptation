package com.github.adalrsjr1.automaton.visualizer

import java.awt.Color

import javax.swing.JFrame

import com.github.adalrsjr1.automaton.AutomatonEngine
import com.github.adalrsjr1.automaton.events.AutomatonEvent
import com.github.adalrsjr1.automaton.events.AutomatonListener

import edu.uci.ics.jung.algorithms.layout.FRLayout
import edu.uci.ics.jung.graph.Graph
import edu.uci.ics.jung.visualization.VisualizationViewer
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse
import edu.uci.ics.jung.visualization.control.ModalGraphMouse
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position

class AutomatonVisuzalizer implements AutomatonListener {
	//TODO: implements
	//https://github.com/jrtom/jung/tree/master/jung-samples
	@Override
	public void notify(AutomatonEvent event) {
	}


}
