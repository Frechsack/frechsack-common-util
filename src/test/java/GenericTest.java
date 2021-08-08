import java.io.IOException;

import static frechsack.dev.util.Functional.*;

public class GenericTest
{
    public static void main(String[] args) throws IOException, InterruptedException
    {

        Integer i = tryGetWith(() -> () -> System.out.println("Called close"), it -> 26, ex -> 12);

        try

        System.out.println(i);
    }

    public static void x() throws Exception
    {
        System.out.println("Called X");
    }

}
