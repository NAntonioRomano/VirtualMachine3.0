package operands;

import components.VirtualMachine;

public abstract class Operand {

    protected VirtualMachine vm;
    protected int data;

    public Operand(VirtualMachine vm) {
        this.vm = vm;
    }
    
    public abstract int getValue() throws Exception;
    
    public abstract void setValue(int value) throws Exception;

    public void setData(int data) {
        this.data = data;
    }

    public int getData() {
        return this.data;
    }

    abstract public String toString();
    
}

