package mnemonic;

import components.VirtualMachine;

public abstract class Mnemonic0 extends Mnemonic {
    
    public Mnemonic0(VirtualMachine vm,String mnemonic) {
        super(vm, mnemonic);
    }
    

    abstract public void operate() throws Exception;
    
    public String toString(){
        return this.mnemonic;
    }

}