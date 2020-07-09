package com.doudou.meiju;

import lombok.Getter;
import lombok.Setter;

public enum MyEnum {

    mon(1){
        @Override
        public boolean isRest() {
            return super.isRest();
        }

        @Override
        public String hello() {
            return super.hello();
        }
    };

    @Setter
    @Getter
    private int value;
    private MyEnum(int value){
        this.value = value;
    }

    public boolean isRest() {
        return false;
    }

    public String hello(){
        return "hello, i am enum!";
    }
}
