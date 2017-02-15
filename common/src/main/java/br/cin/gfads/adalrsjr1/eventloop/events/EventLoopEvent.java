package br.cin.gfads.adalrsjr1.eventloop.events;

import br.cin.gfads.adalrsjr1.eventloop.EventLoopWorker;

public abstract class EventLoopEvent {
	EventLoopWorker source;

	private EventLoopEvent(EventLoopWorker source) {
		this.source = source;
	}

	public EventLoopWorker getSource() {
		return source;
	}

	public static class StopEvent extends EventLoopEvent {
		public StopEvent(EventLoopWorker source) {
			super(source);
		}
	}
	
	public static class ExceptionEvent extends EventLoopEvent {
		public final Throwable exception; 
		public ExceptionEvent(EventLoopWorker source, Throwable exception) {
			super(source);
			this.exception = exception;
		}
		
		@Override
		public String toString() {
			return "EventLoopEvent: " + exception.getMessage();
		}
	}

	public static class MessageEvent extends EventLoopEvent {
		public final String message;

		public MessageEvent(EventLoopWorker source, String message) {
			super(source);
			this.message = message;
		}

		@Override
		public String toString() {
			return "EventLoopEvent: " + message;
		}
	}
}
