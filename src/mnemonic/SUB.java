package mnemonic;

import components.VirtualMachine;
import operands.Operand;  

public class SUB extends Mnemonic2 {

    public SUB(VirtualMachine vm) {
        super(vm, "SUB");
    }

    @Override
    public void operate(Operand A, Operand B) throws Exception{

        A.setValue(A.getValue() - B.getValue());
        ModifyCC(A.getValue());

    }

}
