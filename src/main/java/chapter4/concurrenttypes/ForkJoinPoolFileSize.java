package chapter4.concurrenttypes;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

public class ForkJoinPoolFileSize {

    private final static java.util.concurrent.ForkJoinPool forkJoinPool = new java.util.concurrent.ForkJoinPool();

    private static class FileSizeFinder extends RecursiveTask<Long> {
        final File file;

        public FileSizeFinder(final File theFile) {
            file = theFile;
        }

        @Override
        public Long compute() {
            long size = 0;
            if (file.isFile()) {
                size = file.length();
            } else {
                final File[] children = file.listFiles();
                if (children != null) {
                    List<ForkJoinTask<Long>> tasks = new ArrayList<ForkJoinTask<Long>>();
                    for(final File child : children) {
                        if (child.isFile()) {
                            size += child.length();
                        } else {
                            tasks.add(new FileSizeFinder(child));
                        }
                    }

                    for(final ForkJoinTask<Long> task : invokeAll(tasks)) {
                        size += task.join();
                    }
                }
            }

            return size;
        }
    }

    public static void main(final String[] args) {
        final long start = System.nanoTime();
        final long total = forkJoinPool.invoke(new FileSizeFinder(new File(args[0])));
        final long end = System.nanoTime();
        System.out.println("Total Size: " + total);
        System.out.println("Time taken: " + (end - start)/1.0e9);
    }
}