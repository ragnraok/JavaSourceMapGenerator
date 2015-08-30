package com.ragnarok.javasourcemapgenerator;

import com.ragnarok.javasourcemapgenerator.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by ragnarok on 15/8/2.
 */
public class JavaFileScanner {

    public static final String TAG = "JavaSourceMapGenerator.JavaFileScanner";

    private String dir;

    private ArrayList<String> allJavaSourcePaths = new ArrayList<>();
    
    private int threadNumber;
    
    private boolean isInParallelMode = false;
    
    private ThreadPoolExecutor executorService;

    public JavaFileScanner(String dir) {
        this.dir = dir;
        this.threadNumber = 0;
        isInParallelMode = false;
    }
    
    public JavaFileScanner(String dir, int minThreadNumber) {
        this.dir = dir;
        this.threadNumber = minThreadNumber;
        if (this.threadNumber > 0) {
            initThreadPool();
            isInParallelMode = true;
        }
    }
    
    private void initThreadPool() {
        executorService = new ThreadPoolExecutor(threadNumber, threadNumber * 2, 2L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        executorService.allowCoreThreadTimeOut(true);
    }

    public ClassNameMaps scanAllJavaSources() throws FileNotFoundException {
        initJavaSourcePaths();
//        Log.d(TAG, "source paths: %s\n", allJavaSourcePaths.toString());
        if (!isInParallelMode) {
           return scanInSingleThread();
        } else {
            return scanInMultiThreads();
        }
    }
    
    private ClassNameMaps scanInSingleThread() {
//        long startTime = System.currentTimeMillis();
        ClassNameMaps result = new ClassNameMaps();
        if (allJavaSourcePaths.size() > 0) {
            for (String path : allJavaSourcePaths) {
//                Log.d(TAG, "parsing: %s", path);
                SourceClassParser sourceClassParser = new SourceClassParser(path);
                result.addAll(sourceClassParser.parse());
            }
        }
//        long endTime = System.currentTimeMillis();
//        Log.i(TAG, "parse finish, used: %dms", endTime - startTime);
        return result;
    }
    
    private class SubTaskRunnable implements Runnable {
        
        private List<String> subTaskList;
        private ClassNameMaps result;
        private int threadNo;
        
        public SubTaskRunnable(List<String> subTaskList, ClassNameMaps result, int threadNo) {
            this.subTaskList = subTaskList;
            this.result = result;
            this.threadNo = threadNo;
        }

        @Override
        public void run() {
            for (String path : subTaskList) {
//                Log.d(TAG, "parsing: %s", path);
                SourceClassParser sourceClassParser = new SourceClassParser(path);
                result.addAll(sourceClassParser.parse());
            }
            
            Log.i(TAG, "thread: %d, parsing finish", threadNo);
        }
    }
    
    private ClassNameMaps scanInMultiThreads() {
        int size = allJavaSourcePaths.size();
        Log.i(TAG, "size: %d, threadNumber: %d", size, threadNumber);
        int eachSubtaskListsize = size / threadNumber;
        int currentSplitStartIndex = 0;
        ClassNameMaps result = new ClassNameMaps();
        Future[] futureList = new Future[threadNumber];
        for (int i = 0; i < threadNumber; i++) {
            int startIndex = currentSplitStartIndex > size ? size - 1 : currentSplitStartIndex;
            int endIndex = startIndex + eachSubtaskListsize > size ? size - 1 : startIndex + eachSubtaskListsize;
            Log.i(TAG, "scanInMultiThreads, startIndex: %d, endIndex: %d", startIndex, endIndex);
            currentSplitStartIndex = endIndex;
            List<String> subTaskList = allJavaSourcePaths.subList(startIndex, endIndex);
            if (subTaskList.size() > 0) {
                futureList[i] = executorService.submit(new SubTaskRunnable(subTaskList, result, i), true);
            }
        }
                
        // wait all tasks finish
        for (Future future : futureList) {
            try {
                future.get();
            } catch (Exception e) {
                Log.e(TAG, "scanInMultiThreads error: %s", e.getMessage());
            }
        }
        executorService.shutdown();

        return result;
    }

    private void initJavaSourcePaths() throws FileNotFoundException {
        File rootPath = new File(dir);
        if (!rootPath.exists()) {
            throw new FileNotFoundException(String.format("Directory %s not exist!", dir));
        }
        allJavaSourcePaths.clear();
        initJavaSourcePathsRecursive(rootPath);
    }

    private void initJavaSourcePathsRecursive(File rootPath) {
        File[] children = rootPath.listFiles();
        if (children != null && children.length > 0) {
            for (File child : children) {
                if (child.isFile() && child.getAbsolutePath().endsWith(".java")) {
                    String path = child.getAbsolutePath();
                    allJavaSourcePaths.add(path);
                }
                initJavaSourcePathsRecursive(child);
            }
        }
    }
}

