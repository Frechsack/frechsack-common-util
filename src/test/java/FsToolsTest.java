import com.frechsack.dev.util.FsTools;

import java.nio.file.Path;

public class FsToolsTest
{

    public static void main(String[] args)
    {
        Path path = Path.of("C:\\Users\\Patri\\Documents\\Bewerbung 2019\\Obi\\Anschreiben.pdf");

        System.out.println(FsTools.FileSystem.getExtension(path));
        System.out.println(FsTools.FileSystem.getName(path));
    }
}
