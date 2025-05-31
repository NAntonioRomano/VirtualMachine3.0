package operands;

import components.VirtualMachine;
import utils.utilities;

public class RegisterOperand extends Operand {
    
    public RegisterOperand(VirtualMachine vm) {
        super(vm);
    }
    
    @Override
    public int getValue() throws Exception {
        int codreg = (this.data >> 4) & 0x0F;
        int regseg = (this.data >> 2) & 0x03;
        int valor = this.vm.getRegisters().getRegister(codreg);

        if (regseg == 1) {
            valor = valor & 0xFF;
            valor = (valor << 24) >> 24;
        } else if (regseg == 2) {
            valor = valor & 0xFF00;
            valor = (valor << 16) >> 16;
            valor = (valor >> 8);

        } else if (regseg == 3) {
            valor = valor & 0xFFFF;
            valor = (valor << 16) >> 16;

        }
        return valor;
    }

    
    @Override
    public void setValue(int value) throws Exception {
        int codreg = (this.data >> 4) & 0x0F;
        int regseg = (this.data >> 2) & 0x03;
        int reg = vm.getRegisters().getRegister(codreg);
        
        if (regseg == 1) {
            value = value & 0xFF;
            vm.getRegisters().setRegister(codreg, reg & 0xFFFFFF00 | value);
        } else if (regseg == 2) {
            value = (value << 8) & 0xFF00;
            vm.getRegisters().setRegister(codreg, reg & 0xFFFF00FF | value);
        } else if (regseg == 3) {
            value = value  & 0xFFFF;
            vm.getRegisters().setRegister(codreg, reg & 0xFFFF0000 | value);
        } else {
            vm.getRegisters().setRegister(codreg, value);
        }
    } 

    @Override
    public String toString() {
        String result = "";
        int codreg = (this.data >> 4) & 0x0F;
        int regseg = (this.data >> 2) & 0x03;
        
        result += utilities.nameRegisters[codreg];
        
        if(codreg >= 10 && codreg <= 15 && regseg != 0){  
            result = result.substring(1);
            if(regseg == 1){
                result = result.replace("X","L");
            }else if(regseg == 2){
                
                result = result.replace("X","H");
            }

        }
        return result;
    }
}


    