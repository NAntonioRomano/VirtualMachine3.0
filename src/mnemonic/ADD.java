package mnemonic;

import components.VirtualMachine;
import operands.Operand; 

public class ADD extends Mnemonic2 {

    public ADD(VirtualMachine vm) {
        super(vm, "ADD");
    }

    @Override
    public void operate(Operand A, Operand B) throws Exception{
        
        int result = A.getValue() + B.getValue();

        A.setValue(result);
        ModifyCC(result); 

    }



}
