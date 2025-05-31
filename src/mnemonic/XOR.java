package mnemonic;

import components.VirtualMachine;
import operands.Operand;

public class XOR extends Mnemonic2 {


    public XOR(VirtualMachine vm) {
        super(vm,"XOR");
    }

    @Override
    public void operate(Operand A, Operand B) throws Exception {
        int value = (A.getValue() ^ B.getValue()); 
        A.setValue(value); 
        ModifyCC(value);


    }



}
