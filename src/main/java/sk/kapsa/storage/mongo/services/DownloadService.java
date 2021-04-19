package sk.kapsa.storage.mongo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.kapsa.storage.mongo.DownloadRepository;
import sk.kapsa.storage.mongo.IDAssigner;
import sk.kapsa.storage.mongo.entities.Download;
import sk.kapsa.storage.mongo.entities.State;
import sk.kapsa.storage.mongo.entities.Wrapper;

@Service
public class DownloadService {

	@Autowired
	private DownloadRepository downloadRepository;;

	public List<Download> getDownloadsByState(State state) {

		List<Download> downloads = downloadRepository.findByActualState(state);
		return downloads;
	}

	//TODO otestovat saveWithState
	/**
	 * If you have newly created Download object please use <b>save()</b> method instead.
 	 * @param download that is about to be saved with given state
	 * @param newState is new actualState that is going to assigned to given download 
	 * @return updated Download
	 */
	public Download saveWithState(Download download, State newState) {
		LocalDateTime now = LocalDateTime.now();
		download.setActualState(newState);
		download.addStateTimestamp(newState, now);
		if (newState.equals(State.FINISHED) || newState.equals(State.CANCELLED)) {
			download.setEnd(now);
		}
		return save(download);
	}
	
	//TODO otestovat save
	/**
	 * 
	 * @param download object that is going to be saved
	 * @return given download with id assigned to it
	 */
	public Download save(Download download) {
		if (download.getId() == null) {
			long newDownloadId = IDAssigner.assignNewDownloadId();
			download.setId(newDownloadId);
		}
		return downloadRepository.save(download);
	}

	// TODO otestovat getLastByWrapperId
	/**
	 * 
	 * @param wrapperId is id of wrapper which is equal to wrapperId of the returned <b>download</b>
	 * @return download which wrapper id is equal to given <b>wrapperId</b> and the <b>start</b> is highest of possible downloads
	 */
	public Download getLastByWrapperId(String wrapperId) {
		List<Download> downloads = downloadRepository.findByWrapperId(wrapperId);
		Download actualLast = downloads.get(0);
		for (Download download : downloads) {
			if (download.getStart().compareTo(actualLast.getStart()) > 0) {
				actualLast = download;
			}
		}
		return actualLast;
	}
	
	//TODO otestovat
	/**
	 * 
	 * @param wrapperId is id of wrapper which is equal to wrapperId of the returned downloads
	 * @return all downloads for given <b>wrapperId</b>
	 */
	public List<Download> getByWrapperId(String wrapperId){
		List<Download> downloads = downloadRepository.findByWrapperId(wrapperId);
		return downloads;
	}
	
	public List<Download> getAll(){
		List<Download> downloads = downloadRepository.findAll();
		return downloads;
	}
	
	public List<Download> getBySite(String site){
		List<Download> downloads = downloadRepository.findBySite(site);
		return downloads;
	}
	
	public Optional<Download> getById(long id) {
		Optional<Download> download = downloadRepository.findById(id);
		return download;
	}
	
	public Optional<Wrapper> getWrapperByDownloadId(long id) {
		Optional<Download> download = getById(id);
		if(download.isEmpty()) {
			return Optional.empty();
		}else {
			Optional<Wrapper> wrapper = Optional.of(download.get().getWrapper());
			return wrapper;
		}
	}
	
 
}
