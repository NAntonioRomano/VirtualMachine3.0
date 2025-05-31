package mnemonic;

import components.VirtualMachine;
import operands.Operand;

public class SHR extends Mnemonic2 {

    public SHR(VirtualMachine vm) {
        super(vm, "SHR");
    }

    @Override
    public void operate(Operand A, Operand B) throws Exception {

        int value = A.getValue() >> B.getValue(); // Operación de desplazamiento a la derecha
        A.setValue(value); // Almacena el resultado en A
        ModifyCC(value); // Modifica el CC de acuerdo al resultado
    }

}
