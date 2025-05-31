package mnemonic;

import components.VirtualMachine;
import operands.Operand;

public abstract class Mnemonic2 extends Mnemonic {
    
    public Mnemonic2(VirtualMachine vm,String mnemonic) {
        super(vm, mnemonic);
    }
    
    abstract public void operate(Operand A, Operand B) throws Exception; 

    public String toString(Operand A, Operand B){
        return (this.mnemonic + " " + A.toString() + ", " + B.toString());
    }
    

}