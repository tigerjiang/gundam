package org.booster.sdk.logging.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.booster.sdk.logging.Logger;
import org.booster.sdk.util.CommonTools;
import org.booster.sdk.util.Task;


public class FileLoggerImpl extends Logger {
    private static ExecutorService threadPool;
    private static final String LINE_SEP = System.getProperty("line.separator");
    QuietWriter qw = null;
    
    private void init(){
        if(qw !=null){
            return;
        }
        try {
            setFile(logFilePath, true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * 添加一个任务到处理线程池中
     * @param task
     */
    private void addTask(Runnable task) {
        if (threadPool == null) {
            synchronized (FileLoggerImpl.class) {
                if (threadPool == null) {
                    threadPool = Executors.newSingleThreadExecutor();
                    threadPool.execute(task);
                }
            }
        } else {
            threadPool.execute(task);
        }
    }

    /**
     * 停止线程池,并销毁
     */
    private void shutdown() {
        
        if (threadPool != null && !threadPool.isShutdown()) {
            threadPool.shutdown();
            threadPool = null;
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        shutdown();
        closeWriter();
        super.finalize();
    }

    @Override
    protected void verbose(final String tag, final String text) {
        addTask(new Task(){

            @Override
            public void work() {
               write(CommonTools.getCurrentDateTime()+" "+tag+" "+text);
                
            }
            
        });

    }

    @Override
    protected void debug(final String tag, final String text) {
        addTask(new Task(){

            @Override
            public void work() {
               write(CommonTools.getCurrentDateTime()+" "+tag+" "+text);
                
            }
            
        });

    }

    @Override
    protected void info(final String tag, final String text) {
        addTask(new Task(){

            @Override
            public void work() {
               write(CommonTools.getCurrentDateTime()+" "+tag+" "+text);
                
            }
            
        });

    }

    @Override
    protected void warn(final String tag, final String text) {
        addTask(new Task(){

            @Override
            public void work() {
               write(CommonTools.getCurrentDateTime()+" "+tag+" "+text);
                
            }
            
        });

    }

    @Override
    protected void error(final String tag, final String text) {
        addTask(new Task(){

            @Override
            public void work() {
               write(CommonTools.getCurrentDateTime()+" "+tag+" "+text);
                
            }
            
        });

    }

    @Override
    protected void fetal(final String tag, final String text) {
        addTask(new Task(){

            @Override
            public void work() {
               write(CommonTools.getCurrentDateTime()+" "+tag+" "+text);
                
            }
            
        });

    }
    
    class QuietWriter extends FilterWriter{

        protected QuietWriter(Writer out) {
            super(out);
        }
        
        public void write(String string){
            try{
                super.out.write(string);
            }catch(IOException ex){
                ex.printStackTrace();
            }
        } 
        
        public void flush(){
            try{
                super.out.flush();
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
        
    }

    public synchronized void setFile(String fileName, boolean append)
        throws IOException
    {
        reset();
        FileOutputStream ostream = null;
        try
        {
            ostream = new FileOutputStream(fileName, append);
        }
        catch(FileNotFoundException ex)
        {
            String parentName = (new File(fileName)).getParent();
            if(parentName != null)
            {
                File parentDir = new File(parentName);
                if(!parentDir.exists() && parentDir.mkdirs())
                    ostream = new FileOutputStream(fileName, append);
                else
                    throw ex;
            } else
            {
                throw ex;
            }
        }
        Writer fw = createWriter(ostream);
        setQWForFiles(fw);
    }
    protected OutputStreamWriter createWriter(OutputStream os)
    {
        OutputStreamWriter retval = null;
        String enc = "UTF-8";
        if(enc != null)
            try
            {
                retval = new OutputStreamWriter(os, enc);
            }
            catch(IOException e)
            {
                System.out.println("Error initializing output writer.");
                System.out.println("Unsupported encoding?");
            }
        if(retval == null)
            retval = new OutputStreamWriter(os);
        return retval;
    }

    protected void reset()
    {
        closeWriter();
    }
    protected void closeWriter()
    {
        if(qw != null)
            try
            {
                qw.close();
                qw = null;
            }
            catch(IOException e)
            {
                System.out.println("Could not close " + qw+e);
            }
    }
    protected void setQWForFiles(Writer writer)
    {
        qw = new QuietWriter(writer);
    }
    private void write(String string){
        init();
        if(qw !=null){
            qw.write(string);
            qw.write(LINE_SEP);
            qw.flush();
        }
    }
}
