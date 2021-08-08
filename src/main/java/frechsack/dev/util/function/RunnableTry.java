package frechsack.dev.util.function;

@FunctionalInterface
public interface RunnableTry extends Runnable
{
    @Override
    default void run(){
        try
        {
            tryRun();
        }catch (Exception ignored){
        }
    }

    void tryRun() throws Exception;

    static RunnableTry of(Runnable runnable){
        return runnable == null ? null :  new RunnableTryFactory.SimpleRunnableTry(runnable);
    }
}
