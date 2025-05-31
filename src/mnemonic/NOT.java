package mnemonic;
import components.VirtualMachine;
import operands.Operand;

public class NOT extends Mnemonic1 {

    public NOT(VirtualMachine vm) {
        super(vm, "NOT");
    }

    @Override
    public void operate(Operand A) throws Exception {
        int value = ~(A.getValue()); 
        A.setValue(value); 
        ModifyCC(value); 
    }

}
