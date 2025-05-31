package main;

import java.nio.file.Files;
import java.nio.file.Paths;
import componentsSecondPart.VirtualMachine2;
import componentsSecondPart.VirtualMainMemory2;

public class mainVM {	
	public static void main(String[] args){
		try{
			VirtualMainMemory2 vmm = new VirtualMainMemory2(16384);
			VirtualMachine2 vm = new VirtualMachine2(vmm);
			String vmi_file_path = null;
			String params[] = null;
			String file_path = args[0];
			vm.validateExtension(file_path);
			byte[] allbytes = Files.readAllBytes(Paths.get(file_path));
			vm.verify(allbytes);


			if(args.length > 1){
				String param;
				int i = 1;
				while(i < args.length && !args[i].equals("-p")){
					param = args[i];
					if(param.equals("-d")){
						vm.setDisassembler(true);
					}else if(param.contains("m=")){
						int size = Integer.parseInt(param.substring(2));
						if(size > 0){
							vmm.setMemorySize(size);
						}
					}else if(param.endsWith(".vmi")){
						vmi_file_path = param;
					}else{
						throw new Exception("Invalid parameter: " + param);
					}

					i++;
				}


				if(i < args.length && file_path.endsWith(".vmx")){
					i++;
					params = new String[args.length - i];

					for(int j = 0; j < params.length; j++){
						params[j] = args[i + j];
					}
				}
			}
			byte version = allbytes[5];
			if((version == 1 && file_path.endsWith(".vmx"))){
				vm.startMemory(allbytes);
			}else{
				vm.setVmi_file(vmi_file_path);
				
				if(params != null){
					vm.loadParams(params);
					vm.startMemory(file_path,allbytes,vm.getParamSize(params) + params.length * 4);
					vm.initStack(params.length, vm.getParamSize(params));
				}else{
					vm.startMemory(file_path,allbytes,vm.getParamSize(params) + 0);
					if(!file_path.endsWith("vmi"))
						vm.initStack(0,-1);
				}
			}

			if(vm.getDissasembler()){
				byte[] offset = new byte[2];
				System.arraycopy(allbytes, 16, offset, 0, 2);
				vm.disassembler(offset);
			}

			vm.execute();


		}catch(Exception e){
			System.out.println("Error: " + e.getMessage());
		}
	}
}

