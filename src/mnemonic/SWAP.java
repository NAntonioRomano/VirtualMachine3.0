package mnemonic;

import components.VirtualMachine;
import operands.Operand;

public class SWAP extends Mnemonic2 {

    public SWAP(VirtualMachine vm) {
        super(vm, "SWAP");
    }

    @Override
    public void operate(Operand A, Operand B) throws Exception {

        int value = A.getValue(); //verificar que ambos sean del mismo tipo
                                    // si no lo son, lanzar una excepción
        A.setValue(B.getValue());
        B.setValue(value);
    }

}
