package mnemonic;

import components.VirtualMachine;
import operands.Operand;

public class DIV extends Mnemonic2 {

    public DIV(VirtualMachine vm) {
        super(vm, "DIV");
    }

    @Override
    public void operate(Operand A, Operand B) throws Exception{
        if (B.getValue() == 0) { 
            throw new Exception("Division by zero");
        }
        this.vm.getRegisters().setRegister(9,A.getValue() % B.getValue());
        A.setValue(A.getValue() / B.getValue());
        ModifyCC(A.getValue()); 
        
    }

}
