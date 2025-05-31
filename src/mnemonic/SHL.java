package mnemonic;

import components.VirtualMachine;
import operands.Operand;

public class SHL extends Mnemonic2 {

    public SHL(VirtualMachine vm) {
        super(vm, "SHL");
    }

    @Override
    public void operate(Operand A, Operand B) throws Exception {

        int value = (A.getValue() << B.getValue()); // Operación de desplazamiento a la izquierda
        A.setValue(value); // Almacena el resultado en A
        ModifyCC(value); // Modifica el CC de acuerdo al resultado}

    }

}
