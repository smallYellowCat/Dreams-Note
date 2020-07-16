package com.doudou.dp.behavior;
/**
 * 模板方法模式
 *
 * 模板方法模式相对简单，他的定义说明了一切。
 * 定义：定义一个操作中的算法的框架，而将一些步骤延迟到子类中。
 *      使得子类可以不改变一个算法的结构即可冲定义该算法的某些
 *      特定步骤。
 * 特征： 抽象类称之为抽象模板， 方法分两类：
 * 1. 基本方法：基本方法也叫基本操作，是子类负责实现，并且会在模板方法中调用。
 * 2. 模板方法：可以有一个或几个，一般是一个具体的方法，也就是一个框架，实现对
 *             基本方法的调度，完成固定的逻辑。为了防止被恶意篡改，模板方法一
 *             般会加上final关键字，不允许被重写。
 * 扩展： 通过子类的某个方法（钩子函数）的返回值来决定公共部分的执行结果。即通过
 *      钩子约束模板方法的行为
 * @author doudou
 */
public class TemplateMethodPattern {

    //参考大话设计模式中的示例，以车模来演示。

    //首先有一个抽象的模板

    static abstract  class AbstractModel {
        //定义子类应该具有的能力

        abstract void start();

        abstract void alarm();

        abstract void stop();

        //钩子函数
        boolean needAlarm() {
            return true;
        }


        //模板方法，定义基本操作的执行顺序。

        final void run() {
            this.start();
            if (this.needAlarm()) {
                this.alarm();
            }
            this.stop();
        }
    }


    //宝马模型
    static class BMWModel extends AbstractModel {

        @Override
        void start() {
            System.out.println("发动宝马车。。。。。。。");
        }

        @Override
        void alarm() {
            System.out.println("嘀嘀， 宝马车按喇叭了。。。。。");
        }

        @Override
        void stop() {
            System.out.println("宝马车停了。。。。。");
        }
    }


    //奔驰模型
    static class BenZModel extends AbstractModel {

        private boolean needAlarm = true;

        @Override
        void start() {
            System.out.println("发动奔驰车。。。。。");
        }

        @Override
        void alarm() {
            System.out.println("嘟嘟，奔驰车按喇叭了。。。。。");
        }

        @Override
        void stop() {
            System.out.println("奔驰车停下来了。。。。");
        }

        @Override
        boolean needAlarm() {
            return this.needAlarm;
        }

        public void setNeedAlarm(boolean needAlarm) {
            this.needAlarm = needAlarm;
        }
    }


    public static void main(String[] args) {
        BMWModel bmwModel = new BMWModel();
        bmwModel.run();

        BenZModel benZModel = new BenZModel();
        benZModel.setNeedAlarm(false);
        benZModel.run();
        benZModel.setNeedAlarm(true);
        benZModel.run();
    }
}