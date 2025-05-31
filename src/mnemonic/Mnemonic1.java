package mnemonic;

import components.VirtualMachine;
import operands.Operand;


public abstract class Mnemonic1 extends Mnemonic {
    
    public Mnemonic1(VirtualMachine vm,String mnemonic) {
        super(vm, mnemonic);
    }

    abstract public void operate(Operand A) throws Exception;

    public String toString(Operand A){
        return (this.mnemonic + " " + A.toString());
    }
}

    