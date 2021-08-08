package frechsack.dev.util.filesystem;

import java.nio.file.Path;

/**
 * Provides static functions for basic file-system operations.
 * @author frechsack
 */
public class FileSystemUtils
{
    private FileSystemUtils(){}

    /**
     * Returns the last {@link Path} sibling.
     *
     * @param path The Path
     * @return Return the last sibling. Returns an empty String if the Path is null or empty.
     */
    public static String getLastName(Path path)
    {
        if (path == null) return "";
        return lastSibling(path);
    }

    private static String lastSibling(Path path)
    {
        String lastName = path.getName(path.getNameCount() - 1).toString();
        return lastName == null ? "" : lastName;
    }

    /**
     * Returns the extension of a {@link Path} without the dot.
     *
     * @param path The Path.
     * @return Returns the extension. Returns an empty String if the Path does not contain an extension.
     */
    public static String getExtension(Path path)
    {
        if (path == null) return "";
        String lastSibling = lastSibling(path);
        int dotIndex = lastSibling.lastIndexOf('.');
        if (dotIndex == -1) return "";
        return lastSibling.substring(++dotIndex);
    }

    /**
     * Returns the name of a {@link Path}.
     *
     * @param path The Path.
     * @return Returns the name without any extension. Returns an empty String if the Path does not contain a sibling.
     */
    public static String getName(Path path)
    {
        if (path == null) return "";
        String lastName = getLastName(path);
        int dotIndex = lastName.lastIndexOf('.');
        if (dotIndex == -1) return "";
        return lastName.substring(0, dotIndex);
    }
}
