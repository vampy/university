package model;

import java.util.ArrayList;
import java.util.List;

// TODO finish writing class
public class FileInfo
{
    private int type; // R = 0, W = 1
    private final int READ  = 0;
    private final int WRITE = 1;

    final static int           BUFFER_SIZE    = 10;
    private      List<Integer> buffer         = new ArrayList<Integer>(BUFFER_SIZE);
    private      int           bufferPosition = 0;
    private      int           filePosition   = 0;

    public FileInfo(int type)
    {
        this.type = type;
    }

    /**
     * Getter for property 'buffer'.
     *
     * @return Value for property 'buffer'.
     */
    public List<Integer> getBuffer()
    {
        return buffer;
    }

    public int getInt()
    {
        int ret = buffer.get(bufferPosition);
        bufferPosition++;

        return ret;
    }

    public boolean isBufferFull()
    {
        return bufferPosition >= BUFFER_SIZE;
    }

    /**
     * Getter for property 'type'.
     *
     * @return Value for property 'type'.
     */
    public int getType()
    {
        return type;
    }

    /**
     * Setter for property 'type'.
     *
     * @param type Value to set for property 'type'.
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /**
     * Getter for property 'bufferPosition'.
     *
     * @return Value for property 'bufferPosition'.
     */
    public int getBufferPosition()
    {
        return bufferPosition;
    }

    /**
     * Setter for property 'bufferPosition'.
     *
     * @param bufferPosition Value to set for property 'bufferPosition'.
     */
    public void setBufferPosition(int bufferPosition)
    {
        this.bufferPosition = bufferPosition;
    }

    /**
     * Getter for property 'filePosition'.
     *
     * @return Value for property 'filePosition'.
     */
    public int getFilePosition()
    {
        return filePosition;
    }

    /**
     * Setter for property 'filePosition'.
     *
     * @param filePosition Value to set for property 'filePosition'.
     */
    public void setFilePosition(int filePosition)
    {
        this.filePosition = filePosition;
    }
}

/*

public class FileProperties {
	private int typeOfFile;
	private FileBuffer buffer;
	private int filePosition;

	public FileProperties(int typeOfFile, int filePosition) {
		this.typeOfFile = typeOfFile;
		this.filePosition = filePosition;
		this.buffer = new FileBuffer();
	}

	public FileBuffer getBuffer() {
		return this.buffer;
	}

	public int getTypeOfFile() {
		return this.typeOfFile;
	}

	public int getFilePosition() {
		return this.filePosition;
	}

	public void setFilePosition(int filePosition) {
		this.filePosition = filePosition;
	}

	public String toString() {
		return "Type of file: " + this.typeOfFile + "\nBuffer : " + buffer.getBuffer().toString() + "\nfile position :" + this.filePosition + "\n";
	}
}

package Model;

import java.util.ArrayList;

public class FileBuffer {
	private ArrayList<Integer> buffer;
	private int maxSize;
	int position;

	public FileBuffer() {
		this.buffer = new ArrayList<Integer>();
		this.maxSize = 2;
	}

	public ArrayList<Integer> getBuffer() {
		return this.buffer;
	}

	public int getMaxSize() {
		return this.maxSize;
	}

	public Boolean isEmpty() {
		return this.buffer.isEmpty();
	}

	public Integer getFirstInt() {
		Integer p = this.buffer.get(0);
		this.buffer.remove(0);
		return p;
	}

	public void insert(Integer i) {
		this.buffer.add(i);
	}
}


package PrgState;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import Model.FileProperties;

public class FileTable implements Cloneable {
	private HashMap<String, FileProperties> files;

	public FileTable() {
		this.files = new HashMap<String, FileProperties>();
	}

	public HashMap<String, FileProperties> getFiles() {
		return this.files;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void insert(String fileName, int fileType) {
		FileProperties fileProp = new FileProperties(fileType, 0);

		this.files.put(fileName, fileProp);
	}

	public Integer readFromFile(String fileName) {
		if (!this.files.containsKey(fileName)) {
			return null;
		} else {
			FileProperties fileProp = this.files.get(fileName);
			if (fileProp.getBuffer().isEmpty()) {
				if (fileProp.getFilePosition() % fileProp.getBuffer().getMaxSize() != 0) {
					return null;
				}

				refillBufferOfFile(fileName);
			}

			return fileProp.getBuffer().getFirstInt();
		}
	}

	public void refillBufferOfFile(String fileName) {
		if (!this.files.containsKey(fileName)) {
			return;
		}
		FileProperties fp = this.files.get(fileName);
		Scanner sc;
		try {
			sc = new Scanner(new File(fileName));
			int pos = fp.getFilePosition();

			while (pos > 0) {
				sc.nextInt();
				pos--;
			}

			while (sc.hasNext() && pos < fp.getBuffer().getMaxSize()) {
				fp.getBuffer().insert(sc.nextInt());
				pos++;
			}

			fp.setFilePosition(fp.getFilePosition() + pos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


	}

	public String toString() {
		return this.files.toString() + "\n";
	}
}
 */