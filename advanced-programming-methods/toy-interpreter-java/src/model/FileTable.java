package model;

import java.util.HashMap;

// TODO finish writing class
public class FileTable
{
    private HashMap<String, FileInfo> files;

    public void addFile(String filename, int filetype)
    {
        files.put(filename, new FileInfo(filetype));
    }

    public int readFile(String filename)
    {
        if (!files.containsKey(filename))
        {
            throw new RuntimeException("Filename was not opened");
        }

        FileInfo info = files.get(filename);
        if (info.getType() == 1)
        {
            throw new RuntimeException("File was not opened for reading");
        }

        if (info.getBuffer().isEmpty())
        {
            refillBuffer(filename);
        }

        return -1;
    }

    private void refillBuffer(String filename)
    {

    }
}
