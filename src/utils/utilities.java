package utils;

public class utilities {
        public static String[] nameRegisters = {
                "CS",
                "DS",
                "ES",
                "SS",
                "KS",
                "IP",
                "SP",
                "BP",
                "CC",
                "AC",
                "EAX",
                "EBX",
                "ECX",
                "EDX",
                "EEX", 
                "EFX"
        };

        public static byte[] defaultRegisters = {
                //CS
                0x00,0x00,0x00,0x00,
                //DS
                0x00,0x01,0x00,0x00,
                //ES
                (byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
                //SS
                (byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
                //KS
                (byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
                //IP
                0x00,0x00,0x00,0x00,
                //SP
                0x00,0x00,0x00,0x00,
                //BP
                0x00,0x00,0x00,0x00,
                //CC
                0x00,0x00,0x00,0x00,
                //AC
                0x00,0x00,0x00,0x00,
                //EAX
                0x00,0x00,0x00,0x00,
                //EBX
                0x00,0x00,0x00,0x00,
                //ECX
                0x00,0x00,0x00,0x00,
                //EDX
                0x00,0x00,0x00,0x00,
                //EEX
                0x00,0x00,0x00,0x00,
                //EFX
                0x00,0x00,0x00,0x00,      
        };
}    
