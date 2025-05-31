package mnemonic;

import components.VirtualMachine;
import operands.Operand;  

public class MUL extends Mnemonic2 {

    public MUL(VirtualMachine vm) {
        super(vm, "MUL");
    }

    @Override
    public void operate(Operand A, Operand B) throws Exception{
        int result = A.getValue() * B.getValue();
        A.setValue(result);
        ModifyCC(result); 
    }

}
