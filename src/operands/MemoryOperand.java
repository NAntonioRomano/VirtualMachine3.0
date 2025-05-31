package operands;

import components.VirtualMachine;
import utils.utilities;

public class MemoryOperand extends Operand {
    
    
    public MemoryOperand(VirtualMachine vm) {
        super(vm);
    }

    @Override
    public int getValue() throws Exception {
        int offset = ((this.data & 0x00FFFF00) << 8) >> 16;
        int codreg = (this.data >> 4) & 0x0F;
        int cell_size = this.data & 0x3;
        int reg = this.vm.getRegisters().getRegister(codreg);
        int value = 0;

        offset += (reg & 0xFFFF);
        offset = offset & 0xFFFF;

        reg = (reg & 0xFFFF0000) | offset;

        switch (cell_size) {
            case 0:
                cell_size = 4;
                break;
            case 3:
                cell_size = 1;
                break;
        default:
                break;
        }
        value = signExtend(this.vm.getVirtualMemory().readNbytes(vm.getSegTable().LogicToPhysic(reg),cell_size), cell_size);
        return value;

    }

    @Override
    public void setValue(int value) throws Exception {
        int offset = ((this.data & 0x00FFFF00) << 8) >> 16;
        int codreg = (this.data >> 4) & 0x0F;
        int cell_size = this.data & 0x3;
        int reg = this.vm.getRegisters().getRegister(codreg);

        offset += (reg & 0xFFFF);
        offset = offset & 0xFFFF;

        reg = (reg & 0xFFFF0000) | offset;

        switch (cell_size) {
            case 0:
                cell_size = 4;
                break;
            case 3:
                cell_size = 1;
                break;
            default:
                break;
        }
        value  = signExtend(value, cell_size);
        this.vm.getVirtualMemory().writeNbytes(vm.getSegTable().LogicToPhysic(reg), cell_size, value);

    }  

    @Override
    public String toString() {
        String result = "";
        int offset = ((this.data & 0x00FFFF00) << 8) >> 16;
        int codreg = (this.data >> 4) & 0x0F;
        int cell_size = this.data & 0x3;
        char access_modificator;

        switch (cell_size) {
            case 0:
                access_modificator = 'l';
                break;
            case 3:
                access_modificator = 'b';
                break;
            default:
                access_modificator = 'w';
        }

        result += access_modificator + "[";
        if(codreg == 1){
            result += Integer.toString(offset);
        }else{
            result += utilities.nameRegisters[codreg];
            if(offset != 0){
                result += "+" + Integer.toString(offset);
            }   
        }

        result += "]";
        
        return result;
    }

    private int signExtend(int value, int cell_size){
        switch (cell_size) {
            case 1:
                return ((value << 24) >> 24);
            case 2:
                return ((value << 16)>>16);
            default:
                return value;
        }
    }
} 

