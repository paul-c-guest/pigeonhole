package backend.util;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

public class ClusteredList {

	private List<List<ClusteredFile>> fileClusters;
	private Integer position = null;
	private final static long FIVE_MINUTES = 300;

	public ClusteredList(File[] input) {

		List<ClusteredFile> files = new ArrayList<>();
		Metadata data;
		ExifIFD0Directory exif;
		Date date;

		for (File file : input) {
			try {
				data = JpegMetadataReader.readMetadata(file);
				exif = data.getFirstDirectoryOfType(ExifIFD0Directory.class);
				date = exif.getDate(ExifIFD0Directory.TAG_DATETIME);

//				System.out.println(exif.getTags().toString());

				files.add(new ClusteredFile(file, date.toInstant()));

			} catch (JpegProcessingException e) {
//				e.printStackTrace();
				continue;

			} catch (IOException e) {
//				e.printStackTrace();
				continue;
			}
		}

		// ClusteredFile implements Comparable
		Collections.sort(files);

		int limit = files.size();

		// set boundary flags for appropriate entries
		for (int i = 0; i < limit; i++) {
			// final entry is always a boundary
			if (i == limit - 1) {
				files.get(i).clusterEnd = true;
				continue;
			}

			long current = files.get(i).time.getEpochSecond();
			long next = files.get(i + 1).time.getEpochSecond();

			if (next - current > FIVE_MINUTES) {
				files.get(i).clusterEnd = true;
			}
		}

		// convert list to 2d array
		fileClusters = new ArrayList<List<ClusteredFile>>();

		List<ClusteredFile> cluster = new ArrayList<ClusteredFile>();

		for (int i = 0; i < files.size(); i++) {
			ClusteredFile current = files.get(i);
			cluster.add(current);

			if (current.clusterEnd) {
				fileClusters.add(cluster);
				cluster = new ArrayList<ClusteredFile>();
			}
		}
	}

	public List<ClusteredFile> getNext() {
		return fileClusters.get(next());
	}

	public List<ClusteredFile> getPrevious() {
		return fileClusters.get(previous());
	}
	
	public List<ClusteredFile> getClusterAtIndex(int position) {
		return fileClusters.get(jumpTo(position));
	}

	public final int size() {
		return fileClusters.size();
	}
	
	public final int position() {
		return position + 1;
	}
	
	private int next() {
		if (position == null) {
			position = 0;
			return position;
		}
		position = (position + 1) % size();
		return position;
	}

	private int previous() {
		if (position == null) {
			position = 0;
		}
		// guard against modulo of negative numbers
		if (position - 1 < 0) {
			position += fileClusters.size();
		}
		position = (position - 1) % size();
		return position;
	}
	
	private int jumpTo(int index) {
		if (index > fileClusters.size()) index = fileClusters.size();
		index -= 1;
		if (index < 0) index = 0;
		position = index;
		return index;
	}
	
	public void printStructure() {
		for (List<ClusteredFile> cluster : fileClusters) {
			for (ClusteredFile file : cluster) {
				System.out.print(file.file.getName() + " ");
			}
			System.out.println();
		}
	}

	public String getClusterData() {
		List<ClusteredFile> cluster = fileClusters.get(position);
		String firstNumber = Tool.getNumberFromString(cluster.get(0).file.getName());
		String lastNumber = Tool.getNumberFromString(cluster.get(cluster.size() - 1).file.getName()); 
		String firstTime = Tool.processTime(cluster.get(0).time.toString());
		String lastTime = Tool.processTime(cluster.get(cluster.size() - 1).time.toString());

		// @formatter:off
		StringBuilder sb = cluster.size() == 1 
				? new StringBuilder("[ activity at " + firstTime + " ] [ image " + firstNumber + " ]")
				: new StringBuilder()
				.append("[ activity from ")
				.append(firstTime)
				.append(" to ")
				.append(lastTime)
				.append(" ] [ images ")
				.append(firstNumber)
				.append(" to ")
				.append(lastNumber)
				.append(" ]");
		// @formatter:on
		
		sb.append(" [ session " + (position + 1) + " / " + fileClusters.size() + " ]");

		return sb.toString();
	}
	
	public class ClusteredFile implements Comparable<ClusteredFile> {
		private File file;
		private Instant time;
		private boolean clusterEnd = false;

		public ClusteredFile(File file, Instant time) {
			this.file = file;
			this.time = time;
		}

		public File getFile() {
			return this.file;
		}

		@Override
		public String toString() {
			return file.getName() + (clusterEnd ? " cluster end" : "");
		}

		@Override
		public int compareTo(ClusteredFile other) {
			return (int) (this.time.getEpochSecond() - other.time.getEpochSecond());
		}
	}
}
