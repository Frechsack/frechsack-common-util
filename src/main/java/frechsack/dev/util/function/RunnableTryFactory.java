package frechsack.dev.util.function;

class RunnableTryFactory
{
    private RunnableTryFactory(){}

    static class SimpleRunnableTry implements RunnableTry{

        private final Runnable runnable;

        SimpleRunnableTry(Runnable runnable) {this.runnable = runnable;}

        @Override
        public void run()
        {
            runnable.run();
        }

        @Override
        public void tryRun()
        {
            runnable.run();
        }
    }
}