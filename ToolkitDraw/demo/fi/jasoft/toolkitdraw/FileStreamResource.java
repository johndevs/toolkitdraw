package fi.jasoft.toolkitdraw;

import com.vaadin.Application;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.StreamResource;

/**
 * Class for downloading files without opening 
 * them in the browser
 * @author John Ahlroos /ITMill Oy Ltd
 */
public class FileStreamResource extends StreamResource {

	private static final long serialVersionUID = 1L;
	private StreamSource source;
	private String filename;
	
	public FileStreamResource(StreamSource streamSource, String filename,
			Application application) {
		super(streamSource, filename, application);
		source = streamSource;
		this.filename = filename;
	}
	
	@Override
	public DownloadStream getStream() {		
		 final DownloadStream ds = new DownloadStream(source.getStream(), getMIMEType(), getFilename());
		 ds.setCacheTime(getCacheTime());
		 ds.setParameter("Content-Disposition", "attachment; filename="+filename);
		 return ds;	         
	}
}
