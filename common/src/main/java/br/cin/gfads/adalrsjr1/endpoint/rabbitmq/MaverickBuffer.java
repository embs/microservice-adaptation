package br.cin.gfads.adalrsjr1.endpoint.rabbitmq;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.naming.OperationNotSupportedException;

public class MaverickBuffer<E> implements BlockingQueue<E> {
	private BlockingQueue<PriorityBufferEnvelope<E>> queue;
	
	static class PriorityBufferEnvelope<E> implements Comparable<PriorityBufferEnvelope> {
		public final E body;
		public final Integer priority;
		
		private PriorityBufferEnvelope(E body, Integer priority) {
			this.body = body;
			this.priority = priority;
		}
		
		@Override
		public int compareTo(PriorityBufferEnvelope o) {
			return o.priority.compareTo(this.priority);
		}

	}
	
	public MaverickBuffer() {
		queue = new  PriorityBlockingQueue<>();
	}
	
	public MaverickBuffer(int capacity) {
		queue = new PriorityBlockingQueue<>(capacity);
	}
	
	@Override
	public E remove() {
		return queue.remove().body;
	}

	@Override
	public E poll() {
		return queue.poll().body;
	}

	@Override
	public E element() {
		return queue.element().body;
	}

	@Override
	public E peek() {
		return queue.peek().body;
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		throw new RuntimeException(new OperationNotSupportedException());
	}

	@Override
	public Object[] toArray() {
		throw new RuntimeException(new OperationNotSupportedException());
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new RuntimeException(new OperationNotSupportedException());
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new RuntimeException(new OperationNotSupportedException());
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new RuntimeException(new OperationNotSupportedException());
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new RuntimeException(new OperationNotSupportedException());
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new RuntimeException(new OperationNotSupportedException());
	}

	@Override
	public void clear() {
		queue.clear();
	}

	@Override
	public boolean add(E e) {
		return add(e, 0);
	}
	
	public boolean add(E e, int priority) {
		return queue.add(new PriorityBufferEnvelope<E>(e, priority));
	}

	@Override
	public boolean offer(E e) {
		return offer(e, 0);
	}
	
	public boolean offer(E e, int priority) {
		return queue.offer(new PriorityBufferEnvelope<E>(e, priority));
	}

	@Override
	public void put(E e) throws InterruptedException {
		put(e,0);
	}
	
	public void put(E e, int priority) throws InterruptedException {
		queue.put(new PriorityBufferEnvelope<E>(e, priority));
	}

	@Override
	public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
		return offer(e, 0, timeout, unit);
	}
	
	public boolean offer(E e, int priority, long timeout, TimeUnit unit) throws InterruptedException {
		return queue.offer(new PriorityBufferEnvelope<E>(e, priority), timeout, unit);
	}

	@Override
	public E take() throws InterruptedException {
		return queue.take().body;
	}

	@Override
	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		return queue.poll(timeout, unit).body;
	}

	@Override
	public int remainingCapacity() {
		return queue.remainingCapacity();
	}

	@Override
	public boolean remove(Object o) {
		throw new RuntimeException(new OperationNotSupportedException());
	}

	@Override
	public boolean contains(Object o) {
		throw new RuntimeException(new OperationNotSupportedException());
	}

	@Override
	public int drainTo(Collection<? super E> c) {
		throw new RuntimeException(new OperationNotSupportedException());
	}

	@Override
	public int drainTo(Collection<? super E> c, int maxElements) {
		throw new RuntimeException(new OperationNotSupportedException());
	}

}
