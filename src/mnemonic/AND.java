package mnemonic;

import components.VirtualMachine;
import operands.Operand;

public class AND extends Mnemonic2 {

    public AND(VirtualMachine vm) {
        super(vm, "AND");
    }

    @Override
    public void operate(Operand A, Operand B) throws Exception {
        int value = A.getValue() & B.getValue(); // Operación AND bit a bit
        A.setValue(value); // Almacena el resultado en A
        ModifyCC(value); // Modifica el CC de acuerdo al resultado
        }
}


