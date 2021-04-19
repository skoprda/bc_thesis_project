package sk.kapsa.storage.mongo.entities;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Download")
public class Download {

	@Id
	private Long id;
	private LocalDateTime start;
	private LocalDateTime end;
	private Wrapper wrapper;
	private State actualState;
	private Map<State, LocalDateTime> stateTimestamps = new HashMap<>();
	
	/**
	 * Constructor which sets <b>start</b> to LocalDateTime.now() and <b>actualState</b> to State.CRAWLING
	 * @param wrapper that is used by Exago for download
	 */
	public Download(Wrapper wrapper) {
		this.wrapper = wrapper;
		this.start = LocalDateTime.now();
		actualState = State.CRAWLING;
		this.stateTimestamps.put(actualState, start);
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	void setStart(LocalDateTime start) {
		this.start = start;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	void setWrapper(Wrapper wrapper) {
		this.wrapper = wrapper;
	}
	public void setActualState(State actualState) {
		this.actualState = actualState;
	}
	public Long getId() {
		return id;
	}
	public LocalDateTime getStart() {
		return start;
	}
	public LocalDateTime getEnd() {
		return end;
	}
	public Wrapper getWrapper() {
		return wrapper;
	}
	public State getActualState() {
		return actualState;
	}
	public Map<State, LocalDateTime> getStateTimestamps() {
		Map<State, LocalDateTime> clone = new HashMap<>(stateTimestamps);
		return clone;
	}
	
	public void addStateTimestamp(State state) {
		addStateTimestamp(state, LocalDateTime.now());
	}	
	
	public void addStateTimestamp(State state, LocalDateTime time) {
		stateTimestamps.put(state, time);
	}

	@Override
	public String toString() {
		return "Download [id=" + id + ", start=" + start + ", end=" + end + ", wrapper=" + wrapper + ", actualState="
				+ actualState + ", stateTimestamps=" + stateTimestamps + "]";
	}
	
	
}
