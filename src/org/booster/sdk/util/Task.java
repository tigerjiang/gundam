package org.booster.sdk.util;




/**
 * 
 * @Description: 一个要实现Runnable的抽象类，用于简化代码调用，为统一控制提供可能
 * @author Merry.Zhao
 * @date 2014-3-13 下午6:27:14
 */
public abstract class Task implements Runnable{
    //线程别名
    protected String name = "";
    //带参构造
    public Task(String name){
        this.name = name;
    }
    public Task(){
    }
    /**
     * 子类需实现该方法，该方法的逻辑将在run()方法中触发
     */
    public abstract void work();
    @Override
    public void run() {
//        System.out.println("do in thread  "+Thread.currentThread().getName()+"  "+name);
        work();
    }

}
